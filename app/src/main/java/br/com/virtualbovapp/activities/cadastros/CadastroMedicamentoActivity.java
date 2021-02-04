package br.com.virtualbovapp.activities.cadastros;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import br.com.virtualbovapp.adapters.ConsultaLoteMedicamentoAdapter;
import br.com.virtualbovapp.dialogs.CriaLoteMedicamentoDialog;
import br.com.virtualbovapp.model.Lote_Medicamento;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.model.Medicamento;
import br.com.virtualbovapp.helpers.LoteMedicamentoItemTouchHelper;
import br.com.virtualbovapp.model.ProtocoloSanitario;

public class CadastroMedicamentoActivity extends AppCompatActivity implements LoteMedicamentoItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaLoteMedicamentoAdapter.MedicamentosAdapterListener, CriaLoteMedicamentoDialog.CriaLoteMedicamento, CriaLoteMedicamentoDialog.AlteraLoteMedicamento {
    private EditText ed_nome_medicamento, ed_desc_complementar_medicamento;
    private ImageButton ib_addlote_medicamento;
    private String _modo;
    private Medicamento _medicamento;
    private RecyclerView recyclerView;
    private TextView tv_title_lote_medicamento, tv_title_empty;
    private LinearLayoutManager linearLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private ConsultaLoteMedicamentoAdapter loteMedicamentosAdapter;
    private AlertDialog alerta;
    private CriaLoteMedicamentoDialog criaLoteMedicamentoDialog;
    private ArrayList<Lote_Medicamento> lote_medicamentos;
    private static String ROOT = "BD";
    private static String CHILDREN_MEDICAMENTO = "medicamento";
    private static String CHILDREN_PROTOCOLO = "protocolo";
    private static String CHILDREN_MEDICAMENTO_PROTOCOLO = "medicamento_protocolo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        if (_modo.equals("UPD")) {
            preencheCampos(_medicamento);

            setAdapter();

            botaoInclusao();
        }
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_medicamento"))
            _medicamento =   getIntent().getParcelableExtra("_medicamento");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_medicamento_activity);

        ed_nome_medicamento = findViewById(R.id.ed_nome_medicamento);
        ed_desc_complementar_medicamento = findViewById(R.id.ed_desc_complementar_medicamento);
        tv_title_lote_medicamento = findViewById(R.id.tv_title_lote_medicamento);
        recyclerView = findViewById(R.id.recycler_view);
        tv_title_empty = findViewById(R.id.tv_title_empty);
        ib_addlote_medicamento = findViewById(R.id.ib_addlote_medicamento);
    }

    private void personalizaView()
    {
        ed_nome_medicamento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_medicamento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (_modo.equals("INS"))
        {
            tv_title_lote_medicamento.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_title_empty.setVisibility(View.INVISIBLE);
            ib_addlote_medicamento.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_title_empty.setText("Não temos nenhum lote cadastrado para esse medicamento");

            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            itemTouchHelperCallback = new LoteMedicamentoItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    private void preencheCampos(Medicamento medicamento)
    {
        ed_nome_medicamento.setText(medicamento.getNome_medicamento());
        ed_desc_complementar_medicamento.setText(medicamento.getDesc_complementar_medicamento());

        if(medicamento.getLote_medicamento() != null)
            lote_medicamentos = medicamento.getLote_medicamento();
        else
            lote_medicamentos = new ArrayList<>();
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_medicamento.getText().length() == 0)
            ed_nome_medicamento.setError("Informe o nome da medicamento!");
        else
            validacao = true;

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN_MEDICAMENTO);
        String key_medicamento = databaseReference.push().getKey();

        Medicamento medicamento = new Medicamento();
        medicamento.setNome_medicamento(ed_nome_medicamento.getText().toString());
        medicamento.setDesc_complementar_medicamento(ed_desc_complementar_medicamento.getText().toString());
        medicamento.setLote_medicamento(lote_medicamentos);
        medicamento.setKey_medicamento(key_medicamento);

        databaseReference.child(key_medicamento).setValue(medicamento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erro ao salvar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void alteraCadastroFirebase(final String key_medicamento)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_MEDICAMENTO).child(key_medicamento);

        final Medicamento medicamento = new Medicamento();
        medicamento.setNome_medicamento(ed_nome_medicamento.getText().toString());
        medicamento.setDesc_complementar_medicamento(ed_desc_complementar_medicamento.getText().toString());
        medicamento.setLote_medicamento(lote_medicamentos);
        medicamento.setKey_medicamento(key_medicamento);

        databaseReference.setValue(medicamento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Atualiza a referência do medicamento no Cadastro de Protocolo Sanitário
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            ProtocoloSanitario protocoloSanitario = data.getValue(ProtocoloSanitario.class);

                            for(int i = 0; i < protocoloSanitario.getMedicamento_protocolo().size(); i++) {
                                Medicamento m = protocoloSanitario.getMedicamento_protocolo().get(i);

                                if (m.getKey_medicamento().equals(key_medicamento))
                                {
                                    String key_protocolo = protocoloSanitario.getKey_protocolo();
                                    atualizaMedicamentoProtocolo(key_protocolo, i, medicamento);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erro ao atualizar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void atualizaMedicamentoProtocolo(String key_protocolo, int position, Medicamento medicamento)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO).child(key_protocolo).child(CHILDREN_MEDICAMENTO_PROTOCOLO).child(String.valueOf(position));

        databaseReference.setValue(medicamento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erro ao atualizar as referências do cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAdapter()
    {
        loteMedicamentosAdapter = new ConsultaLoteMedicamentoAdapter(lote_medicamentos, this);
        recyclerView.setAdapter(loteMedicamentosAdapter);
        loteMedicamentosAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void botaoInclusao()
    {
        ib_addlote_medicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaLoteMedicamentoDialog = CriaLoteMedicamentoDialog.newInstance("INS", new Lote_Medicamento());
                criaLoteMedicamentoDialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        menu.findItem(R.id.salvarButton);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.salvarButton) {
            if (validacoesCadastro())
            {
                if (_modo.equals("INS"))
                    criaCadastroFirebase();
                else
                    alteraCadastroFirebase(_medicamento.getKey_medicamento());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMedicamentoSelected(Lote_Medicamento lote_medicamento) {
        criaLoteMedicamentoDialog = CriaLoteMedicamentoDialog.newInstance("UPD", lote_medicamento);
        criaLoteMedicamentoDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof ConsultaLoteMedicamentoAdapter.LoteMedicamentoViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir do medicamento "  +  _medicamento.getNome_medicamento();
            message += " o lote " + loteMedicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_medicamento() + "?";

            builder.setTitle("Exclusão de lote de medicamento");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.loteCacinaMovimentado(loteMedicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getMedicamento_lote().getId_medicamento(), loteMedicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_medicamento()) == false) {
                    String messageToast =   "Lote " + loteMedicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_medicamento();
                    messageToast += " foi excluído com sucesso!";

                    loteMedicamentosAdapter.removeItem(viewHolder.getAdapterPosition());

                    Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                    //Toast.makeText(getBaseContext(), "Lote desta medicamento não pode ser deletado pois já possui movimentação!", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    itemTouchHelper.attachToRecyclerView(null);
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                    dialog.cancel();
                }
            });

            alerta = builder.create();
            alerta.show();
            alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    itemTouchHelper.attachToRecyclerView(null);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            });
        }
    }

    @Override
    public void criaLoteMedicamento(Lote_Medicamento lote_medicamento) {
        boolean achou = false;

        for(int i = 0; i < lote_medicamentos.size(); i++) {
            if(lote_medicamentos.get(i).getNumero_lote_medicamento().equals(lote_medicamento.getNumero_lote_medicamento()))
            {
                achou = true;
                break;
            }
        }

        if(achou)
            Toast.makeText(getBaseContext(), "Este lote já foi adicionado ao medicamento!", Toast.LENGTH_LONG).show();
        else
        {
            lote_medicamentos.add(lote_medicamento);
            loteMedicamentosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void alteraLoteMedicamento(Lote_Medicamento lote_medicamento) {
        for(int i = 0; i < lote_medicamentos.size(); i++) {
            if(lote_medicamentos.get(i).getNumero_lote_medicamento().equals(lote_medicamento.getNumero_lote_medicamento()))
            {
                lote_medicamentos.set(i, lote_medicamento);
                loteMedicamentosAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}