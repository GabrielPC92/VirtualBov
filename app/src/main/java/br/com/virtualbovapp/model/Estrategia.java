package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Estrategia implements Parcelable {
    private int tipo_estrategia;
    private String key_estrategia, nome_estrategia , desc_complementar_estrategia;

    public Estrategia() {
    }

    public int getTipo_estrategia() {
        return tipo_estrategia;
    }

    public void setTipo_estrategia(int tipo_estrategia) {
        this.tipo_estrategia = tipo_estrategia;
    }

    public String getNome_estrategia() {
        return nome_estrategia;
    }

    public void setNome_estrategia(String nome_estrategia) {
        this.nome_estrategia = nome_estrategia;
    }

    public String getDesc_complementar_estrategia() {
        return desc_complementar_estrategia;
    }

    public void setDesc_complementar_estrategia(String desc_complementar_estrategia) {
        this.desc_complementar_estrategia = desc_complementar_estrategia;
    }

    public String getKey_estrategia() {
        return key_estrategia;
    }

    public void setKey_estrategia(String key_estrategia) {
        this.key_estrategia = key_estrategia;
    }

    protected Estrategia(Parcel in) {
        tipo_estrategia = in.readInt();
        nome_estrategia = in.readString();
        desc_complementar_estrategia = in.readString();
        key_estrategia = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tipo_estrategia);
        dest.writeString(nome_estrategia);
        dest.writeString(desc_complementar_estrategia);
        dest.writeString(key_estrategia);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Estrategia> CREATOR = new Creator<Estrategia>() {
        @Override
        public Estrategia createFromParcel(Parcel in) {
            return new Estrategia(in);
        }

        @Override
        public Estrategia[] newArray(int size) {
            return new Estrategia[size];
        }
    };
}
