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
import br.com.virtualbovapp.activities.cadastros.CadastroMedicamentoActivity;
import br.com.virtualbovapp.model.Medicamento;
import br.com.virtualbovapp.adapters.ConsultaMedicamentoAdapter;
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.helpers.RVEmptyObserver;
import br.com.virtualbovapp.helpers.MedicamentoItemTouchHelper;

public class ConsultaCadastroMedicamentoActivity extends AppCompatActivity implements MedicamentoItemTouchHelper.RecyclerItemTouchHelperListener, ConsultaMedicamentoAdapter.MedicamentosAdapterListener {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ConsultaMedicamentoAdapter medicamentosAdapter;
    private Intent intent;
    private FloatingActionButton fab_novo;
    private SearchView searchView;
    private AlertDialog alerta;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private TextView tv_title_empty;
    private ArrayList<Medicamento> medicamentos;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private static String ROOT = "BD";
    private static String CHILDREN = "medicamento";

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
        tv_title_empty.setText("Não temos nenhum medicamento cadastrado");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        itemTouchHelperCallback = new MedicamentoItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setAdapter()
    {
        medicamentos = new ArrayList<>();
        medicamentosAdapter = new ConsultaMedicamentoAdapter(medicamentos, this);
        recyclerView.setAdapter(medicamentosAdapter);
        medicamentosAdapter.registerAdapterDataObserver(new RVEmptyObserver(recyclerView, tv_title_empty));
    }

    private void carregaListaFirebase()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN);

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        medicamentos.add(snapshot.getValue(Medicamento.class));
                        medicamentosAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        Medicamento new_medicamento = snapshot.getValue(Medicamento.class);
                        String key_medicamento = snapshot.getKey();

                        for (int i = 0; i < medicamentos.size(); i++) {
                            Medicamento medicamento_aux = medicamentos.get(i);

                            if (medicamento_aux.getKey_medicamento().equals(key_medicamento)) {
                                medicamentos.set(i, new_medicamento);
                                break;
                            }
                        }

                        medicamentosAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Falha a ler os dados na atualização. Tente novamente!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    try {
                        String key_medicamento = snapshot.getKey();

                        for (int i = 0; i < medicamentos.size(); i++)
                        {
                            Medicamento medicamento_aux = medicamentos.get(i);

                            if (medicamento_aux.getKey_medicamento().equals(key_medicamento)){
                                medicamentos.remove(i);
                                break;
                            }
                        }

                        medicamentosAdapter.notifyDataSetChanged();
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
                intent = new Intent(getBaseContext(), CadastroMedicamentoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("_medicamento", new Medicamento());
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
                medicamentosAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                medicamentosAdapter.getFilter().filter(query);
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
        if (viewHolder instanceof ConsultaMedicamentoAdapter.MedicamentoViewHolder) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String message =   "Excluir o medicamento "  +  medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_medicamento() + "?";

            builder.setTitle("Exclusão de medicamento");
            builder.setMessage(message);

            builder.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //if (bc.medicamentoMovimentada(medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getId_medicamento()) == false) {
                    String messageToast =   "Medicamento " + medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getNome_medicamento();
                    messageToast += " foi excluído com sucesso!";

                    DatabaseReference databaseReferenceDel = FirebaseDatabase.getInstance().getReference().child(ROOT).child(CHILDREN).child(medicamentosAdapter.getList().get(viewHolder.getAdapterPosition()).getKey_medicamento());
                    databaseReferenceDel.removeValue();

                    Toast.makeText(getBaseContext(), messageToast, Toast.LENGTH_LONG).show();
                    //}
                    //else
                    //Toast.makeText(getBaseContext(), "Medicamento não pode ser deletada pois já possui movimentação!", Toast.LENGTH_LONG).show();

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
    public void onMedicamentoSelected(Medicamento medicamento) {
        intent = new Intent(this, CadastroMedicamentoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("_medicamento", medicamento);
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