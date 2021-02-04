package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Local implements Parcelable {
    private int capacidade_local;
    private String key_local, nome_local, desc_complementar_local;

    public Local() {
    }

    public int getCapacidade_local() {
        return capacidade_local;
    }

    public void setCapacidade_local(int capacidade_local) {
        this.capacidade_local = capacidade_local;
    }

    public String getNome_local() {
        return nome_local;
    }

    public void setNome_local(String nome_local) {
        this.nome_local = nome_local;
    }

    public String getDesc_complementar_local() {
        return desc_complementar_local;
    }

    public void setDesc_complementar_local(String desc_complementar_local) {
        this.desc_complementar_local = desc_complementar_local;
    }

    public String getKey_local() {
        return key_local;
    }

    public void setKey_local(String key_local) {
        this.key_local = key_local;
    }

    protected Local(Parcel in) {
        capacidade_local = in.readInt();
        key_local = in.readString();
        nome_local = in.readString();
        desc_complementar_local = in.readString();
    }

    public static final Creator<Local> CREATOR = new Creator<Local>() {
        @Override
        public Local createFromParcel(Parcel in) {
            return new Local(in);
        }

        @Override
        public Local[] newArray(int size) {
            return new Local[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(capacidade_local);
        dest.writeString(key_local);
        dest.writeString(nome_local);
        dest.writeString(desc_complementar_local);
    }
}
