package br.com.virtualbovapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import br.com.virtualbovapp.R;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroAnimalActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroEstrategiaActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroLocalActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroLoteActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroMedicamentoActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroProtocoloSanitarioActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroRacaActivity;
import br.com.virtualbovapp.activities.consultas.ConsultaCadastroVacinaActivity;

public class AnimaisFragmentCadastro extends Fragment {
    private CardView racas_cardview, animais_cardview, locais_cardview, estrategias_cardview, lotes_cardview, vacinas_cardview, medicamentos_cardview, protocolos_sanitarios_cardview;
    private Intent intent;

    public AnimaisFragmentCadastro() {
        // Required empty public constructor
    }

    public static AnimaisFragmentCadastro newInstance(String param1, String param2) {
        AnimaisFragmentCadastro fragment = new AnimaisFragmentCadastro();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animais_fragment_cadastro, container, false);

        animais_cardview = view.findViewById(R.id.animais_cardview);
        racas_cardview = view.findViewById(R.id.racas_cardview);
        locais_cardview = view.findViewById(R.id.locais_cardview);
        estrategias_cardview = view.findViewById(R.id.estrategias_cardview);
        lotes_cardview = view.findViewById(R.id.lotes_cardview);
        vacinas_cardview = view.findViewById(R.id.vacinas_cardview);
        medicamentos_cardview = view.findViewById(R.id.medicamentos_cardview);
        protocolos_sanitarios_cardview = view.findViewById(R.id.protocolos_sanitarios_cardview);


        animais_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroAnimalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        racas_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroRacaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        locais_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroLocalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        estrategias_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroEstrategiaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        lotes_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroLoteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        vacinas_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroVacinaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        medicamentos_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroMedicamentoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        protocolos_sanitarios_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ConsultaCadastroProtocoloSanitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}