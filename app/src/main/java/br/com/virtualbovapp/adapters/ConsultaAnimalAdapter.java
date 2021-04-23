package br.com.virtualbovapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
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
	private final Context context;

	public ConsultaAnimalAdapter(ArrayList<Animal> animais, AnimaisAdapterListener listener, Context context) {
		this.animaisList = animais;
		this.animaisListFiltered = animais;
		this.listener = listener;
		this.context = context;
	}

	@NonNull
	@Override
	public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_animal, parent, false);
		return new AnimalViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final AnimalViewHolder holder, final int position) {
		if(animaisListFiltered.get(position).getSexo_animal().equals("Masculino")) {
			holder.iv_sexo_animal.setImageResource(R.drawable.ic_male);
			holder.iv_sexo_animal.setColorFilter(context.getResources().getColor(R.color.sex_male));
			holder.tv_desc_sexo_animal.setText("Macho");
		}
		else {
			holder.iv_sexo_animal.setImageResource(R.drawable.ic_female);
			holder.iv_sexo_animal.setColorFilter(context.getResources().getColor(R.color.sex_female));
			holder.tv_desc_sexo_animal.setText("Fêmea");
		}

		holder.tv_brinco_animal.setText(animaisListFiltered.get(position).getBrinco_animal());
		holder.iv_local_animal.setImageResource(R.drawable.ic_localizacao);
		holder.tv_nome_local_animal.setText("NOME DO LOCAL");
		holder.tv_pesagem_animal.setText(animaisListFiltered.get(position).getPesagem_animal() + " Kg");
		holder.tv_nome_lote_animal.setText("NOME DO LOTE");
		holder.tv_peso_gmd_animal.setText("1,48 Kg/GMD");


		if(animaisListFiltered.get(position).getMorte_animal().length() > 0)
			holder.tv_vivo_morto_animal.setText(" Morto ");
		else
			holder.tv_vivo_morto_animal.setText(" Vivo ");

		holder.tv_preenhe_animal.setText(" Preenhe ");
		holder.tv_estrategia_animal.setText(" Pasto ");
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
		TextView tv_brinco_animal, tv_nome_local_animal, tv_pesagem_animal, tv_desc_sexo_animal, tv_nome_lote_animal, tv_peso_gmd_animal, tv_vivo_morto_animal, tv_preenhe_animal, tv_estrategia_animal;
		ImageView iv_sexo_animal, iv_local_animal;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public AnimalViewHolder(View itemView) {
			super(itemView);
			tv_brinco_animal = itemView.findViewById(R.id.tv_brinco_animal);
			iv_sexo_animal = itemView.findViewById(R.id.iv_sexo_animal);
			iv_local_animal = itemView.findViewById(R.id.iv_local_animal);
			tv_nome_local_animal = itemView.findViewById(R.id.tv_nome_local_animal);
			tv_pesagem_animal = itemView.findViewById(R.id.tv_pesagem_animal);
			tv_desc_sexo_animal = itemView.findViewById(R.id.tv_desc_sexo_animal);
			tv_nome_lote_animal = itemView.findViewById(R.id.tv_nome_lote_animal);
			tv_peso_gmd_animal = itemView.findViewById(R.id.tv_peso_gmd_animal);
			tv_vivo_morto_animal = itemView.findViewById(R.id.tv_vivo_morto_animal);
			tv_preenhe_animal = itemView.findViewById(R.id.tv_preenhe_animal);
			tv_estrategia_animal = itemView.findViewById(R.id.tv_estrategia_animal);

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
						if (row.getBrinco_animal().toLowerCase().contains(charString.toLowerCase()))
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