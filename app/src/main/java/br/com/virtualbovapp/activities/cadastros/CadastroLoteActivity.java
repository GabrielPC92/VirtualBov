package br.com.virtualbovapp.activities.cadastros;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import br.com.virtualbovapp.model.Estrategia;
import br.com.virtualbovapp.model.Lote;
import br.com.virtualbovapp.dialogfragments.PesquisaEstrategiaDialogFragment;
import br.com.virtualbovapp.dialogfragments.PesquisaLocalDialogFragment;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.TipoAnimaisLote;
import br.com.virtualbovapp.model.TipoPeriodoLote;
import br.com.virtualbovapp.model.Local;

public class CadastroLoteActivity extends AppCompatActivity implements PesquisaEstrategiaDialogFragment.EstrategiasFragmentListener, PesquisaLocalDialogFragment.LocaisFragmentListener {
    private EditText ed_nome_lote, ed_desc_complementar_lote, ed_periodo_permanencia_lote, ed_estrategia_lote, ed_local_lote;
    private Spinner sp_tipo_animais_lote, sp_tipo_periodo_lote;
    private ImageButton ib_pesquisa_estrategia_lote, ib_pesquisa_local_lote;
    private String _modo;
    private int tipo_animais_lote, tipo_periodo_lote;
    private ArrayAdapter<TipoAnimaisLote> tipoAnimaisLoteAdapter;
    private ArrayAdapter<TipoPeriodoLote> tipoPeriodoLoteAdapter;
    private Local localLote;
    private Estrategia estrategiaLote;
    private PesquisaLocalDialogFragment pesquisaLocalDialogFragment;
    private PesquisaEstrategiaDialogFragment pesquisaEstrategiaDialogFragment;
    private Lote _lote;
    private static String ROOT = "BD";
    private static String CHILDREN = "lote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        setSpinnerTipoAnimais();

        setSpinnerTipoPeriodoLote();

        if (_modo.equals("UPD"))
        {
            preencheCampos(_lote);
        }

        selecaoSpinnerTipoAnimais();

        selecaoSpinnerTipoPeriodoLote();

        botoesPesquisa();
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_lote"))
            _lote =   getIntent().getParcelableExtra("_lote");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_lote_activity);

        ed_nome_lote = findViewById(R.id.ed_nome_lote);
        ed_desc_complementar_lote = findViewById(R.id.ed_desc_complementar_lote);
        sp_tipo_animais_lote = findViewById(R.id.sp_tipo_animais_lote);
        ed_periodo_permanencia_lote = findViewById(R.id.ed_periodo_permanencia_lote);
        sp_tipo_periodo_lote = findViewById(R.id.sp_tipo_periodo_lote);
        ed_estrategia_lote = findViewById(R.id.ed_estrategia_lote);
        ed_local_lote = findViewById(R.id.ed_local_lote);
        ib_pesquisa_estrategia_lote = findViewById(R.id.ib_pesquisa_estrategia_lote);
        ib_pesquisa_local_lote = findViewById(R.id.ib_pesquisa_local_lote);
    }

    private void personalizaView()
    {
        ed_estrategia_lote.setEnabled(false);
        ed_local_lote.setEnabled(false);

        ed_nome_lote.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_lote.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void setSpinnerTipoAnimais() {
        ArrayList<TipoAnimaisLote> tipoAnimaisLoteList = new ArrayList<>();

        tipoAnimaisLoteList.add(new TipoAnimaisLote(0, "Selecione"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(1, "Macho Inteiro"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(2, "Macho Castrado"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(3, "Fêmea"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(4, "Macho Inteiro, Macho Castrado e Fêmea"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(5, "Macho Inteiro e Macho Castrado"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(6, "Macho Inteiro e Fêmea"));
        tipoAnimaisLoteList.add(new TipoAnimaisLote(7, "Macho Castrado e Fêmea"));

        tipoAnimaisLoteAdapter = new ArrayAdapter<TipoAnimaisLote>(this, android.R.layout.simple_spinner_dropdown_item, tipoAnimaisLoteList);
        sp_tipo_animais_lote.setAdapter(tipoAnimaisLoteAdapter);
    }

    private void setSpinnerTipoPeriodoLote() {
        ArrayList<TipoPeriodoLote> tipoPeriodoLoteList = new ArrayList<>();

        tipoPeriodoLoteList.add(new TipoPeriodoLote(1, "Dia(s)"));
        tipoPeriodoLoteList.add(new TipoPeriodoLote(2, "Mês(es)"));

        tipoPeriodoLoteAdapter = new ArrayAdapter<TipoPeriodoLote>(this, android.R.layout.simple_spinner_dropdown_item, tipoPeriodoLoteList);
        sp_tipo_periodo_lote.setAdapter(tipoPeriodoLoteAdapter);
    }

    private void preencheCampos(Lote lote)
    {
        ed_nome_lote.setText(lote.getNome_lote());
        ed_desc_complementar_lote.setText(lote.getDesc_complementar_lote());

        for(int i = 0; i < tipoAnimaisLoteAdapter.getCount(); i++) {
            TipoAnimaisLote tipoAnimaisLote = tipoAnimaisLoteAdapter.getItem(i);

            if(tipoAnimaisLote.getId_tipo() == lote.getTipo_animais_lote()) {
                tipo_animais_lote = tipoAnimaisLote.getId_tipo();
                sp_tipo_animais_lote.setSelection(i);
                break;
            }
        }

        ed_periodo_permanencia_lote.setText(String.valueOf(lote.getPeriodo_permanencia_lote()));

        for(int i = 0; i < tipoPeriodoLoteAdapter.getCount(); i++) {
            TipoPeriodoLote tipoPeriodoLote = tipoPeriodoLoteAdapter.getItem(i);

            if(tipoPeriodoLote.getId_tipo() == lote.getTipo_periodo_lote()) {
                tipo_periodo_lote = tipoPeriodoLote.getId_tipo();
                sp_tipo_periodo_lote.setSelection(i);
                break;
            }
        }

        estrategiaLote = lote.getEstrategia_lote();
        ed_estrategia_lote.setText(estrategiaLote.getNome_estrategia());

        localLote = lote.getLocal_lote();
        ed_local_lote.setText(localLote.getNome_local());
    }

    private void selecaoSpinnerTipoAnimais() {
        sp_tipo_animais_lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoAnimaisLote tipoAnimaisLoteSelecionado = (TipoAnimaisLote) parent.getItemAtPosition(position);
                tipo_animais_lote = tipoAnimaisLoteSelecionado.getId_tipo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selecaoSpinnerTipoPeriodoLote() {
        sp_tipo_periodo_lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoPeriodoLote tipoPeriodoLoteSelecionado = (TipoPeriodoLote) parent.getItemAtPosition(position);
                tipo_periodo_lote = tipoPeriodoLoteSelecionado.getId_tipo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void botoesPesquisa()
    {
        ib_pesquisa_local_lote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaLocalDialogFragment = PesquisaLocalDialogFragment.newInstance();
                pesquisaLocalDialogFragment.show(getSupportFragmentManager(), "");
            }
        });

        ib_pesquisa_estrategia_lote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaEstrategiaDialogFragment = PesquisaEstrategiaDialogFragment.newInstance();
                pesquisaEstrategiaDialogFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_lote.getText().length() == 0)
            ed_nome_lote.setError("Informe o nome do lote!");
        else
        {
            if (tipo_animais_lote == 0)
                Toast.makeText(getBaseContext(), "Selecione o tipo de animais do lote!", Toast.LENGTH_LONG).show();
            else
            {
                if (ed_periodo_permanencia_lote.getText().length() == 0)
                    ed_periodo_permanencia_lote.setError("Informe o período de permanência dos animais no lote!");
                else
                {
                    if (Integer.parseInt(ed_periodo_permanencia_lote.getText().toString()) == 0)
                        ed_periodo_permanencia_lote.setError("Informe o período de permanência dos animais no lote!");
                    else
                    {
                        if (ed_estrategia_lote.getText().length() == 0)
                            ed_estrategia_lote.setError("Informe a estratégia do lote!");
                        else
                        {
                            if (ed_local_lote.getText().length() == 0)
                                ed_local_lote.setError("Informe o local do lote!");
                            else
                                validacao = true;
                        }
                    }
                }
            }
        }

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        try {
            FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
            DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN);
            String key_lote = databaseReference.push().getKey();

            Lote lote = new Lote();
            lote.setNome_lote(ed_nome_lote.getText().toString());
            lote.setDesc_complementar_lote(ed_desc_complementar_lote.getText().toString());
            lote.setTipo_animais_lote(tipo_animais_lote);
            lote.setPeriodo_permanencia_lote(Integer.parseInt(ed_periodo_permanencia_lote.getText().toString()));
            lote.setTipo_periodo_lote(tipo_periodo_lote);
            lote.setEstrategia_lote(estrategiaLote);
            lote.setLocal_lote(localLote);
            lote.setKey_lote(key_lote);

            databaseReference.child(key_lote).setValue(lote);

            finish();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
        }
    }

    private void alteraCadastroFirebase(String key_lote)
    {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN).child(key_lote);

            Lote lote = new Lote();
            lote.setNome_lote(ed_nome_lote.getText().toString());
            lote.setDesc_complementar_lote(ed_desc_complementar_lote.getText().toString());
            lote.setTipo_animais_lote(tipo_animais_lote);
            lote.setPeriodo_permanencia_lote(Integer.parseInt(ed_periodo_permanencia_lote.getText().toString()));
            lote.setTipo_periodo_lote(tipo_periodo_lote);
            lote.setEstrategia_lote(estrategiaLote);
            lote.setLocal_lote(localLote);
            lote.setKey_lote(key_lote);

            databaseReference.setValue(lote);

            finish();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao atualizar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
        }
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
                    alteraCadastroFirebase(_lote.getKey_lote());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEstrategiaSelected(Estrategia estrategia) {
        pesquisaEstrategiaDialogFragment.dismiss();

        estrategiaLote = estrategia;
        ed_estrategia_lote.setText(estrategiaLote.getNome_estrategia());
    }

    @Override
    public void onLocalSelected(Local local) {
        pesquisaLocalDialogFragment.dismiss();

        localLote = local;
        ed_local_lote.setText(localLote.getNome_local());
    }
}