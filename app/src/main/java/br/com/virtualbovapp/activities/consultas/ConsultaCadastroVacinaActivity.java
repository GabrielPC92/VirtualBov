package br.com.virtualbovapp.activities.consultas;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import br.com.virtualbovapp.activities.cadastros.CadastroVacinaActivity;
import br.com.virtualbovapp.model.Vacina;
import br.com.virtualbovapp.adapters.ConsultaVacinaAdapter;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.helpers.VacinaItemTouchHelper;

public class ConsultaCadastroVacinaActivity extends AppCompatActivity implements VacinaItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaVacinaAdapter.VacinasAdapterListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ConsultaVacinaAdapter vacinasAdapter;
    private Intent intent;
    private FloatingActionButton fab_novo;
    private SearchView searchView;
    private AlertDialog alerta;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private TextView tv_title_empty;
    private ArrayList<Vacina> vacinas;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private static String ROOT = "BD";
    private static String CHILDREN = "vacina";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carregaView();

        personalizaView();

        setAdapter();

        carregaListaFirebase();

        novoCadastro();
    }

    private void carregaView()
    {
        setContentView(R.layout.consulta_activity);

        recyclerView = findViewById(R.id.recycler_view);
        tv_title_empty = findViewById(R.id.tv_title_empty);
        fab_novo = findViewById(R.id.fab_novo);
    }

    private void personalizaView()
    {
        tv_title_empty.setText("Não temos nenhuma vacina cadastrada");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        itemTouchHelperCallback = new VacinaItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setAdapter()
    {
        vacinas = new ArrayList<>();
        vacinasAdapter = new ConsultaVacinaAdapter(vacinas, this);
        recyclerView.setAdapter(vacinasAdapter);
        vacinasAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }
    
    private void carregaListaFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN);

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        vacinas.add(snapshot.getValue(Vacina.class));
                        vacinasAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Vacina new_vacina = snapshot.getValue(Vacina.class);
                        String key_vacina = snapshot.getKey();

                        for (int i = 0; i < vacinas.size(); i++) {
                            Vacina vacina_aux = vacinas.get(i);

                            if (vacina_aux.getKey_vacina().equals(key_vacina)) {
                                vacinas.set(i, new_vacina);
                                break;
                            }
                        }

                        vacinasAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados na atualização. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    try {
                        String key_vacina = snapshot.getKey();

                        for (int i = 0; i < vacinas.size(); i++)
                        {
                            Vacina vacina_aux = vacinas.get(i);

                            if (vacina_aux.getKey_vacina().equals(key_vacina)){
                                vacinas.remove(i);
                                break;
                            }
                        }

                        vacinasAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados na remoção. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getBaseContext(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                    finish();
                }
            };

            databaseReference.addChildEventListener(childEventListener);
        }
    }

    private void novoCadastro()
    {
        fab_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), CadastroVacinaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("_vacina", new Vacina());
                intent.putExtra("_modo", "INS");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vacinasAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                vacinasAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);

            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof ConsultaVacinaAdapter.VacinaViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir a vacina "  +  vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_vacina() + "?";

            builder.setTitle("Exclusão de vacina");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.vacinaMovimentada(vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getId_vacina()) == false) {
                        String messageToast =   "Vacina " + vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_vacina();
                        messageToast += " foi excluída com sucesso!";

                        DatabaseReference databaseReferenceDel = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN).child(vacinasAdapter.getList().get(viewHolder.getAdapterPosition()).getKey_vacina());
                        databaseReferenceDel.removeValue();

                        Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                        //Toast.makeText(getBaseContext(), "Vacina não pode ser deletada pois já possui movimentação!", Toast.LENGTH_LONG).show();
                    
                    if (searchView != null) {
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                    }
                }
            });

            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    itemTouchHelper.attachToRecyclerView(null);
                    itemTouchHelper.attachToRecyclerView(recyclerView);

                    dialog.cancel();
                }
            });

            alerta = builder.create();
            alerta.show();
            alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    itemTouchHelper.attachToRecyclerView(null);
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            });
        }
    }

    @Override
    public void onVacinaSelected(Vacina vacina) {
        intent = new Intent(this, CadastroVacinaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("_vacina", vacina);
        intent.putExtra("_modo", "UPD");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }
}