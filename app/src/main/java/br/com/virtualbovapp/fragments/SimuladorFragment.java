package br.com.virtualbovapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.virtualbovapp.R;

public class SimuladorFragment extends Fragment {
    public SimuladorFragment() {
        // Required empty public constructor
    }

    public static SimuladorFragment newInstance(String param1, String param2) {
        SimuladorFragment fragment = new SimuladorFragment();
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
        View view = inflater.inflate(R.layout.simulador_fragment, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}