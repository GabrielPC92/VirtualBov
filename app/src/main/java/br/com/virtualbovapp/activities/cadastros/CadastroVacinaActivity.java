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
import br.com.virtualbovapp.adapters.ConsultaLoteVacinaAdapter;
import br.com.virtualbovapp.dialogs.CriaLoteVacinaDialog;
import br.com.virtualbovapp.model.Lote_Vacina;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.model.ProtocoloSanitario;
import br.com.virtualbovapp.model.Vacina;
import br.com.virtualbovapp.helpers.LoteVacinaItemTouchHelper;

public class CadastroVacinaActivity extends AppCompatActivity implements LoteVacinaItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaLoteVacinaAdapter.VacinasAdapterListener, CriaLoteVacinaDialog.CriaLoteVacina, CriaLoteVacinaDialog.AlteraLoteVacina {
    private EditText ed_nome_vacina, ed_desc_complementar_vacina;
    private ImageButton ib_addlote_vacina;
    private String _modo;
    private Vacina _vacina;
    private RecyclerView recyclerView;
    private TextView tv_title_lote_vacina, tv_title_empty;
    private LinearLayoutManager linearLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private ConsultaLoteVacinaAdapter loteVacinasAdapter;
    private AlertDialog alerta;
    private CriaLoteVacinaDialog criaLoteVacinaDialog;
    private ArrayList<Lote_Vacina> lote_vacinas;
    private static String ROOT = "BD";
    private static String CHILDREN_VACINA = "vacina";
    private static String CHILDREN_PROTOCOLO = "protocolo";
    private static String CHILDREN_VACINA_PROTOCOLO = "vacina_protocolo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        if (_modo.equals("UPD")) {
            preencheCampos(_vacina);

            setAdapter();

            botaoInclusao();
        }
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_vacina"))
            _vacina =   getIntent().getParcelableExtra("_vacina");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_vacina_activity);

        ed_nome_vacina = findViewById(R.id.ed_nome_vacina);
        ed_desc_complementar_vacina = findViewById(R.id.ed_desc_complementar_vacina);
        tv_title_lote_vacina = findViewById(R.id.tv_title_lote_vacina);
        recyclerView = findViewById(R.id.recycler_view);
        tv_title_empty = findViewById(R.id.tv_title_empty);
        ib_addlote_vacina = findViewById(R.id.ib_addlote_vacina);
    }

    private void personalizaView()
    {
        ed_nome_vacina.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_vacina.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (_modo.equals("INS"))
        {
            tv_title_lote_vacina.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            tv_title_empty.setVisibility(View.INVISIBLE);
            ib_addlote_vacina.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_title_empty.setText("Não temos nenhum lote cadastrado para essa vacina");

            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            itemTouchHelperCallback = new LoteVacinaItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    private void preencheCampos(Vacina vacina)
    {
        ed_nome_vacina.setText(vacina.getNome_vacina());
        ed_desc_complementar_vacina.setText(vacina.getDesc_complementar_vacina());

        if(vacina.getLote_vacina() != null)
            lote_vacinas = vacina.getLote_vacina();
        else
            lote_vacinas = new ArrayList<>();
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_vacina.getText().length() == 0)
            ed_nome_vacina.setError("Informe o nome da vacina!");
        else
            validacao = true;

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN_VACINA);
        String key_vacina = databaseReference.push().getKey();

        Vacina vacina = new Vacina();
        vacina.setNome_vacina(ed_nome_vacina.getText().toString());
        vacina.setDesc_complementar_vacina(ed_desc_complementar_vacina.getText().toString());
        vacina.setLote_vacina(lote_vacinas);
        vacina.setKey_vacina(key_vacina);

        databaseReference.child(key_vacina).setValue(vacina).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void alteraCadastroFirebase(final String key_vacina)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_VACINA).child(key_vacina);

        final Vacina vacina = new Vacina();
        vacina.setNome_vacina(ed_nome_vacina.getText().toString());
        vacina.setDesc_complementar_vacina(ed_desc_complementar_vacina.getText().toString());
        vacina.setLote_vacina(lote_vacinas);
        vacina.setKey_vacina(key_vacina);

        databaseReference.setValue(vacina).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Atualiza a referência da vacina no Cadastro de Protocolo Sanitário
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            ProtocoloSanitario protocoloSanitario = data.getValue(ProtocoloSanitario.class);

                            for(int i = 0; i < protocoloSanitario.getVacina_protocolo().size(); i++) {
                                Vacina v = protocoloSanitario.getVacina_protocolo().get(i);

                                if (v.getKey_vacina().equals(key_vacina))
                                {
                                    String key_protocolo = protocoloSanitario.getKey_protocolo();
                                    atualizaVacinaProtocolo(key_protocolo, i, vacina);
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

    private void atualizaVacinaProtocolo(String key_protocolo, int position, Vacina vacina)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO).child(key_protocolo).child(CHILDREN_VACINA_PROTOCOLO).child(String.valueOf(position));

        databaseReference.setValue(vacina).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void setAdapter()
    {
        loteVacinasAdapter = new ConsultaLoteVacinaAdapter(lote_vacinas, this);
        recyclerView.setAdapter(loteVacinasAdapter);
        loteVacinasAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void botaoInclusao()
    {
        ib_addlote_vacina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criaLoteVacinaDialog = CriaLoteVacinaDialog.newInstance("INS", new Lote_Vacina());
                criaLoteVacinaDialog.show(getSupportFragmentManager(), "");
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
                    alteraCadastroFirebase(_vacina.getKey_vacina());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVacinaSelected(Lote_Vacina lote_vacina) {
        criaLoteVacinaDialog = CriaLoteVacinaDialog.newInstance("UPD", lote_vacina);
        criaLoteVacinaDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof ConsultaLoteVacinaAdapter.LoteVacinaViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir da vacina "  +  _vacina.getNome_vacina();
            message += " o lote " + loteVacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_vacina() + "?";

            builder.setTitle("Exclusão de lote da vacina");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.loteCacinaMovimentado(loteVacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getVacina_lote().getId_vacina(), loteVacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_vacina()) == false) {
                    String messageToast =   "Lote " + loteVacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNumero_lote_vacina();
                    messageToast += " foi excluído com sucesso!";

                    loteVacinasAdapter.removeItem(viewHolder.getAdapterPosition());

                    Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                    //Toast.makeText(getBaseContext(), "Lote desta vacina não pode ser deletado pois já possui movimentação!", Toast.LENGTH_LONG).show();
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
    public void criaLoteVacina(Lote_Vacina lote_vacina) {
        boolean achou = false;

        for(int i = 0; i < lote_vacinas.size(); i++) {
            if(lote_vacinas.get(i).getNumero_lote_vacina().equals(lote_vacina.getNumero_lote_vacina()))
            {
                achou = true;
                break;
            }
        }

        if(achou)
            Toast.makeText(getBaseContext(), "Este lote já foi adicionado a vacina!", Toast.LENGTH_LONG).show();
        else
        {
            lote_vacinas.add(lote_vacina);
            loteVacinasAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void alteraLoteVacina(Lote_Vacina lote_vacina) {
        for(int i = 0; i < lote_vacinas.size(); i++) {
            if(lote_vacinas.get(i).getNumero_lote_vacina().equals(lote_vacina.getNumero_lote_vacina()))
            {
                lote_vacinas.set(i, lote_vacina);
                loteVacinasAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}