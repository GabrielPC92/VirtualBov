package br.com.virtualbovapp.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.Lote_Vacina;

public class CriaLoteVacinaDialog extends DialogFragment {
    private CriaLoteVacina listenerC;
    private AlteraLoteVacina listenerA;
    private String _modo;
    private Lote_Vacina _lote_vacina;
    private EditText ed_numero_lote_vacina, ed_data_fabricacao_lote_vacina, ed_data_validade_lote_vacina;
    private ImageButton ib_data_fabricacao_lote_vacina, ib_data_validade_lote_vacina;
    private Button bt_confirmar;
    private int mYear, mMonth, mDay;

    public CriaLoteVacinaDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CriaLoteVacinaDialog newInstance(String modo, Lote_Vacina lote_vacina) {
        CriaLoteVacinaDialog frag = new CriaLoteVacinaDialog();
        Bundle args = new Bundle();
        args.putString("_modo", modo);
        args.putParcelable("_lote_vacina", lote_vacina);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cria_lote_vacina_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carregaParametros();

        carregaView(view);

        personalizaView();

        if (_modo.equals("UPD"))
            preencheCampos(_lote_vacina);

        selecaoDatas();

        confirmarCadastro();
    }

    public void carregaParametros()
    {
        _modo = getArguments().getString("_modo", "");
        _lote_vacina = getArguments().getParcelable("_lote_vacina");
    }

    private void carregaView(View view)
    {
        ed_numero_lote_vacina = view.findViewById(R.id.ed_numero_lote_vacina);
        ed_data_fabricacao_lote_vacina = view.findViewById(R.id.ed_data_fabricacao_lote_vacina);
        ed_data_validade_lote_vacina = view.findViewById(R.id.ed_data_validade_lote_vacina);
        ib_data_fabricacao_lote_vacina = view.findViewById(R.id.ib_data_fabricacao_lote_vacina);
        ib_data_validade_lote_vacina = view.findViewById(R.id.ib_data_validade_lote_vacina);
        bt_confirmar = view.findViewById(R.id.bt_confirmar);
    }

    private void personalizaView()
    {
        ed_data_fabricacao_lote_vacina.setEnabled(false);
        ed_data_validade_lote_vacina.setEnabled(false);
        ed_numero_lote_vacina.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (_modo.equals("INS"))
            getDialog().getWindow().setTitle("Criação de Lote");
        else
        {
            getDialog().getWindow().setTitle("Alteração de Lote");

            ed_numero_lote_vacina.setEnabled(false);
        }
    }

    private void preencheCampos(Lote_Vacina lote_vacina)
    {
        ed_numero_lote_vacina.setText(lote_vacina.getNumero_lote_vacina());
        ed_data_fabricacao_lote_vacina.setText(lote_vacina.getData_fabricacao_lote_vacina());
        ed_data_validade_lote_vacina.setText(lote_vacina.getData_validade_lote_vacina());
    }

    private void selecaoDatas()
    {
        ib_data_fabricacao_lote_vacina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_modo.equals("INS"))
                {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else
                {
                    mDay = Integer.parseInt(ed_data_fabricacao_lote_vacina.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_fabricacao_lote_vacina.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_fabricacao_lote_vacina.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        ed_data_fabricacao_lote_vacina.setText(dia + "/" + mes + "/" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        ib_data_validade_lote_vacina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_modo.equals("INS"))
                {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else
                {
                    mDay = Integer.parseInt(ed_data_validade_lote_vacina.getText().toString().substring(0, 2));
                    mMonth = Integer.parseInt(ed_data_validade_lote_vacina.getText().toString().substring(3, 5)) - 1;
                    mYear = Integer.parseInt(ed_data_validade_lote_vacina.getText().toString().substring(6, 10));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dia, mes;

                        if (dayOfMonth < 10)
                            dia = "0" + dayOfMonth;
                        else
                            dia = String.valueOf(dayOfMonth);

                        if (monthOfYear + 1 < 10)
                            mes = "0" + (monthOfYear + 1);
                        else
                            mes = String.valueOf(monthOfYear + 1);

                        ed_data_validade_lote_vacina.setText(dia + "/" + mes + "/" + year);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });
    }

    private void confirmarCadastro()
    {
        bt_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacoesCadastro())
                {
                    if (_modo.equals("INS")) {
                        Lote_Vacina lote_vacina = new Lote_Vacina();
                        lote_vacina.setNumero_lote_vacina(ed_numero_lote_vacina.getText().toString());
                        lote_vacina.setData_fabricacao_lote_vacina(ed_data_fabricacao_lote_vacina.getText().toString());
                        lote_vacina.setData_validade_lote_vacina(ed_data_validade_lote_vacina.getText().toString());

                        listenerC.criaLoteVacina(lote_vacina);

                        getDialog().hide();
                    }
                    else{
                        Lote_Vacina lote_vacina = new Lote_Vacina();
                        lote_vacina.setNumero_lote_vacina(ed_numero_lote_vacina.getText().toString());
                        lote_vacina.setData_fabricacao_lote_vacina(ed_data_fabricacao_lote_vacina.getText().toString());
                        lote_vacina.setData_validade_lote_vacina(ed_data_validade_lote_vacina.getText().toString());

                        listenerA.alteraLoteVacina(lote_vacina);

                        getDialog().hide();
                    }
                }
            }
        });
    }

    private boolean validacoesCadastro()
    {
        boolean validacao = false;

        if (ed_numero_lote_vacina.getText().length() == 0)
            ed_numero_lote_vacina.setError("Informe o número do lote!");
        else {
            if (ed_data_fabricacao_lote_vacina.getText().length() == 0)
                ed_data_fabricacao_lote_vacina.setError("Informe a data de fabricação do lote!");
            else {
                if (ed_data_validade_lote_vacina.getText().length() == 0)
                    ed_data_validade_lote_vacina.setError("Informe a data de validade do lote!");
                else {
                    String strDataFabricacao;
                    String strDataHoje;
                    Date dataFabricacao;
                    Date dataHoje;
                    Calendar c = Calendar.getInstance();

                    strDataFabricacao = ed_data_fabricacao_lote_vacina.getText().toString();
                    strDataHoje = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

                    dataFabricacao = stringToDate(strDataFabricacao);
                    dataHoje = stringToDate(strDataHoje);

                    if (dataFabricacao.compareTo(dataHoje) > 0)
                        ed_data_fabricacao_lote_vacina.setError("Data de fabricação deve ser menor ou igual a data de hoje!");
                    else {
                        String strDataValidade;
                        Date dataValidade;

                        strDataValidade = ed_data_validade_lote_vacina.getText().toString();

                        dataValidade = stringToDate(strDataValidade);
                        dataHoje = stringToDate(strDataHoje);

                        if (dataValidade.compareTo(dataHoje) < 0)
                            ed_data_validade_lote_vacina.setError("Data de validade deve ser maior ou igual a data de hoje!");
                        else
                            validacao = true;
                    }
                }
            }
        }

        return validacao;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listenerC = (CriaLoteVacina) context;
            listenerA = (AlteraLoteVacina) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface CriaLoteVacina {
        void criaLoteVacina(Lote_Vacina lote_vacina);
    }

    public interface AlteraLoteVacina {
        void alteraLoteVacina(Lote_Vacina lote_vacina);
    }

    public Date stringToDate(String dataStr)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try
        {
            Date date = formatter.parse(dataStr);
            return date;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}