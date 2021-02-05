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
import br.com.virtualbovapp.R;
import br.com.virtualbovapp.model.Local;

public class ConsultaLocalAdapter extends RecyclerView.Adapter<ConsultaLocalAdapter.LocalViewHolder> implements Filterable {
	private final ArrayList<Local> locaisList;
	private List<Local> locaisListFiltered;
	private final LocaisAdapterListener listener;

	public ConsultaLocalAdapter(ArrayList<Local> locais, LocaisAdapterListener listener) {
		this.locaisList = locais;
		this.locaisListFiltered = locais;
		this.listener = listener;
	}

	@NonNull
	@Override
	public LocalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_local, parent, false);
		return new LocalViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final LocalViewHolder holder, final int position) {
		holder.tv_nome_local.setText(locaisListFiltered.get(position).getNome_local());
		holder.tv_desc_complementar_local.setText(locaisListFiltered.get(position).getDesc_complementar_local());
		holder.tv_capacidade_local.setText(String.valueOf(locaisListFiltered.get(position).getCapacidade_local()));
	}

	@Override
	public int getItemCount() {
		return locaisListFiltered.size();
	}

	public List<Local> getList()
	{
		return locaisListFiltered;
	}

	public void removeItem(int position) {
		locaisListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class LocalViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_local, tv_desc_complementar_local, tv_capacidade_local;
		RelativeLayout viewBackground;
		public RelativeLayout viewForeground;

		public LocalViewHolder(View itemView) {
			super(itemView);
			tv_nome_local = itemView.findViewById(R.id.tv_nome_local);
			tv_desc_complementar_local = itemView.findViewById(R.id.tv_desc_complementar_local);
			tv_capacidade_local = itemView.findViewById(R.id.tv_capacidade_local);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onLocalSelected(locaisListFiltered.get(getAdapterPosition()));
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
					locaisListFiltered = locaisList;
				} else {
					List<Local> filteredList = new ArrayList<>();
					for (Local row : locaisList)
					{
						if (row.getNome_local().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					locaisListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = locaisListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				locaisListFiltered = (ArrayList<Local>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public interface LocaisAdapterListener {
		void onLocalSelected(Local local);
	}
}