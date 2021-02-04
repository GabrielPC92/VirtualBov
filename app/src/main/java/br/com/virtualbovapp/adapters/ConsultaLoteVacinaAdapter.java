package br.com.virtualbovapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import br.com.virtualbovapp.model.Lote_Vacina;
import br.com.virtualbovapp.R;

public class ConsultaLoteVacinaAdapter extends RecyclerView.Adapter<ConsultaLoteVacinaAdapter.LoteVacinaViewHolder> {
	private ArrayList<Lote_Vacina> lotes_vacinasList;
	private VacinasAdapterListener listener;

	public ConsultaLoteVacinaAdapter(ArrayList<Lote_Vacina> lote_vacinas, VacinasAdapterListener listener) {
		this.lotes_vacinasList = lote_vacinas;
		this.listener = listener;
	}

	@Override
	public LoteVacinaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_lote_vacina, parent, false);
		return new LoteVacinaViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final LoteVacinaViewHolder holder, final int position) {
		holder.tv_numero_lote_vacina.setText(lotes_vacinasList.get(position).getNumero_lote_vacina());
		holder.tv_data_fabricacao_lote_vacina.setText(lotes_vacinasList.get(position).getData_fabricacao_lote_vacina());
		holder.tv_data_validade_lote_vacina.setText(lotes_vacinasList.get(position).getData_validade_lote_vacina());
	}

	@Override
	public int getItemCount() {
		return lotes_vacinasList.size();
	}

	public List<Lote_Vacina> getList()
	{
		return lotes_vacinasList;
	}

	public void removeItem(int position) {
		lotes_vacinasList.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class LoteVacinaViewHolder extends RecyclerView.ViewHolder {
		TextView tv_numero_lote_vacina, tv_data_fabricacao_lote_vacina, tv_data_validade_lote_vacina;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public LoteVacinaViewHolder(View itemView) {
			super(itemView);
			tv_numero_lote_vacina = itemView.findViewById(R.id.tv_numero_lote_vacina);
			tv_data_fabricacao_lote_vacina = itemView.findViewById(R.id.tv_data_fabricacao_lote_vacina);
			tv_data_validade_lote_vacina = itemView.findViewById(R.id.tv_data_validade_lote_vacina);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onVacinaSelected(lotes_vacinasList.get(getAdapterPosition()));
				}
			});
		}
	}

	public void updateList(ArrayList<Lote_Vacina> newlist) {
		lotes_vacinasList = newlist;
		this.notifyDataSetChanged();
	}

	public interface VacinasAdapterListener {
		void onVacinaSelected(Lote_Vacina lote_vacina);
	}
}