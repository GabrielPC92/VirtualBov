package br.com.virtualbovapp.dialogfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.adapters.ConsultaEstrategiaAdapter;
import br.com.virtualbovapp.model.Estrategia;

public class PesquisaEstrategiaDialogFragment extends DialogFragment implements ConsultaEstrategiaAdapter.EstrategiasAdapterListener {
    private RecyclerView recyclerView;
    private ConsultaEstrategiaAdapter estrategiasAdapter;
    private TextView tv_title_empty;
    private SearchView sv_pesquisa;
    private LinearLayoutManager linearLayoutManager;
    private EstrategiasFragmentListener listener;
    private ArrayList<Estrategia> estrategias;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private static String ROOT = "BD";
    private static String CHILDREN = "estrategia";

    public PesquisaEstrategiaDialogFragment() {
        // Required empty public constructor
    }

    public static PesquisaEstrategiaDialogFragment newInstance() {
        PesquisaEstrategiaDialogFragment frag = new PesquisaEstrategiaDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pesquisa_fragment, container, false);

        carregaView(view);

        personalizaView();

        setAdaper();

        carregaListaFirebase();

        botaoPesquuisa();

        return view;
    }

    private void carregaView(View view)
    {
        sv_pesquisa = view.findViewById(R.id.sv_pesquisa);
        recyclerView = view.findViewById(R.id.recycler_view);
        tv_title_empty = view.findViewById(R.id.tv_title_empty);
    }

    private void personalizaView()
    {
        tv_title_empty.setText("Não temos nenhuma estratégia para pesquisa");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void setAdaper()
    {
        estrategias = new ArrayList<>();
        estrategiasAdapter = new ConsultaEstrategiaAdapter(estrategias, this);
        recyclerView.setAdapter(estrategiasAdapter);
        estrategiasAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void carregaListaFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN);

        if (valueEventListener == null) {
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for(DataSnapshot data : snapshot.getChildren())
                        {
                            estrategias.add(data.getValue(Estrategia.class));
                        }

                        estrategiasAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getActivity(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            databaseReference.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void botaoPesquuisa()
    {
        sv_pesquisa.setQueryHint("Pesquisar...");
        sv_pesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                estrategiasAdapter.getFilter().filter(txt);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String txt) {
                estrategiasAdapter.getFilter().filter(txt);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setTitle("Pesquisa de estratégias");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PesquisaEstrategiaDialogFragment.EstrategiasFragmentListener) context;
    }

    @Override
    public void onEstrategiaSelected(Estrategia estrategia) {
        listener.onEstrategiaSelected(estrategia);
    }

    public interface EstrategiasFragmentListener {
        void onEstrategiaSelected(Estrategia estrategia);
    }
}