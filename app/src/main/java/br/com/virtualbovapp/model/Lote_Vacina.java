package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lote_Vacina implements Parcelable {
    private String numero_lote_vacina, data_fabricacao_lote_vacina, data_validade_lote_vacina;

    public Lote_Vacina() {
    }

    public String getNumero_lote_vacina() {
        return numero_lote_vacina;
    }

    public void setNumero_lote_vacina(String numero_lote_vacina) {
        this.numero_lote_vacina = numero_lote_vacina;
    }

    public String getData_fabricacao_lote_vacina() {
        return data_fabricacao_lote_vacina;
    }

    public void setData_fabricacao_lote_vacina(String data_fabricacao_lote_vacina) {
        this.data_fabricacao_lote_vacina = data_fabricacao_lote_vacina;
    }

    public String getData_validade_lote_vacina() {
        return data_validade_lote_vacina;
    }

    public void setData_validade_lote_vacina(String data_validade_lote_vacina) {
        this.data_validade_lote_vacina = data_validade_lote_vacina;
    }

    protected Lote_Vacina(Parcel in) {
        numero_lote_vacina = in.readString();
        data_fabricacao_lote_vacina = in.readString();
        data_validade_lote_vacina = in.readString();
    }

    public static final Creator<Lote_Vacina> CREATOR = new Creator<Lote_Vacina>() {
        @Override
        public Lote_Vacina createFromParcel(Parcel in) {
            return new Lote_Vacina(in);
        }

        @Override
        public Lote_Vacina[] newArray(int size) {
            return new Lote_Vacina[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numero_lote_vacina);
        dest.writeString(data_fabricacao_lote_vacina);
        dest.writeString(data_validade_lote_vacina);
    }
}
