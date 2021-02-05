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
import br.com.virtualbovapp.activities.cadastros.CadastroAnimalActivity;
import br.com.virtualbovapp.adapters.ConsultaAnimalAdapter;
import br.com.virtualbovapp.helpers.AnimalItemTouchHelper;
import br.com.virtualbovapp.model.Animal;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;

public class ConsultaCadastroAnimalActivity extends AppCompatActivity implements AnimalItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaAnimalAdapter.AnimaisAdapterListener {
    private RecyclerView recyclerView;
    private ConsultaAnimalAdapter animaisAdapter;
    private Intent intent;
    private FloatingActionButton fab_novo;
    private SearchView searchView;
    private ItemTouchHelper itemTouchHelper;
    private TextView tv_title_empty;
    private ArrayList<Animal> animais;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private static final String ROOT = "BD";
    private static final String CHILDREN = "animal";

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
        tv_title_empty.setText("Não temos nenhum animal cadastrado");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new AnimalItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setAdapter()
    {
        animais = new ArrayList<>();
        animaisAdapter = new ConsultaAnimalAdapter(animais, this);
        recyclerView.setAdapter(animaisAdapter);
        animaisAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void carregaListaFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN);

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        animais.add(snapshot.getValue(Animal.class));
                        animaisAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Animal new_animal = snapshot.getValue(Animal.class);
                        String key_animal = snapshot.getKey();

                        for (int i = 0; i < animais.size(); i++) {
                            Animal animal_aux = animais.get(i);

                            if (animal_aux.getKey_animal().equals(key_animal)) {
                                animais.set(i, new_animal);
                                break;
                            }
                        }

                        animaisAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados na atualização. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    try {
                        String key_animal = snapshot.getKey();

                        for (int i = 0; i < animais.size(); i++)
                        {
                            Animal animal_aux = animais.get(i);

                            if (animal_aux.getKey_animal().equals(key_animal)){
                                animais.remove(i);
                                break;
                            }
                        }

                        animaisAdapter.notifyDataSetChanged();
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

    private void novoCadastro() {
        fab_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), CadastroAnimalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("_animal", new Animal());
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
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                animaisAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                animaisAdapter.getFilter().filter(query);
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
        if (viewHolder instanceof ConsultaAnimalAdapter.AnimalViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir o animal "  +  animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_animal() + "?";

            builder.setTitle("Exclusão de animal");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.animalMovimentado(animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getBrinco_animal()) == false) {
                        String messageToast =   "Animal " + animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_animal();
                        messageToast += " foi excluído com sucesso!";

                        DatabaseReference databaseReferenceDel = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN).child(animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getKey_animal());
                        databaseReferenceDel.removeValue();

                        Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                        //Toast.makeText(getBaseContext(), "Animal não pode ser deletado pois já possui movimentação!", Toast.LENGTH_LONG).show();
                    
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

            AlertDialog alerta = builder.create();
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
    public void onAnimalSelected(Animal animal) {
        intent = new Intent(this, CadastroAnimalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("_animal", animal);
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