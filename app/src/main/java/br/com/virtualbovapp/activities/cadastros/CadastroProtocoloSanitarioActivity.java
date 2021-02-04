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
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import br.com.virtualbovapp.adapters.ConsultaMedicamentoAdapter;
import br.com.virtualbovapp.adapters.ConsultaVacinaAdapter;
import br.com.virtualbovapp.helpers.MedicamentoItemTouchHelper;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.helpers.VacinaItemTouchHelper;
import br.com.virtualbovapp.model.Vacina;
import br.com.virtualbovapp.model.Medicamento;
import br.com.virtualbovapp.dialogfragments.PesquisaMedicamentoDialogFragment;
import br.com.virtualbovapp.dialogfragments.PesquisaVacinaDialogFragment;
import br.com.virtualbovapp.model.ProtocoloSanitario;
import br.com.virtualbovapp.R;

public class CadastroProtocoloSanitarioActivity extends AppCompatActivity implements 
        PesquisaVacinaDialogFragment.VacinasProtocoloFragmentListener,
        PesquisaMedicamentoDialogFragment.MedicamentosProtocoloFragmentListener,
        ConsultaVacinaAdapter.VacinasAdapterListener,
        ConsultaMedicamentoAdapter.MedicamentosAdapterListener, 
        MedicamentoItemTouchHelper.RecyclerItemTouchHelperListener, 
        VacinaItemTouchHelper.RecyclerItemTouchHelperListener {
    
    private EditText ed_nome_protocolo, ed_desc_complementar_protocolo, ed_idade_protocolo, ed_outros_procedimentos_protocolo;
    private ImageButton ib_pesquisa_vacina_protocolo, ib_pesquisa_medicamento_protocolo;
    private RecyclerView recyclerViewVacinas;
    private RecyclerView recyclerViewMedicamentos;
    private TextView tv_title_empty_vacinas, tv_title_vacina_protocolo;
    private TextView tv_title_empty_medicamentos, tv_title_medicamento_protocolo;
    private RelativeLayout relativeLayout;
    private String _modo;
    private AlertDialog alerta;
    private ProtocoloSanitario protocoloSanitario;
    private ProtocoloSanitario _protocoloSanitario;
    private ConsultaMedicamentoAdapter medicamentosAdapter;
    private ConsultaVacinaAdapter vacinasAdapter;
    private ArrayList<Vacina> vacinas;
    private ArrayList<Medicamento> medicamentos;
    private PesquisaVacinaDialogFragment pesquisaVacinaDialogFragment;
    private PesquisaMedicamentoDialogFragment pesquisaMedicamentoDialogFragment;
    private LinearLayoutManager linearLayoutManagerVacina, linearLayoutManagerMedicamento;
    private ItemTouchHelper itemTouchHelperVacina, itemTouchHelperMedicamento;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallbackVacina, itemTouchHelperCallbackMedicamento;
    private static String ROOT = "BD";
    private static String CHILDREN_PROTOCOLO = "protocolo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        if (_modo.equals("UPD")) {
            preencheCampos(_protocoloSanitario);

            setAdapter();

            botoesInclusao();
        }
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_protocoloSanitario"))
            _protocoloSanitario =   getIntent().getParcelableExtra("_protocoloSanitario");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_protocolo_sanitario_activity);

        ed_nome_protocolo = findViewById(R.id.ed_nome_protocolo);
        ed_desc_complementar_protocolo = findViewById(R.id.ed_desc_complementar_protocolo);
        ed_idade_protocolo = findViewById(R.id.ed_idade_protocolo);
        ed_outros_procedimentos_protocolo = findViewById(R.id.ed_outros_procedimentos_protocolo);
        tv_title_vacina_protocolo = findViewById(R.id.tv_title_vacina_protocolo);
        tv_title_medicamento_protocolo = findViewById(R.id.tv_title_medicamento_protocolo);
        ib_pesquisa_vacina_protocolo = findViewById(R.id.ib_pesquisa_vacina_protocolo);
        ib_pesquisa_medicamento_protocolo = findViewById(R.id.ib_pesquisa_medicamento_protocolo);
        
        relativeLayout = findViewById(R.id.layout_content_vacinas);
        recyclerViewVacinas = relativeLayout.findViewById(R.id.recycler_view);
        tv_title_empty_vacinas = relativeLayout.findViewById(R.id.tv_title_empty);

        relativeLayout = findViewById(R.id.layout_content_medicamentos);
        recyclerViewMedicamentos = relativeLayout.findViewById(R.id.recycler_view);
        tv_title_empty_medicamentos = relativeLayout.findViewById(R.id.tv_title_empty);
    }

    private void personalizaView()
    {
        ed_nome_protocolo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_protocolo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_outros_procedimentos_protocolo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (_modo.equals("INS"))
        {
            tv_title_vacina_protocolo.setVisibility(View.INVISIBLE);
            recyclerViewVacinas.setVisibility(View.INVISIBLE);
            tv_title_empty_vacinas.setVisibility(View.INVISIBLE);
            ib_pesquisa_vacina_protocolo.setVisibility(View.INVISIBLE);

            tv_title_medicamento_protocolo.setVisibility(View.INVISIBLE);
            recyclerViewMedicamentos.setVisibility(View.INVISIBLE);
            tv_title_empty_medicamentos.setVisibility(View.INVISIBLE);
            ib_pesquisa_medicamento_protocolo.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_title_empty_vacinas.setText("Não temos nenhuma vacina cadastrada para esse protocolo");
            tv_title_empty_medicamentos.setText("Não temos nenhum medicamento cadastrado para esse protocolo");

            linearLayoutManagerVacina = new LinearLayoutManager(this);
            recyclerViewVacinas.setLayoutManager(linearLayoutManagerVacina);
            recyclerViewVacinas.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            itemTouchHelperCallbackVacina = new VacinaItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            itemTouchHelperVacina = new ItemTouchHelper(itemTouchHelperCallbackVacina);
            itemTouchHelperVacina.attachToRecyclerView(recyclerViewVacinas);

            linearLayoutManagerMedicamento = new LinearLayoutManager(this);
            recyclerViewMedicamentos.setLayoutManager(linearLayoutManagerMedicamento);
            recyclerViewMedicamentos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            itemTouchHelperCallbackMedicamento = new MedicamentoItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            itemTouchHelperMedicamento = new ItemTouchHelper(itemTouchHelperCallbackMedicamento);
            itemTouchHelperMedicamento.attachToRecyclerView(recyclerViewMedicamentos);
        }
    }

    private void preencheCampos(ProtocoloSanitario protocoloSanitario)
    {
        ed_nome_protocolo.setText(protocoloSanitario.getNome_protocolo());
        ed_desc_complementar_protocolo.setText(protocoloSanitario.getDesc_complementar_protocolo());
        ed_idade_protocolo.setText(String.valueOf(protocoloSanitario.getIdade_protocolo()));
        ed_outros_procedimentos_protocolo.setText(protocoloSanitario.getOutros_procedimentos_protocolo());

        if(protocoloSanitario.getVacina_protocolo() != null)
            vacinas = protocoloSanitario.getVacina_protocolo();
        else
            vacinas = new ArrayList<>();

        if(protocoloSanitario.getMedicamento_protocolo() != null)
            medicamentos = protocoloSanitario.getMedicamento_protocolo();
        else
            medicamentos = new ArrayList<>();
    }

    private void setAdapter()
    {
        vacinasAdapter = new ConsultaVacinaAdapter(vacinas, this);
        recyclerViewVacinas.setAdapter(vacinasAdapter);
        vacinasAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerViewVacinas, tv_title_empty_vacinas));

        medicamentosAdapter = new ConsultaMedicamentoAdapter(medicamentos, this);
        recyclerViewMedicamentos.setAdapter(medicamentosAdapter);
        medicamentosAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerViewMedicamentos, tv_title_empty_medicamentos));
    }

    private void botoesInclusao()
    {
        ib_pesquisa_vacina_protocolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaVacinaDialogFragment = PesquisaVacinaDialogFragment.newInstance();
                pesquisaVacinaDialogFragment.show(getSupportFragmentManager(), "");
            }
        });

        ib_pesquisa_medicamento_protocolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaMedicamentoDialogFragment = PesquisaMedicamentoDialogFragment.newInstance();
                pesquisaMedicamentoDialogFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_protocolo.getText().length() == 0)
            ed_nome_protocolo.setError("Informe o nome do protocolo!");
        else 
        {
            if (ed_idade_protocolo.getText().length() == 0)
                ed_idade_protocolo.setError("Informe para qual idade que esse protocolo deve ser aplicado!");
            else 
                validacao = true;
        }

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO);
        String key_protocolo = databaseReference.push().getKey();

        protocoloSanitario = new ProtocoloSanitario();
        protocoloSanitario.setNome_protocolo(ed_nome_protocolo.getText().toString());
        protocoloSanitario.setDesc_complementar_protocolo(ed_desc_complementar_protocolo.getText().toString());
        protocoloSanitario.setIdade_protocolo(Integer.parseInt(ed_idade_protocolo.getText().toString()));
        protocoloSanitario.setOutros_procedimentos_protocolo(ed_outros_procedimentos_protocolo.getText().toString());
        protocoloSanitario.setVacina_protocolo(vacinas);
        protocoloSanitario.setMedicamento_protocolo(medicamentos);
        protocoloSanitario.setKey_protocolo(key_protocolo);

        databaseReference.child(key_protocolo).setValue(protocoloSanitario).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void alteraCadastroFirebase(String key_protocolo)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_PROTOCOLO).child(key_protocolo);

        protocoloSanitario = new ProtocoloSanitario();
        protocoloSanitario.setNome_protocolo(ed_nome_protocolo.getText().toString());
        protocoloSanitario.setDesc_complementar_protocolo(ed_desc_complementar_protocolo.getText().toString());
        protocoloSanitario.setIdade_protocolo(Integer.parseInt(ed_idade_protocolo.getText().toString()));
        protocoloSanitario.setOutros_procedimentos_protocolo(ed_outros_procedimentos_protocolo.getText().toString());
        protocoloSanitario.setVacina_protocolo(vacinas);
        protocoloSanitario.setMedicamento_protocolo(medicamentos);
        protocoloSanitario.setKey_protocolo(key_protocolo);

        databaseReference.setValue(protocoloSanitario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Erro ao atualizar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
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
                    alteraCadastroFirebase(_protocoloSanitario.getKey_protocolo());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVacinaSelected(Vacina vacina) {
        /*do not implement*/
    }

    @Override
    public void onMedicamentoSelected(Medicamento medicamento) {
        /*do not implement*/
    }

    @Override
    public void onVacinaProtocoloSelected(Vacina vacina) {
        pesquisaVacinaDialogFragment.dismiss();

        boolean achou = false;

        for(int i = 0; i < vacinas.size(); i++) {
            if(vacinas.get(i).getKey_vacina().equals(vacina.getKey_vacina()))
            {
                achou = true;
                break;
            }
        }

        if(achou)
            Toast.makeText(getBaseContext(), "Vacina já adicionada ao protocolo!", Toast.LENGTH_LONG).show();
        else
        {
            vacinas.add(vacina);
            vacinasAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMedicamentoProtocoloSelected(Medicamento medicamento) {
        pesquisaMedicamentoDialogFragment.dismiss();

        boolean achou = false;

        for(int i = 0; i < medicamentos.size(); i++) {
            if(medicamentos.get(i).getKey_medicamento().equals(medicamento.getKey_medicamento()))
            {
                achou = true;
                break;
            }
        }

        if(achou)
            Toast.makeText(getBaseContext(), "Medicamento já adicionado ao protocolo!", Toast.LENGTH_LONG).show();
        else
        {
            medicamentos.add(medicamento);
            medicamentosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof ConsultaVacinaAdapter.VacinaViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir a vacina "  +  vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_vacina() + " do protocolo sanitário?";

            builder.setTitle("Exclusão de vacina do protocolo");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.vacinaMovimentada(vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getId_vacina()) == false) {
                    String messageToast =   "Vacina " + vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_vacina();
                    messageToast += " foi excluída do protocolo com sucesso!";

                    vacinasAdapter.removeItem(viewHolder.getAdapterPosition());

                    Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                    //Toast.makeText(getBaseContext(), "Vacina não pode ser deletada pois já possui movimentação!", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    itemTouchHelperVacina.attachToRecyclerView(null);
                    itemTouchHelperVacina.attachToRecyclerView(recyclerViewVacinas);

                    dialog.cancel();
                }
            });

            alerta = builder.create();
            alerta.show();
            alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    itemTouchHelperVacina.attachToRecyclerView(null);
                    itemTouchHelperVacina.attachToRecyclerView(recyclerViewVacinas);
                }
            });
        }

        if (viewHolder instanceof ConsultaMedicamentoAdapter.MedicamentoViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir o medicamento "  +  medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_medicamento() + " do protocolo sanitário?";

            builder.setTitle("Exclusão de medicamento do protocolo");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.medicamentoMovimentada(medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getId_medicamento()) == false) {
                    String messageToast =   "Medicamento " + medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_medicamento();
                    messageToast += " foi excluído do protocolo com sucesso!";

                    medicamentosAdapter.removeItem(viewHolder.getAdapterPosition());

                    Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                    //Toast.makeText(getBaseContext(), "Medicamento não pode ser deletada pois já possui movimentação!", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    itemTouchHelperMedicamento.attachToRecyclerView(null);
                    itemTouchHelperMedicamento.attachToRecyclerView(recyclerViewMedicamentos);

                    dialog.cancel();
                }
            });

            alerta = builder.create();
            alerta.show();
            alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    itemTouchHelperMedicamento.attachToRecyclerView(null);
                    itemTouchHelperMedicamento.attachToRecyclerView(recyclerViewMedicamentos);
                }
            });
        }
    }
}