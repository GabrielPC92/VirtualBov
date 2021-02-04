package br.com.virtualbovapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import br.com.virtualbovapp.R;

public class FinanceiroFragment extends Fragment {
    public FinanceiroFragment() {
        // Required empty public constructor
    }

    public static FinanceiroFragment newInstance(String param1, String param2) {
        FinanceiroFragment fragment = new FinanceiroFragment();
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
        View view = inflater.inflate(R.layout.financeiro_fragment, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}