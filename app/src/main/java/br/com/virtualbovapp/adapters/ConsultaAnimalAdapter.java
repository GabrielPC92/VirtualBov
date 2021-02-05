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
import br.com.virtualbovapp.model.Animal;
import br.com.virtualbovapp.R;

public class ConsultaAnimalAdapter extends RecyclerView.Adapter<ConsultaAnimalAdapter.AnimalViewHolder> implements Filterable {
	private final ArrayList<Animal> animaisList;
	private List<Animal> animaisListFiltered;
	private final AnimaisAdapterListener listener;

	public ConsultaAnimalAdapter(ArrayList<Animal> animais, AnimaisAdapterListener listener) {
		this.animaisList = animais;
		this.animaisListFiltered = animais;
		this.listener = listener;
	}

	@NonNull
	@Override
	public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_raca, parent, false);
		return new AnimalViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final AnimalViewHolder holder, final int position) {
		holder.tv_brinco_animal.setText(animaisListFiltered.get(position).getBrinco_animal());
		holder.tv_nome_animal.setText(animaisListFiltered.get(position).getNome_animal());
	}

	@Override
	public int getItemCount() {
		return animaisListFiltered.size();
	}

	public List<Animal> getList()
	{
		return animaisListFiltered;
	}

	public void removeItem(int position) {
		animaisListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class AnimalViewHolder extends RecyclerView.ViewHolder {
		TextView tv_brinco_animal, tv_nome_animal;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public AnimalViewHolder(View itemView) {
			super(itemView);
			tv_brinco_animal = itemView.findViewById(R.id.tv_brinco_animal);
			tv_nome_animal = itemView.findViewById(R.id.tv_nome_animal);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onAnimalSelected(animaisListFiltered.get(getAdapterPosition()));
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
					animaisListFiltered = animaisList;
				} else {
					List<Animal> filteredList = new ArrayList<>();
					for (Animal row : animaisList)
					{
						if (row.getBrinco_animal().toLowerCase().contains(charString.toLowerCase()) ||
								row.getNome_animal().toLowerCase().contains(charString.toLowerCase()))
						{
							filteredList.add(row);
						}
					}

					animaisListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = animaisListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				animaisListFiltered = (ArrayList<Animal>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public interface AnimaisAdapterListener {
		void onAnimalSelected(Animal animal);
	}
}