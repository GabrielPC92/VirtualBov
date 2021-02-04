package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lote_Medicamento implements Parcelable {
    private String numero_lote_medicamento, data_fabricacao_lote_medicamento, data_validade_lote_medicamento;

    public Lote_Medicamento() {
    }

    public String getNumero_lote_medicamento() {
        return numero_lote_medicamento;
    }

    public void setNumero_lote_medicamento(String numero_lote_medicamento) {
        this.numero_lote_medicamento = numero_lote_medicamento;
    }

    public String getData_fabricacao_lote_medicamento() {
        return data_fabricacao_lote_medicamento;
    }

    public void setData_fabricacao_lote_medicamento(String data_fabricacao_lote_medicamento) {
        this.data_fabricacao_lote_medicamento = data_fabricacao_lote_medicamento;
    }

    public String getData_validade_lote_medicamento() {
        return data_validade_lote_medicamento;
    }

    public void setData_validade_lote_medicamento(String data_validade_lote_medicamento) {
        this.data_validade_lote_medicamento = data_validade_lote_medicamento;
    }

    protected Lote_Medicamento(Parcel in) {
        numero_lote_medicamento = in.readString();
        data_fabricacao_lote_medicamento = in.readString();
        data_validade_lote_medicamento = in.readString();
    }

    public static final Creator<Lote_Medicamento> CREATOR = new Creator<Lote_Medicamento>() {
        @Override
        public Lote_Medicamento createFromParcel(Parcel in) {
            return new Lote_Medicamento(in);
        }

        @Override
        public Lote_Medicamento[] newArray(int size) {
            return new Lote_Medicamento[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numero_lote_medicamento);
        dest.writeString(data_fabricacao_lote_medicamento);
        dest.writeString(data_validade_lote_medicamento);
    }
}
