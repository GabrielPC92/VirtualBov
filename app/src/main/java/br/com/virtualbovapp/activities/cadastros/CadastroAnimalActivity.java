package br.com.virtualbovapp.activities.cadastros;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.dialogfragments.PesquisaLocalDialogFragment;
import br.com.virtualbovapp.dialogfragments.PesquisaRacaDialogFragment;
import br.com.virtualbovapp.model.Animal;
import br.com.virtualbovapp.model.Local;
import br.com.virtualbovapp.model.Raca;

public class CadastroAnimalActivity extends AppCompatActivity implements PesquisaRacaDialogFragment.RacasFragmentListener {
    private EditText ed_brinco_animal, ed_data_nascimento_animal, ed_sisbov_animal, ed_data_bnd_animal, ed_nome_pai_animal, ed_nome_mae_animal,
            ed_raca_animal, ed_pesagem_inicial_animal, ed_data_desmame_animal, ed_data_morte_animal;
    private RadioButton rb_seleciotion;
    private RadioGroup rg_sexo;
    private ImageButton ib_data_nascimento_animal, ib_data_bnd_animal, ib_data_desmame_animal, ib_data_morte_animal, ib_pesquisa_raca_animal;
    private TextView tv_title_sexo_animal;
    private String _modo;
    private Animal _animal;
    private Raca racaAnimal;
    private PesquisaRacaDialogFragment pesquisaRacaDialogFragment;
    private int mYear, mMonth, mDay;
    private static final String ROOT = "BD";
    private static final String CHILDREN = "animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaParametros();

        carregaView();

        personalizaView();

        if (_modo.equals("UPD"))
            preencheCampos(_animal);

        selecaoDatas();

        botaoPesquisa();
    }

    private void carregaParametros()
    {
        if(getIntent().hasExtra("_modo"))
            _modo =   getIntent().getStringExtra("_modo");

        if(getIntent().hasExtra("_animal"))
            _animal =   getIntent().getParcelableExtra("_animal");
    }

    private void carregaView()
    {
        setContentView(R.layout.cadastro_animal_activity);

        ed_brinco_animal = findViewById(R.id.ed_brinco_animal);
        ed_data_nascimento_animal = findViewById(R.id.ed_data_nascimento_animal);
        ib_data_nascimento_animal = findViewById(R.id.ib_data_nascimento_animal);
        rg_sexo = findViewById(R.id.rg_sexo);
        tv_title_sexo_animal = findViewById(R.id.tv_title_sexo_animal);
        ed_sisbov_animal = findViewById(R.id.ed_sisbov_animal);
        ed_data_bnd_animal = findViewById(R.id.ed_data_bnd_animal);
        ib_data_bnd_animal = findViewById(R.id.ib_data_bnd_animal);
        ed_nome_pai_animal = findViewById(R.id.ed_nome_pai_animal);
        ed_nome_mae_animal = findViewById(R.id.ed_nome_mae_animal);
        ed_raca_animal = findViewById(R.id.ed_raca_animal);
        ib_pesquisa_raca_animal = findViewById(R.id.ib_pesquisa_raca_animal);
        ed_pesagem_inicial_animal = findViewById(R.id.ed_pesagem_inicial_animal);
        ed_data_desmame_animal = findViewById(R.id.ed_data_desmame_animal);
        ib_data_desmame_animal = findViewById(R.id.ib_data_desmame_animal);
        ed_data_morte_animal = findViewById(R.id.ed_data_morte_animal);
        ib_data_morte_animal = findViewById(R.id.ib_data_morte_animal);
    }

    private void personalizaView()
    {
        ed_brinco_animal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_sisbov_animal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_nome_pai_animal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ed_nome_mae_animal.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        ed_data_nascimento_animal.setEnabled(false);
        ed_data_bnd_animal.setEnabled(false);
        ed_raca_animal.setEnabled(false);
        ed_data_morte_animal.setEnabled(false);
        ed_data_desmame_animal.setEnabled(false);
        
        rg_sexo.clearCheck();

        if(_modo.equals("UPD"))
            ed_brinco_animal.setEnabled(false);
    }

    private void preencheCampos(Animal animal)
    {
        ed_brinco_animal.setText(animal.getBrinco_animal());
        ed_data_nascimento_animal.setText(animal.getData_nascimento_animal());

        switch (animal.getSexo_animal()){
            case "Masculino":
                rg_sexo.check(R.id.rb_sexo_masculino);
                break;

            case "Feminino":
                rg_sexo.check(R.id.rb_sexo_feminino);
                break;
        }

        ed_sisbov_animal.setText(animal.getSisbov_animal());
        ed_data_bnd_animal.setText(animal.getData_bnd_animal());
        ed_nome_pai_animal.setText(animal.getNome_pai_animal());
        ed_nome_mae_animal.setText(animal.getNome_mae_animal());

        racaAnimal = animal.getRaca_animal();
        ed_raca_animal.setText(racaAnimal.getNome_raca());

        ed_pesagem_inicial_animal.setText(animal.getPesagem_animal());
        ed_data_desmame_animal.setText(animal.getDesmame_animal());
        ed_data_morte_animal.setText(animal.getMorte_animal());
    }

    private void selecaoDatas() {
        ib_data_nascimento_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_data_nascimento_animal.getText().length()   ==  0){
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    mDay = Integer.parseInt(ed_data_nascimento_animal.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_nascimento_animal.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_nascimento_animal.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroAnimalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes, dataCompleta;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        dataCompleta = dia + "/" + mes + "/" + year;
                        ed_data_nascimento_animal.setText(dataCompleta);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        ib_data_bnd_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_data_bnd_animal.getText().length()   ==  0){
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    mDay = Integer.parseInt(ed_data_bnd_animal.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_bnd_animal.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_bnd_animal.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroAnimalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes, dataCompleta;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        dataCompleta = dia + "/" + mes + "/" + year;
                        ed_data_bnd_animal.setText(dataCompleta);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        ib_data_desmame_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_data_desmame_animal.getText().length()   ==  0){
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    mDay = Integer.parseInt(ed_data_desmame_animal.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_desmame_animal.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_desmame_animal.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroAnimalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes, dataCompleta;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        dataCompleta = dia + "/" + mes + "/" + year;
                        ed_data_desmame_animal.setText(dataCompleta);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        ib_data_morte_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_data_morte_animal.getText().length()   ==  0){
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    mDay = Integer.parseInt(ed_data_morte_animal.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_morte_animal.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_morte_animal.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroAnimalActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes, dataCompleta;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        dataCompleta = dia + "/" + mes + "/" + year;
                        ed_data_morte_animal.setText(dataCompleta);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = true;

        if (ed_brinco_animal.getText().length() == 0) {
            ed_brinco_animal.setError("Informe o brinco do animal!");
            validacao = false;
        }

        if (ed_data_nascimento_animal.getText().length() > 0 && validacao == true) {
            String strDataOcorrencia;
            String strDataHoje;
            Date dataOcorrencia;
            Date dataHoje;
            Calendar c = Calendar.getInstance();

            strDataOcorrencia = ed_data_nascimento_animal.getText().toString();
            strDataHoje = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

            dataOcorrencia = stringToDate(strDataOcorrencia);
            dataHoje = stringToDate(strDataHoje);

            if (dataOcorrencia.compareTo(dataHoje) > 0){
                ed_data_nascimento_animal.setError("Data de nascimento deve ser menor ou igual a data de hoje!");
                validacao = false;
            }
        }

        if (rg_sexo.getCheckedRadioButtonId() == -1 && validacao == true) {
            tv_title_sexo_animal.setError("Informe o sexo do animal!");
            validacao = false;
        }

        if (ed_data_bnd_animal.getText().length() > 0 && validacao == true) {
            String strDataOcorrencia;
            String strDataHoje;
            Date dataOcorrencia;
            Date dataHoje;
            Calendar c = Calendar.getInstance();

            strDataOcorrencia = ed_data_bnd_animal.getText().toString();
            strDataHoje = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

            dataOcorrencia = stringToDate(strDataOcorrencia);
            dataHoje = stringToDate(strDataHoje);

            if (dataOcorrencia.compareTo(dataHoje) > 0){
                ed_data_bnd_animal.setError("Data do BND deve ser menor ou igual a data de hoje!");
                validacao = false;
            }
        }

        if (ed_raca_animal.getText().length() == 0) {
            ed_raca_animal.setError("Selecione a raça do animal!");
            validacao = false;
        }

        if (ed_data_desmame_animal.getText().length() > 0 && validacao == true) {
            String strDataOcorrencia;
            String strDataHoje;
            Date dataOcorrencia;
            Date dataHoje;
            Calendar c = Calendar.getInstance();

            strDataOcorrencia = ed_data_desmame_animal.getText().toString();
            strDataHoje = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

            dataOcorrencia = stringToDate(strDataOcorrencia);
            dataHoje = stringToDate(strDataHoje);

            if (dataOcorrencia.compareTo(dataHoje) > 0){
                ed_data_desmame_animal.setError("Data de desmame deve ser menor ou igual a data de hoje!");
                validacao = false;
            }
        }

        if (ed_data_morte_animal.getText().length() > 0 && validacao == true) {
            String strDataOcorrencia;
            String strDataHoje;
            Date dataOcorrencia;
            Date dataHoje;
            Calendar c = Calendar.getInstance();

            strDataOcorrencia = ed_data_morte_animal.getText().toString();
            strDataHoje = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

            dataOcorrencia = stringToDate(strDataOcorrencia);
            dataHoje = stringToDate(strDataHoje);

            if (dataOcorrencia.compareTo(dataHoje) > 0){
                ed_data_morte_animal.setError("Data da morte deve ser menor ou igual a data de hoje!");
                validacao = false;
            }
        }

        return validacao;
    }

    private void selecionaSexo()
    {
        rb_seleciotion = rg_sexo.findViewById(rg_sexo.getCheckedRadioButtonId());
    }

    private void criaCadastroFirebase()
    {
        try {
            FirebaseDatabase firebaseDatabase   =   FirebaseDatabase.getInstance();
            DatabaseReference databaseReference =   firebaseDatabase.getReference().child(ROOT).child(CHILDREN);
            String key_animal = databaseReference.push().getKey();

            Animal animal = new Animal();
            animal.setBrinco_animal(ed_brinco_animal.getText().toString());
            animal.setData_nascimento_animal(ed_data_nascimento_animal.getText().toString());
            animal.setSexo_animal(rb_seleciotion.getText().toString());
            animal.setSisbov_animal(ed_sisbov_animal.getText().toString());
            animal.setData_bnd_animal(ed_data_bnd_animal.getText().toString());
            animal.setNome_pai_animal(ed_nome_pai_animal.getText().toString());
            animal.setNome_mae_animal(ed_nome_mae_animal.getText().toString());
            animal.setRaca_animal(racaAnimal);
            animal.setPesagem_animal(ed_pesagem_inicial_animal.getText().toString());
            animal.setDesmame_animal(ed_data_desmame_animal.getText().toString());
            animal.setMorte_animal(ed_data_morte_animal.getText().toString());
            animal.setKey_animal(key_animal);

            databaseReference.child(key_animal).setValue(animal);

            finish();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao salvar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
        }
    }

    private void alteraCadastroFirebase(String key_animal)
    {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child(ROOT).child(CHILDREN).child(key_animal);

            Animal animal = new Animal();
            animal.setBrinco_animal(ed_brinco_animal.getText().toString());
            animal.setData_nascimento_animal(ed_data_nascimento_animal.getText().toString());
            animal.setSexo_animal(rb_seleciotion.getText().toString());
            animal.setSisbov_animal(ed_sisbov_animal.getText().toString());
            animal.setData_bnd_animal(ed_data_bnd_animal.getText().toString());
            animal.setNome_pai_animal(ed_nome_pai_animal.getText().toString());
            animal.setNome_mae_animal(ed_nome_mae_animal.getText().toString());
            animal.setRaca_animal(racaAnimal);
            animal.setPesagem_animal(ed_pesagem_inicial_animal.getText().toString());
            animal.setDesmame_animal(ed_data_desmame_animal.getText().toString());
            animal.setMorte_animal(ed_data_morte_animal.getText().toString());
            animal.setKey_animal(key_animal);

            databaseReference.setValue(animal);

            finish();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao atualizar o cadastro. Tente novamente!", Toast.LENGTH_LONG).show();
        }
    }

    public Date stringToDate(String dataStr)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try
        {
            return formatter.parse(dataStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
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
                selecionaSexo();

                if (_modo.equals("INS"))
                    criaCadastroFirebase();
                else
                    alteraCadastroFirebase(_animal.getKey_animal());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void botaoPesquisa()
    {
        ib_pesquisa_raca_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaRacaDialogFragment = PesquisaRacaDialogFragment.newInstance();
                pesquisaRacaDialogFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public void onRacaSelected(Raca raca) {
        pesquisaRacaDialogFragment.dismiss();

        racaAnimal = raca;
        ed_raca_animal.setText(racaAnimal.getNome_raca());
    }
}