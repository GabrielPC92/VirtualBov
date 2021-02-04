package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Raca implements Parcelable {
    private int id_raca, origem_raca;
    private String nome_raca, key_raca;
    private boolean padrao_raca;

    public Raca() {
    }

    public int getId_raca() {
        return id_raca;
    }

    public void setId_raca(int id_raca) {
        this.id_raca = id_raca;
    }

    public int getOrigem_raca() {
        return origem_raca;
    }

    public void setOrigem_raca(int origem_raca) {
        this.origem_raca = origem_raca;
    }

    public String getNome_raca() {
        return nome_raca;
    }

    public void setNome_raca(String nome_raca) {
        this.nome_raca = nome_raca;
    }

    public String getKey_raca() {
        return key_raca;
    }

    public void setKey_raca(String key_raca) {
        this.key_raca = key_raca;
    }

    public boolean isPadrao_raca() {
        return padrao_raca;
    }

    public void setPadrao_raca(boolean padrao_raca) {
        this.padrao_raca = padrao_raca;
    }

    protected Raca(Parcel in) {
        id_raca = in.readInt();
        origem_raca = in.readInt();
        nome_raca = in.readString();
        key_raca = in.readString();
        padrao_raca = in.readByte() != 0;
    }

    public static final Creator<Raca> CREATOR = new Creator<Raca>() {
        @Override
        public Raca createFromParcel(Parcel in) {
            return new Raca(in);
        }

        @Override
        public Raca[] newArray(int size) {
            return new Raca[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_raca);
        dest.writeInt(origem_raca);
        dest.writeString(nome_raca);
        dest.writeString(key_raca);
        dest.writeByte((byte) (padrao_raca ? 1 : 0));
    }
}
