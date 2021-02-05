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

import br.com.virtualbovapp.model.Estrategia;
import br.com.virtualbovapp.R;

public class ConsultaEstrategiaAdapter extends RecyclerView.Adapter<ConsultaEstrategiaAdapter.EstrategiaViewHolder> implements Filterable {
	private final ArrayList<Estrategia> estrategiasList;
	private List<Estrategia> estrategiasListFiltered;
	private final EstrategiasAdapterListener listener;

	public ConsultaEstrategiaAdapter(ArrayList<Estrategia> estrategias, EstrategiasAdapterListener listener) {
		this.estrategiasList = estrategias;
		this.estrategiasListFiltered = estrategias;
		this.listener = listener;
	}

	@NonNull
	@Override
	public EstrategiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_estrategia, parent, false);
		return new EstrategiaViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final EstrategiaViewHolder holder, final int position) {
		holder.tv_nome_estrategia.setText(estrategiasListFiltered.get(position).getNome_estrategia());
		holder.tv_desc_complementar_estrategia.setText(estrategiasListFiltered.get(position).getDesc_complementar_estrategia());
		holder.tv_tipo_estrategia.setText(getNome_tipo_estrategia(estrategiasListFiltered.get(position).getTipo_estrategia()));
	}

	@Override
	public int getItemCount() {
		return estrategiasListFiltered.size();
	}

	public List<Estrategia> getList()
	{
		return estrategiasListFiltered;
	}

	public void removeItem(int position) {
		estrategiasListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class EstrategiaViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_estrategia, tv_desc_complementar_estrategia, tv_tipo_estrategia;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public EstrategiaViewHolder(View itemView) {
			super(itemView);
			tv_nome_estrategia = itemView.findViewById(R.id.tv_nome_estrategia);
			tv_desc_complementar_estrategia = itemView.findViewById(R.id.tv_desc_complementar_estrategia);
			tv_tipo_estrategia = itemView.findViewById(R.id.tv_tipo_estrategia);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onEstrategiaSelected(estrategiasListFiltered.get(getAdapterPosition()));
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
					estrategiasListFiltered = estrategiasList;
				} else {
					List<Estrategia> filteredList = new ArrayList<>();
					for (Estrategia row : estrategiasList)
					{
						if (row.getNome_estrategia().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					estrategiasListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = estrategiasListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				estrategiasListFiltered = (ArrayList<Estrategia>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public String getNome_tipo_estrategia(int id_estrategia) {
		String nome_tipo_estrategia = "";

		switch (id_estrategia) {
			case 1:
				nome_tipo_estrategia = "  Pasto  ";
				break;
			case 2:
				nome_tipo_estrategia = "  Confinado  ";
				break;
			case 3:
				nome_tipo_estrategia = "  T.I.P  ";
				break;
			case 4:
				nome_tipo_estrategia = "  Matriz  ";
				break;
			case 5:
				nome_tipo_estrategia = "  Reprodutor  ";
				break;
			case 6:
				nome_tipo_estrategia = "  Vaca de Leite  ";
				break;
			case 7:
				nome_tipo_estrategia = "  Semi Confinamento  ";
				break;
			case 8:
				nome_tipo_estrategia = "  Descarte  ";
				break;
		}

		return nome_tipo_estrategia;
	}

	public interface EstrategiasAdapterListener {
		void onEstrategiaSelected(Estrategia estrategia);
	}
}