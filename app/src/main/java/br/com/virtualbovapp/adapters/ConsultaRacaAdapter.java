package br.com.virtualbovapp.adapters;

import android.app.Activity;
import android.content.Intent;
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
import br.com.virtualbovapp.model.Raca;

public class ConsultaRacaAdapter extends RecyclerView.Adapter<ConsultaRacaAdapter.RacaViewHolder> implements Filterable {
	private ArrayList<Raca> racasList;
	private List<Raca> racasListFiltered;
	private Activity activity;
	private Intent intent;
	private RacasAdapterListener listener;

	public ConsultaRacaAdapter(ArrayList<Raca> racas, RacasAdapterListener listener) {
		this.racasList = racas;
		this.racasListFiltered = racas;
		this.listener = listener;
	}

	@Override
	public RacaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_raca, parent, false);
		return new RacaViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final RacaViewHolder holder, final int position) {
		holder.tv_nome_raca.setText(racasListFiltered.get(position).getNome_raca());
		holder.tv_origem_raca.setText(getNome_origem_raca(racasListFiltered.get(position).getOrigem_raca()));
	}

	@Override
	public int getItemCount() {
		return racasListFiltered.size();
	}

	public List<Raca> getList()
	{
		return racasListFiltered;
	}

	public void removeItem(int position) {
		racasListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class RacaViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_raca, tv_origem_raca;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public RacaViewHolder(View itemView) {
			super(itemView);
			tv_nome_raca = itemView.findViewById(R.id.tv_nome_raca);
			tv_origem_raca = itemView.findViewById(R.id.tv_origem_raca);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onRacaSelected(racasListFiltered.get(getAdapterPosition()));
				}
			});
		}
	}

	public void updateList(ArrayList<Raca> newlist) {
		racasList = newlist;
		racasListFiltered = newlist;
		this.notifyDataSetChanged();
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();

				if (charString.isEmpty()) {
					racasListFiltered = racasList;
				} else {
					List<Raca> filteredList = new ArrayList<>();
					for (Raca row : racasList)
					{
						if (String.valueOf(row.getId_raca()).contentEquals(charSequence) ||
								row.getNome_raca().toLowerCase().contains(charString.toLowerCase()))
						{
							filteredList.add(row);
						}
					}

					racasListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = racasListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				racasListFiltered = (ArrayList<Raca>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public String getNome_origem_raca(int id_origem) {
		String nome_origem_raca = "";

		switch (id_origem) {
			case 1:
				nome_origem_raca = "  Puro de Origem  ";
				break;
			case 2:
				nome_origem_raca = "  Cruzamento Industrial  ";
				break;
			case 3:
				nome_origem_raca = "  Sem Raça Definida  ";
				break;
		}

		return nome_origem_raca;
	}

	public interface RacasAdapterListener {
		void onRacaSelected(Raca raca);
	}
}