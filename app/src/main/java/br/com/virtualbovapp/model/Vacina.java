package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Vacina implements Parcelable {
    private String nome_vacina, desc_complementar_vacina, key_vacina;
    private ArrayList<Lote_Vacina> lote_vacina;

    public Vacina() {
    }

    public String getNome_vacina() {
        return nome_vacina;
    }

    public void setNome_vacina(String nome_vacina) {
        this.nome_vacina = nome_vacina;
    }

    public String getDesc_complementar_vacina() {
        return desc_complementar_vacina;
    }

    public void setDesc_complementar_vacina(String desc_complementar_vacina) {
        this.desc_complementar_vacina = desc_complementar_vacina;
    }

    public String getKey_vacina() {
        return key_vacina;
    }

    public void setKey_vacina(String key_vacina) {
        this.key_vacina = key_vacina;
    }

    public ArrayList<Lote_Vacina> getLote_vacina() {
        return lote_vacina;
    }

    public void setLote_vacina(ArrayList<Lote_Vacina> lote_vacina) {
        this.lote_vacina = lote_vacina;
    }

    protected Vacina(Parcel in) {
        nome_vacina = in.readString();
        desc_complementar_vacina = in.readString();
        key_vacina = in.readString();
        lote_vacina = in.createTypedArrayList(Lote_Vacina.CREATOR);
    }

    public static final Creator<Vacina> CREATOR = new Creator<Vacina>() {
        @Override
        public Vacina createFromParcel(Parcel in) {
            return new Vacina(in);
        }

        @Override
        public Vacina[] newArray(int size) {
            return new Vacina[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome_vacina);
        dest.writeString(desc_complementar_vacina);
        dest.writeString(key_vacina);
        dest.writeTypedList(lote_vacina);
    }
}
