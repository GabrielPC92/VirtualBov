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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import br.com.virtualbovapp.model.Estrategia;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.Lote;
import br.com.virtualbovapp.model.TipoEstrategia;

public class CadastroEstrategiaActivity extends AppCompatActivity {
    private EditText ed_nome_estrategia, ed_desc_complementar_estrategia;
    private Spinner sp_tipo_estrategia;
    private String _modo;
    private Estrategia _estrategia;
    private int tipo_estrategia;
    private ArrayAdapter<TipoEstrategia> tipoEstrategiaAdapter;
    private static String ROOT = "BD";
    private static String CHILDREN_ESTRATEGIA = "estrategia";
    private static String CHILDREN_LOTE = "lote";
    private static String CHILDREN_ESTRATEGIA_LOTE = "estrategia_lote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        setSpinnerTipoEstrategia();

        if (_modo.equals("UPD"))
        {
            preencheCampos(_estrategia);
        }

        selecaoSpinnerTipoEstrategia();
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_estrategia"))
            _estrategia =   getIntent().getParcelableExtra("_estrategia");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_estrategia_activity);

        ed_nome_estrategia = findViewById(R.id.ed_nome_estrategia);
        ed_desc_complementar_estrategia = findViewById(R.id.ed_desc_complementar_estrategia);
        sp_tipo_estrategia = findViewById(R.id.sp_tipo_estrategia);
    }

    private void personalizaView()
    {
        ed_nome_estrategia.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_estrategia.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void setSpinnerTipoEstrategia() {
        ArrayList<TipoEstrategia> tipoEstrategiaList = new ArrayList<>();

        tipoEstrategiaList.add(new TipoEstrategia(0, "Selecione"));
        tipoEstrategiaList.add(new TipoEstrategia(1, "Pasto"));
        tipoEstrategiaList.add(new TipoEstrategia(2, "Confinado"));
        tipoEstrategiaList.add(new TipoEstrategia(3, "TIP - Terminação Intensiva a Pasto"));
        tipoEstrategiaList.add(new TipoEstrategia(4, "Matriz"));
        tipoEstrategiaList.add(new TipoEstrategia(5, "Reprodutor"));
        tipoEstrategiaList.add(new TipoEstrategia(6, "Vaca de Leite"));
        tipoEstrategiaList.add(new TipoEstrategia(7, "Semi Confinamento"));
        tipoEstrategiaList.add(new TipoEstrategia(8, "Descarte"));

        tipoEstrategiaAdapter = new ArrayAdapter<TipoEstrategia>(this, android.R.layout.simple_spinner_dropdown_item, tipoEstrategiaList);
        sp_tipo_estrategia.setAdapter(tipoEstrategiaAdapter);
    }

    private void preencheCampos(Estrategia estrategia)
    {
        ed_nome_estrategia.setText(estrategia.getNome_estrategia());
        ed_desc_complementar_estrategia.setText(estrategia.getDesc_complementar_estrategia());

        for(int i = 0; i < tipoEstrategiaAdapter.getCount(); i++) {
            TipoEstrategia tipoEstrategia = tipoEstrategiaAdapter.getItem(i);

            if(tipoEstrategia.getId_tipo() == estrategia.getTipo_estrategia()) {
                tipo_estrategia = tipoEstrategia.getId_tipo();
                sp_tipo_estrategia.setSelection(i);
                break;
            }
        }
    }

    private void selecaoSpinnerTipoEstrategia()
    {
        sp_tipo_estrategia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoEstrategia tipoEstrategiaSelecionada = (TipoEstrategia) parent.getItemAtPosition(position);
                tipo_estrategia = tipoEstrategiaSelecionada.getId_tipo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_estrategia.getText().length() == 0)
            ed_nome_estrategia.setError("Informe o nome da estratégia!");
        else
        {
            if (tipo_estrategia == 0)
                Toast.makeText(getBaseContext(), "Selecione o tipo de estratégia!", Toast.LENGTH_LONG).show();
            else
                validacao = true;
        }

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN_ESTRATEGIA);
        String key_estrategia = databaseReference.push().getKey();

        Estrategia estrategia = new Estrategia();
        estrategia.setNome_estrategia(ed_nome_estrategia.getText().toString());
        estrategia.setDesc_complementar_estrategia(ed_desc_complementar_estrategia.getText().toString());
        estrategia.setTipo_estrategia(tipo_estrategia);
        estrategia.setKey_estrategia(key_estrategia);

        databaseReference.child(key_estrategia).setValue(estrategia).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void alteraCadastroFirebase(final String key_estrategia)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_ESTRATEGIA).child(key_estrategia);

        final Estrategia estrategia = new Estrategia();
        estrategia.setNome_estrategia(ed_nome_estrategia.getText().toString());
        estrategia.setDesc_complementar_estrategia(ed_desc_complementar_estrategia.getText().toString());
        estrategia.setTipo_estrategia(tipo_estrategia);
        estrategia.setKey_estrategia(key_estrategia);

        databaseReference.setValue(estrategia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Atualiza a referência da estratégia no Cadastro de Lotes
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOTE);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            Lote lote = data.getValue(Lote.class);

                            if (lote.getEstrategia_lote().getKey_estrategia().equals(key_estrategia))
                            {
                                String key_lote = lote.getKey_lote();
                                atualizaEstrategiaLote(key_lote, estrategia);
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

    private void atualizaEstrategiaLote(String key_lote, Estrategia estrategia)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOTE).child(key_lote).child(CHILDREN_ESTRATEGIA_LOTE);

        databaseReference.setValue(estrategia).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    alteraCadastroFirebase(_estrategia.getKey_estrategia());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

