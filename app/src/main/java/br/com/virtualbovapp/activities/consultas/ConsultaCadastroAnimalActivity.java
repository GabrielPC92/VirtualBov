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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.virtualbovapp.activities.cadastros.CadastroAnimalActivity;
import br.com.virtualbovapp.sqlite.BancoController;
import br.com.virtualbovapp.adapters.ConsultaAnimalAdapter;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.helpers.AnimalItemTouchHelper;

public class ConsultaCadastroAnimalActivity extends AppCompatActivity implements AnimalItemTouchHelper.RecyclerItemTouchHelperListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ConsultaAnimalAdapter animaisAdapter;
    private Intent intent;
    private FloatingActionButton fab_novo;
    private BancoController bc;
    private SearchView searchView;
    private AlertDialog alerta;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private TextView tv_title_empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_activity);

        recyclerView = findViewById(R.id.recycler_view);
        tv_title_empty = findViewById(R.id.tv_title_empty);
        fab_novo = findViewById(R.id.fab_novo);

        tv_title_empty.setText("Não temos nenhum animal cadastrado");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        bc = new BancoController(getBaseContext());

        setAdapter();

        itemTouchHelperCallback = new AnimalItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), CadastroAnimalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("_brinco_animal", "");
                intent.putExtra("_modo", "INS");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bc = new BancoController(getBaseContext());
        animaisAdapter.updateList(bc.carregaAnimais());

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

            String message =   "Excluir o animal "  +  animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getBrinco_animal();
            message += " - " + animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_animal() + "?";

            builder.setTitle("Exclusão de animal");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.animalMovimentado(animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getBrinco_animal()) == false) {
                        String messageToast =   "Animal " + animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getBrinco_animal();
                        messageToast += " - " + animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_animal();
                        messageToast += " foi excluído com sucesso!";

                        bc.deletaAnimal(animaisAdapter.getList().get(viewHolder.getAdapterPosition()).getBrinco_animal());
                        animaisAdapter.removeItem(viewHolder.getAdapterPosition());

                        Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                        //Toast.makeText(getBaseContext(), "Animal não pode ser deletado pois já possui movimentação!", Toast.LENGTH_LONG).show();

                    setAdapter();

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

    public void setAdapter()
    {
        animaisAdapter = new ConsultaAnimalAdapter(this, bc.carregaAnimais());
        recyclerView.setAdapter(animaisAdapter);
        animaisAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }
}