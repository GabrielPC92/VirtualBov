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
import br.com.virtualbovapp.model.ProtocoloSanitario;
import br.com.virtualbovapp.R;

public class ConsultaProtocoloSanitarioAdapter extends RecyclerView.Adapter<ConsultaProtocoloSanitarioAdapter.ProtocoloSanitarioViewHolder> implements Filterable {
	private final ArrayList<ProtocoloSanitario> protocolosSanitariosList;
	private List<ProtocoloSanitario> protocolosSanitariosListFiltered;
	private final ProtocolosAdapterListener listener;

	public ConsultaProtocoloSanitarioAdapter(ArrayList<ProtocoloSanitario> protocolos, ProtocolosAdapterListener listener) {
		this.protocolosSanitariosList = protocolos;
		this.protocolosSanitariosListFiltered = protocolos;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ProtocoloSanitarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulta_item_protocolo_sanitario, parent, false);
		return new ProtocoloSanitarioViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ProtocoloSanitarioViewHolder holder, final int position) {
		holder.tv_nome_protocolo.setText(protocolosSanitariosListFiltered.get(position).getNome_protocolo());

		String qtd;

		if (protocolosSanitariosListFiltered.get(position).getVacina_protocolo() != null) {
			qtd = " " + protocolosSanitariosListFiltered.get(position).getVacina_protocolo().size() + " ";
			holder.tv_qtd_vacina_protocolo.setText(qtd);
		}
		else
			holder.tv_qtd_vacina_protocolo.setText(" 0 ");

		holder.tv_desc_complementar_protocolo.setText(protocolosSanitariosListFiltered.get(position).getDesc_complementar_protocolo());

		if (protocolosSanitariosListFiltered.get(position).getMedicamento_protocolo() != null) {
			qtd = " " + protocolosSanitariosListFiltered.get(position).getMedicamento_protocolo().size() + " ";
			holder.tv_qtd_medicamento_protocolo.setText(qtd);
		}
		else
			holder.tv_qtd_medicamento_protocolo.setText(" 0 ");
	}

	@Override
	public int getItemCount() {
		return protocolosSanitariosListFiltered.size();
	}

	public List<ProtocoloSanitario> getList()
	{
		return protocolosSanitariosListFiltered;
	}

	public void removeItem(int position) {
		protocolosSanitariosListFiltered.remove(position);
		// notify the item removed by position
		// to perform recycler view delete animations
		// NOTE: don't call notifyDataSetChanged()
		notifyItemRemoved(position);
	}

	public class ProtocoloSanitarioViewHolder extends RecyclerView.ViewHolder {
		TextView tv_nome_protocolo, tv_qtd_vacina_protocolo, tv_desc_complementar_protocolo, tv_qtd_medicamento_protocolo;
		RelativeLayout viewBackground;
        public RelativeLayout viewForeground;

		public ProtocoloSanitarioViewHolder(View itemView) {
			super(itemView);
			tv_nome_protocolo = itemView.findViewById(R.id.tv_nome_protocolo);
			tv_qtd_vacina_protocolo = itemView.findViewById(R.id.tv_qtd_vacina_protocolo);
			tv_desc_complementar_protocolo = itemView.findViewById(R.id.tv_desc_complementar_protocolo);
			tv_qtd_medicamento_protocolo = itemView.findViewById(R.id.tv_qtd_medicamento_protocolo);
			viewBackground = itemView.findViewById(R.id.view_background);
			viewForeground = itemView.findViewById(R.id.view_foreground);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onProtocoloSelected(protocolosSanitariosListFiltered.get(getAdapterPosition()));
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
					protocolosSanitariosListFiltered = protocolosSanitariosList;
				} else {
					List<ProtocoloSanitario> filteredList = new ArrayList<>();
					for (ProtocoloSanitario row : protocolosSanitariosList)
					{
						if (row.getNome_protocolo().toLowerCase().contains(charString.toLowerCase()))
							filteredList.add(row);
					}

					protocolosSanitariosListFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = protocolosSanitariosListFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				protocolosSanitariosListFiltered = (ArrayList<ProtocoloSanitario>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public interface ProtocolosAdapterListener {
		void onProtocoloSelected(ProtocoloSanitario protocoloSanitario);
	}
}