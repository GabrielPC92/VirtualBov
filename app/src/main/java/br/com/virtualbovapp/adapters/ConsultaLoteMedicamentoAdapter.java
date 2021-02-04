package br.com.virtualbovapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import br.com.virtualbovapp.model.Lote_Medicamento;
import br.com.virtualbovapp.R;

public class ConsultaLoteMedicamentoAdapter extends RecyclerView.Adapter<ConsultaLoteMedicamentoAdapter.LoteMedicamentoViewHolder> {
	private ArrayList<Lote_Medicamento> lotes_medicamentosList;
	private MedicamentosAdapterListener listener;

	public ConsultaLoteMedicamentoAdapter(ArrayList<Lote_Medicamento> lote_medicamentos, MedicamentosAdapterListener listener) {
		this.lotes_medicamentosList = lote_medicamentos;
		this.listener = listener;
	}

	@Override
	public LoteMedicamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_lote_medicamento, parent, false);
		return new LoteMedicamentoViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final LoteMedicamentoViewHolder holder, final int position) {
		holder.tv_numero_lote_medicamento.setText(lotes_medicamentosList.get(position).getNumero_lote_medicamento());
		holder.tv_data_fabricacao_lote_medicamento.setText(lotes_medicamentosList.get(position).getData_fabricacao_lote_medicamento());
		holder.tv_data_validade_lote_medicamento.setText(lotes_medicamentosList.get(position).getData_validade_lote_medicamento());
	}

	@Override
	public int getItemCount() {
		return lotes_medicamentosList.size();
	}

	public List<Lote_Medicamento> getList()
	{
		return lotes_medicamentosList;
	}

	public void removeItem(int position) {
		lotes_medicamentosList.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class LoteMedicamentoViewHolder extends RecyclerView.ViewHolder {
		TextView tv_numero_lote_medicamento, tv_data_fabricacao_lote_medicamento, tv_data_validade_lote_medicamento;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public LoteMedicamentoViewHolder(View itemView) {
			super(itemView);
			tv_numero_lote_medicamento = itemView.findViewById(R.id.tv_numero_lote_medicamento);
			tv_data_fabricacao_lote_medicamento = itemView.findViewById(R.id.tv_data_fabricacao_lote_medicamento);
			tv_data_validade_lote_medicamento = itemView.findViewById(R.id.tv_data_validade_lote_medicamento);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onMedicamentoSelected(lotes_medicamentosList.get(getAdapterPosition()));
				}
			});
		}
	}

	public void updateList(ArrayList<Lote_Medicamento> newlist) {
		lotes_medicamentosList = newlist;
		this.notifyDataSetChanged();
	}

	public interface MedicamentosAdapterListener {
		void onMedicamentoSelected(Lote_Medicamento lote_medicamento);
	}
}