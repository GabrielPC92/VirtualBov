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
import br.com.virtualbovapp.adapters.ConsultaLocalAdapter;
import br.com.virtualbovapp.model.Local;

public class PesquisaLocalDialogFragment extends DialogFragment implements ConsultaLocalAdapter.LocaisAdapterListener {
    private RecyclerView recyclerView;
    private ConsultaLocalAdapter locaisAdapter;
    private TextView tv_title_empty;
    private SearchView sv_pesquisa;
    private LinearLayoutManager linearLayoutManager;
    private LocaisFragmentListener listener;
    private ArrayList<Local> locais;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private static String ROOT = "BD";
    private static String CHILDREN = "local";

    public PesquisaLocalDialogFragment() {
        // Required empty public constructor
    }

    public static PesquisaLocalDialogFragment newInstance() {
        PesquisaLocalDialogFragment frag = new PesquisaLocalDialogFragment();
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
        tv_title_empty.setText("Não temos nenhum local para pesquisa");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void setAdaper()
    {
        locais = new ArrayList<>();
        locaisAdapter = new ConsultaLocalAdapter(locais, this);
        recyclerView.setAdapter(locaisAdapter);
        locaisAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
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
                            locais.add(data.getValue(Local.class));
                        }

                        locaisAdapter.notifyDataSetChanged();
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
                locaisAdapter.getFilter().filter(txt);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String txt) {
                locaisAdapter.getFilter().filter(txt);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setTitle("Pesquisa de locais");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PesquisaLocalDialogFragment.LocaisFragmentListener) context;
    }

    @Override
    public void onLocalSelected(Local local) {
        listener.onLocalSelected(local);
    }

    public interface LocaisFragmentListener {
        void onLocalSelected(Local local);
    }
}