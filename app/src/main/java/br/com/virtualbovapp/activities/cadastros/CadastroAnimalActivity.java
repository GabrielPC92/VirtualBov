package br.com.virtualbovapp.activities.cadastros;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.OrigemRaca;
import br.com.virtualbovapp.model.Raca;

public class CadastroAnimalActivity extends AppCompatActivity {
    private EditText ed_nome_raca;
    private Spinner sp_origem_raca;
    private String _modo;
    private Raca _raca;
    private int origem_raca;
    private ArrayAdapter<OrigemRaca> origemRacaAdapter;
    private static String ROOT = "BD";
    private static String CHILDREN = "raca";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //carregaParametros();

        carregaView();

        /*personalizaView();

        setSpinnerOrigemRaca();

        if (_modo.equals("UPD"))
            preencheCampos(_raca);

        selecaoSpinnerOrigemRaca();*/
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_raca"))
            _raca =   getIntent().getParcelableExtra("_raca");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_animal_activity);

        /*ed_nome_raca = findViewById(R.id.ed_nome_raca);
        sp_origem_raca = findViewById(R.id.sp_origem_raca);*/
    }

    private void personalizaView()
    {
        ed_nome_raca.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (_modo.equals("UPD")) {
            if (_raca.isPadrao_raca()) {
                ed_nome_raca.setEnabled(false);
                sp_origem_raca.setEnabled(false);
            }
        }
    }

    private void setSpinnerOrigemRaca() {
        ArrayList<OrigemRaca> origemRacaList = new ArrayList<>();

        origemRacaList.add(new OrigemRaca(0, "Selecione"));
        origemRacaList.add(new OrigemRaca(1, "Puro de Origem"));
        origemRacaList.add(new OrigemRaca(2, "Cruzamento Industrial"));
        origemRacaList.add(new OrigemRaca(3, "Sem Raça Definida"));

        origemRacaAdapter = new ArrayAdapter<OrigemRaca>(this, android.R.layout.simple_spinner_dropdown_item, origemRacaList);
        sp_origem_raca.setAdapter(origemRacaAdapter);
    }

    private void preencheCampos(Raca r)
    {
        ed_nome_raca.setText(r.getNome_raca());

        for(int i = 0; i < origemRacaAdapter.getCount(); i++) {
            OrigemRaca origemRaca = origemRacaAdapter.getItem(i);

            if(origemRaca.getId_origem() == r.getOrigem_raca()) {
                origem_raca = origemRaca.getId_origem();
                sp_origem_raca.setSelection(i);
                break;
            }
        }
    }

    private void selecaoSpinnerOrigemRaca()
    {
        sp_origem_raca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrigemRaca origemRacaSelecionada = (OrigemRaca) parent.getItemAtPosition(position);
                origem_raca = origemRacaSelecionada.getId_origem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if (ed_nome_raca.getText().length() == 0)
            ed_nome_raca.setError("Informe o nome da raça!");
        else
        {
            if (origem_raca == 0)
                Toast.makeText(getBaseContext(), "Selecione a origem da raça!", Toast.LENGTH_LONG).show();
            else
                validacao = true;
        }

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        try {
            FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
            DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN);
            String key_raca = databaseReference.push().getKey();

            Raca raca = new Raca();
            raca.setNome_raca(ed_nome_raca.getText().toString());
            raca.setOrigem_raca(origem_raca);
            raca.setKey_raca(key_raca);
            raca.setPadrao_raca(false);

            databaseReference.child(key_raca).setValue(raca);

            finish();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
        }
    }

    private void alteraCadastroFirebase(String key_raca)
    {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN).child(key_raca);

            Raca raca = new Raca();
            raca.setNome_raca(ed_nome_raca.getText().toString());
            raca.setOrigem_raca(origem_raca);
            raca.setKey_raca(key_raca);
            raca.setPadrao_raca(false);

            databaseReference.setValue(raca);

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
                    alteraCadastroFirebase(_raca.getKey_raca());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}