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
import br.com.virtualbovapp.activities.cadastros.CadastroLocalActivity;
import br.com.virtualbovapp.adapters.ConsultaLocalAdapter;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.helpers.LocalItemTouchHelper;
import br.com.virtualbovapp.model.Local;

public class ConsultaCadastroLocalActivity extends AppCompatActivity implements LocalItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaLocalAdapter.LocaisAdapterListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ConsultaLocalAdapter locaisAdapter;
    private Intent intent;
    private FloatingActionButton fab_novo;
    private SearchView searchView;
    private AlertDialog alerta;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private TextView tv_title_empty;
    private ArrayList<Local> locais;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private static String ROOT = "BD";
    private static String CHILDREN = "local";

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
        tv_title_empty.setText("Não temos nenhum local cadastrado");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        itemTouchHelperCallback = new LocalItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setAdapter()
    {
        locais = new ArrayList<>();
        locaisAdapter = new ConsultaLocalAdapter(locais, this);
        recyclerView.setAdapter(locaisAdapter);
        locaisAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void carregaListaFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN);

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        locais.add(snapshot.getValue(Local.class));
                        locaisAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Local new_local = snapshot.getValue(Local.class);
                        String key_local = snapshot.getKey();

                        for (int i = 0; i < locais.size(); i++) {
                            Local local_aux = locais.get(i);

                            if (local_aux.getKey_local().equals(key_local)) {
                                locais.set(i, new_local);
                                break;
                            }
                        }

                        locaisAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados na atualização. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    try {
                        String key_local = snapshot.getKey();

                        for (int i = 0; i < locais.size(); i++)
                        {
                            Local local_aux = locais.get(i);

                            if (local_aux.getKey_local().equals(key_local)){
                                locais.remove(i);
                                break;
                            }
                        }

                        locaisAdapter.notifyDataSetChanged();
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
                intent = new Intent(getBaseContext(), CadastroLocalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("_local", new Local());
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
                locaisAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                locaisAdapter.getFilter().filter(query);
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
        if (viewHolder instanceof ConsultaLocalAdapter.LocalViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir o local "  +  locaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_local() + "?";

            builder.setTitle("Exclusão de local");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.localMovimentado(locaisAdapter.getList().get(viewHolder.getAdapterPosition()).getId_local()) == false) {
                        String messageToast =   "Local " + locaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_local();
                        messageToast += " foi excluído com sucesso!";

                        DatabaseReference databaseReferenceDel = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN).child(locaisAdapter.getList().get(viewHolder.getAdapterPosition()).getKey_local());
                        databaseReferenceDel.removeValue();

                        Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                        //Toast.makeText(getBaseContext(), "Local não pode ser deletado pois já possui movimentação!", Toast.LENGTH_LONG).show();

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
    public void onLocalSelected(Local local) {
        intent = new Intent(this, CadastroLocalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("_local", local);
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