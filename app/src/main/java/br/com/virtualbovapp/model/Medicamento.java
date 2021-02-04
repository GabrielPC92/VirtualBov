package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Medicamento implements Parcelable {
    private String nome_medicamento, desc_complementar_medicamento, key_medicamento;
    private ArrayList<Lote_Medicamento> lote_medicamento;

    public Medicamento() {
    }

    public String getNome_medicamento() {
        return nome_medicamento;
    }

    public void setNome_medicamento(String nome_medicamento) {
        this.nome_medicamento = nome_medicamento;
    }

    public String getDesc_complementar_medicamento() {
        return desc_complementar_medicamento;
    }

    public void setDesc_complementar_medicamento(String desc_complementar_medicamento) {
        this.desc_complementar_medicamento = desc_complementar_medicamento;
    }

    public String getKey_medicamento() {
        return key_medicamento;
    }

    public void setKey_medicamento(String key_medicamento) {
        this.key_medicamento = key_medicamento;
    }

    public ArrayList<Lote_Medicamento> getLote_medicamento() {
        return lote_medicamento;
    }

    public void setLote_medicamento(ArrayList<Lote_Medicamento> lote_medicamento) {
        this.lote_medicamento = lote_medicamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome_medicamento);
        dest.writeString(desc_complementar_medicamento);
        dest.writeString(key_medicamento);
        dest.writeTypedList(lote_medicamento);
    }

    protected Medicamento(Parcel in) {
        nome_medicamento = in.readString();
        desc_complementar_medicamento = in.readString();
        key_medicamento = in.readString();
        lote_medicamento = in.createTypedArrayList(Lote_Medicamento.CREATOR);
    }

    public static final Creator<Medicamento> CREATOR = new Creator<Medicamento>() {
        @Override
        public Medicamento createFromParcel(Parcel in) {
            return new Medicamento(in);
        }

        @Override
        public Medicamento[] newArray(int size) {
            return new Medicamento[size];
        }
    };
}
