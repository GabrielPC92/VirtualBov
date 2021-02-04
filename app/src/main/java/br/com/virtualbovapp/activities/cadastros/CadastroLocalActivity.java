package br.com.virtualbovapp.activities.cadastros;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.Local;
import br.com.virtualbovapp.model.Lote;

public class CadastroLocalActivity extends AppCompatActivity {
    private EditText ed_nome_local, ed_desc_complementar_local, ed_capacidade_local;
    private String _modo;
    private Local _local;
    private static String ROOT = "BD";
    private static String CHILDREN_LOCAL = "local";
    private static String CHILDREN_LOTE = "lote";
    private static String CHILDREN_LOCAL_LOTE = "local_lote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        if (_modo.equals("UPD"))
        {
            preencheCampos(_local);
        }
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_local"))
            _local =   getIntent().getParcelableExtra("_local");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_local_activity);

        ed_nome_local = findViewById(R.id.ed_nome_local);
        ed_desc_complementar_local = findViewById(R.id.ed_desc_complementar_local);
        ed_capacidade_local = findViewById(R.id.ed_capacidade_local);
    }

    private void personalizaView()
    {
        ed_nome_local.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_desc_complementar_local.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void preencheCampos(Local local)
    {
        ed_nome_local.setText(local.getNome_local());
        ed_desc_complementar_local.setText(local.getDesc_complementar_local());
        ed_capacidade_local.setText(String.valueOf(local.getCapacidade_local()));
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if(ed_nome_local.getText().length() == 0)
            ed_nome_local.setError("Informe o nome do local!");
        else
        {
            if (ed_capacidade_local.getText().length() == 0)
                ed_capacidade_local.setError("Informe a capacidade do local!");
            else
            {
                if (Integer.parseInt(ed_capacidade_local.getText().toString()) == 0)
                    ed_capacidade_local.setError("Informe a capacidade do local!");
                else
                    validacao = true;
            }
        }

        return validacao;
    }

    private void criaCadastroFirebase()
    {
        FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOCAL);
        String key_local = databaseReference.push().getKey();

        Local local = new Local();
        local.setNome_local(ed_nome_local.getText().toString());
        local.setDesc_complementar_local(ed_desc_complementar_local.getText().toString());
        local.setCapacidade_local(Integer.parseInt(ed_capacidade_local.getText().toString()));
        local.setKey_local(key_local);

        databaseReference.child(key_local).setValue(local).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void alteraCadastroFirebase(final String key_local)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOCAL).child(key_local);

        final Local local = new Local();
        local.setNome_local(ed_nome_local.getText().toString());
        local.setDesc_complementar_local(ed_desc_complementar_local.getText().toString());
        local.setCapacidade_local(Integer.parseInt(ed_capacidade_local.getText().toString()));
        local.setKey_local(key_local);

        databaseReference.setValue(local).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Atualiza a referência do local no Cadastro de Lotes
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOTE);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            Lote lote = data.getValue(Lote.class);

                            if (lote.getLocal_lote().getKey_local().equals(key_local))
                            {
                                String key_lote = lote.getKey_lote();
                                atualizaLocalLote(key_lote, local);
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

    private void atualizaLocalLote(String key_lote, Local local)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN_LOTE).child(key_lote).child(CHILDREN_LOCAL_LOTE);

        databaseReference.setValue(local).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    alteraCadastroFirebase(_local.getKey_local());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}