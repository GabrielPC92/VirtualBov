package br.com.virtualbovapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.adapters.ViewPagerAdapter;

public class AnimaisFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.animais_fragment, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // find views by id
        viewPager = view.findViewById(R.id.animais_viewpager);
        tabLayout = view.findViewById(R.id.animais_tab_layout);

        setupViewPager();

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager()
    {
        viewPager.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new AnimaisFragmentCadastro(), "Cadastros");
        adapter.addFrag(new Fragment(), "Lançamentos");
        adapter.addFrag(new Fragment(), "Consultas");

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
    }
}