package br.com.virtualbovapp.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.fragments.AnimaisFragment;
import br.com.virtualbovapp.fragments.FinanceiroFragment;
import br.com.virtualbovapp.fragments.HomeFragment;
import br.com.virtualbovapp.fragments.NutricaoFragment;
import br.com.virtualbovapp.fragments.SimuladorFragment;
import br.com.virtualbovapp.helpers.BottomNavigationBehavior;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private Fragment visibleFragment;
    private BottomNavigationView navigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        navigation = findViewById(R.id.navigation);

        toolbar = getSupportActionBar();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        toolbar.setTitle("VirtualBov");
                        fragment = new HomeFragment();
                        loadFragment(fragment, "TagHome");
                        return true;

                    case R.id.navigation_animais:
                        toolbar.setTitle("Animais");
                        fragment = new AnimaisFragment();
                        loadFragment(fragment, "TagAnimais");
                        return true;

                    case R.id.navigation_financeiro:
                        toolbar.setTitle("Financeiro");
                        fragment = new FinanceiroFragment();
                        loadFragment(fragment, "TagFinanceiro");
                        return true;

                    case R.id.navigation_nutricao:
                        toolbar.setTitle("Nutrição");
                        fragment = new NutricaoFragment();
                        loadFragment(fragment, "TagNutricao");
                        return true;

                    case R.id.navigation_simulador:
                        toolbar.setTitle("Simulador");
                        fragment = new SimuladorFragment();
                        loadFragment(fragment, "TagSimulador");
                        return true;
                }

                return false;
            }
        });

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("VirtualBov");
        loadFragment(new HomeFragment(), "TagHome");
    }

    @Override
    protected void onStart() { super.onStart(); }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        visibleFragment = getSupportFragmentManager().getFragments().get(0);

        if (visibleFragment.getTag() != "TagHome")
        {
            navigation.setSelectedItemId(R.id.navigation_home);
        }
        else
            finish();
    }
}
