package br.com.virtualbovapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.Vacina;

public class ConsultaVacinaAdapter extends RecyclerView.Adapter<ConsultaVacinaAdapter.VacinaViewHolder> implements Filterable {
	private ArrayList<Vacina> vacinasList;
	private List<Vacina> vacinasListFiltered;
	private VacinasAdapterListener listener;

	public ConsultaVacinaAdapter(ArrayList<Vacina> vacinas, VacinasAdapterListener listener) {
		this.vacinasList = vacinas;
		this.vacinasListFiltered = vacinas;
		this.listener = listener;
	}

	@Override
	public VacinaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_vacina, parent, false);
		return new VacinaViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final VacinaViewHolder holder, final int position) {
		holder.tv_nome_vacina.setText(vacinasListFiltered.get(position).getNome_vacina());
		holder.tv_desc_complementar_vacina.setText(vacinasListFiltered.get(position).getDesc_complementar_vacina());
	}

	@Override
	public int getItemCount() {
		if(vacinasListFiltered != null)
			return vacinasListFiltered.size();
		else
			return 0;
	}

	public List<Vacina> getList()
	{
		return vacinasListFiltered;
	}

	public void removeItem(int position) {
		vacinasListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class VacinaViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_vacina, tv_desc_complementar_vacina, tv_capacidade_vacina;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public VacinaViewHolder(View itemView) {
			super(itemView);
			tv_nome_vacina = itemView.findViewById(R.id.tv_nome_vacina);
			tv_desc_complementar_vacina = itemView.findViewById(R.id.tv_desc_complementar_vacina);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onVacinaSelected(vacinasListFiltered.get(getAdapterPosition()));
				}
			});
		}
	}

	public void updateList(ArrayList<Vacina> newlist) {
		vacinasList = newlist;
		vacinasListFiltered = newlist;
		this.notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();

				if (charString.isEmpty()) {
					vacinasListFiltered = vacinasList;
				} else {
					List<Vacina> filteredList = new ArrayList<>();
					for (Vacina row : vacinasList)
					{
						if (row.getNome_vacina().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					vacinasListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = vacinasListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				vacinasListFiltered = (ArrayList<Vacina>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public interface VacinasAdapterListener {
		void onVacinaSelected(Vacina vacina);
	}
}