package br.com.virtualbovapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.virtualbovapp.R;

public class NutricaoFragment extends Fragment {
    public NutricaoFragment() {
        // Required empty public constructor
    }

    public static NutricaoFragment newInstance(String param1, String param2) {
        NutricaoFragment fragment = new NutricaoFragment();
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
        View view = inflater.inflate(R.layout.nutricao_fragment, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}