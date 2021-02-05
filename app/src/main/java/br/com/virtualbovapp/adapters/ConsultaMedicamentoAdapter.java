package br.com.virtualbovapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import br.com.virtualbovapp.model.Medicamento;
import br.com.virtualbovapp.R;

public class ConsultaMedicamentoAdapter extends RecyclerView.Adapter<ConsultaMedicamentoAdapter.MedicamentoViewHolder> implements Filterable {
	private final ArrayList<Medicamento> medicamentosList;
	private List<Medicamento> medicamentosListFiltered;
	private final MedicamentosAdapterListener listener;

	public ConsultaMedicamentoAdapter(ArrayList<Medicamento> medicamentos, MedicamentosAdapterListener listener) {
		this.medicamentosList = medicamentos;
		this.medicamentosListFiltered = medicamentos;
		this.listener = listener;
	}

	@NonNull
	@Override
	public MedicamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_medicamento, parent, false);
		return new MedicamentoViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final MedicamentoViewHolder holder, final int position) {
		holder.tv_nome_medicamento.setText(medicamentosListFiltered.get(position).getNome_medicamento());
		holder.tv_desc_complementar_medicamento.setText(medicamentosListFiltered.get(position).getDesc_complementar_medicamento());
	}

	@Override
	public int getItemCount() {
		if (medicamentosListFiltered != null)
			return medicamentosListFiltered.size();
		else
			return 0;
	}

	public List<Medicamento> getList()
	{
		return medicamentosListFiltered;
	}

	public void removeItem(int position) {
		medicamentosListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class MedicamentoViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_medicamento, tv_desc_complementar_medicamento;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public MedicamentoViewHolder(View itemView) {
			super(itemView);
			tv_nome_medicamento = itemView.findViewById(R.id.tv_nome_medicamento);
			tv_desc_complementar_medicamento = itemView.findViewById(R.id.tv_desc_complementar_medicamento);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onMedicamentoSelected(medicamentosListFiltered.get(getAdapterPosition()));
				}
			});
		}
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();

				if (charString.isEmpty()) {
					medicamentosListFiltered = medicamentosList;
				} else {
					List<Medicamento> filteredList = new ArrayList<>();
					for (Medicamento row : medicamentosList)
					{
						if (row.getNome_medicamento().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					medicamentosListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = medicamentosListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				medicamentosListFiltered = (ArrayList<Medicamento>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public interface MedicamentosAdapterListener {
		void onMedicamentoSelected(Medicamento medicamento);
	}
}