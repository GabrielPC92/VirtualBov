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
import br.com.virtualbovapp.model.Lote;
import br.com.virtualbovapp.R;

public class ConsultaLoteAdapter extends RecyclerView.Adapter<ConsultaLoteAdapter.LoteViewHolder> implements Filterable {
	private final ArrayList<Lote> lotesList;
	private List<Lote> lotesListFiltered;
	private final LotesAdapterListener listener;
	private final Context context;

	public ConsultaLoteAdapter(ArrayList<Lote> lotes, LotesAdapterListener listener, Context context) {
		this.lotesList = lotes;
		this.lotesListFiltered = lotes;
		this.listener = listener;
		this.context = context;
	}

	@NonNull
	@Override
	public LoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_lote, parent, false);
		return new LoteViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final LoteViewHolder holder, final int position) {
		holder.tv_nome_lote.setText(lotesListFiltered.get(position).getNome_lote());
		holder.tv_qtd_animais_lote.setText("1000");
		holder.tv_desc_complementar_lote.setText(lotesListFiltered.get(position).getDesc_complementar_lote());

		holder.iv_local_lote.setImageResource(R.drawable.ic_localizacao);
		holder.iv_local_lote.setColorFilter(context.getResources().getColor(R.color.localizacao));
		holder.iv_qtd_animais_lote.setImageResource(R.drawable.ic_animal);
		holder.iv_qtd_animais_lote.setColorFilter(context.getResources().getColor(R.color.orange));

		switch (lotesListFiltered.get(position).getTipo_animais_lote()) {
			case 1:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setVisibility(View.INVISIBLE);

				break;
			case 2:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setVisibility(View.INVISIBLE);

				break;
			case 3:
				holder.iv_sex01.setImageResource(R.drawable.ic_female);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_female));

				holder.iv_sex02.setVisibility(View.INVISIBLE);

				break;
			case 4:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setImageResource(R.drawable.ic_female);
				holder.iv_sex02.setColorFilter(context.getResources().getColor(R.color.sex_female));

				break;
			case 5:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setVisibility(View.INVISIBLE);

				break;
			case 6:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setImageResource(R.drawable.ic_female);
				holder.iv_sex02.setColorFilter(context.getResources().getColor(R.color.sex_female));

				break;
			case 7:
				holder.iv_sex01.setImageResource(R.drawable.ic_male);
				holder.iv_sex01.setColorFilter(context.getResources().getColor(R.color.sex_male));

				holder.iv_sex02.setImageResource(R.drawable.ic_female);
				holder.iv_sex02.setColorFilter(context.getResources().getColor(R.color.sex_female));

				break;
		}

		holder.tv_nome_local_lote.setText(lotesListFiltered.get(position).getLocal_lote().getNome_local());
		holder.tv_tipo_estrategia_lote.setText(getNome_tipo_estrategia(lotesListFiltered.get(position).getEstrategia_lote().getTipo_estrategia()));
	}

	@Override
	public int getItemCount() {
		return lotesListFiltered.size();
	}

	public List<Lote> getList()
	{
		return lotesListFiltered;
	}

	public void removeItem(int position) {
		lotesListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class LoteViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_lote, tv_qtd_animais_lote, tv_desc_complementar_lote, tv_nome_local_lote, tv_tipo_estrategia_lote;
		ImageView iv_sex01, iv_sex02, iv_local_lote, iv_qtd_animais_lote;
		RelativeLayout viewBackground;
		public RelativeLayout viewForeground;

		public LoteViewHolder(View itemView) {
			super(itemView);
			tv_nome_lote = itemView.findViewById(R.id.tv_nome_lote);
			tv_qtd_animais_lote = itemView.findViewById(R.id.tv_qtd_animais_lote);
			tv_desc_complementar_lote = itemView.findViewById(R.id.tv_desc_complementar_lote);
			tv_nome_local_lote = itemView.findViewById(R.id.tv_nome_local_lote);
			tv_tipo_estrategia_lote = itemView.findViewById(R.id.tv_tipo_estrategia_lote);
			iv_sex01 = itemView.findViewById(R.id.iv_sex01);
			iv_sex02 = itemView.findViewById(R.id.iv_sex02);
			iv_local_lote = itemView.findViewById(R.id.iv_local_lote);
			iv_qtd_animais_lote = itemView.findViewById(R.id.iv_qtd_animais_lote);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onLoteSelected(lotesListFiltered.get(getAdapterPosition()));
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
					lotesListFiltered = lotesList;
				} else {
					List<Lote> filteredList = new ArrayList<>();
					for (Lote row : lotesList)
					{
						if (row.getNome_lote().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					lotesListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = lotesListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				lotesListFiltered = (ArrayList<Lote>) filterResults.values;
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

	public interface LotesAdapterListener {
		void onLoteSelected(Lote lote);
	}
}