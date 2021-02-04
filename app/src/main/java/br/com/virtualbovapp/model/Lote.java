package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lote implements Parcelable {
    private int tipo_animais_lote, periodo_permanencia_lote, tipo_periodo_lote;
    private String key_lote, nome_lote, desc_complementar_lote;
    private Local local_lote;
    private Estrategia estrategia_lote;

    public Lote() {
    }

    public int getTipo_animais_lote() {
        return tipo_animais_lote;
    }

    public void setTipo_animais_lote(int tipo_animais_lote) {
        this.tipo_animais_lote = tipo_animais_lote;
    }

    public int getPeriodo_permanencia_lote() {
        return periodo_permanencia_lote;
    }

    public void setPeriodo_permanencia_lote(int perido_permanencia_lote) {
        this.periodo_permanencia_lote = perido_permanencia_lote;
    }

    public int getTipo_periodo_lote() {
        return tipo_periodo_lote;
    }

    public void setTipo_periodo_lote(int tipo_periodo_lote) {
        this.tipo_periodo_lote = tipo_periodo_lote;
    }

    public String getNome_lote() {
        return nome_lote;
    }

    public void setNome_lote(String nome_lote) {
        this.nome_lote = nome_lote;
    }

    public String getDesc_complementar_lote() {
        return desc_complementar_lote;
    }

    public void setDesc_complementar_lote(String desc_complementar_lote) {
        this.desc_complementar_lote = desc_complementar_lote;
    }

    public Local getLocal_lote() {
        return local_lote;
    }

    public void setLocal_lote(Local local_lote) {
        this.local_lote = local_lote;
    }

    public Estrategia getEstrategia_lote() {
        return estrategia_lote;
    }

    public void setEstrategia_lote(Estrategia estrategia_lote) {
        this.estrategia_lote = estrategia_lote;
    }

    public String getKey_lote() {
        return key_lote;
    }

    public void setKey_lote(String key_lote) {
        this.key_lote = key_lote;
    }

    protected Lote(Parcel in) {
        tipo_animais_lote = in.readInt();
        periodo_permanencia_lote = in.readInt();
        tipo_periodo_lote = in.readInt();
        nome_lote = in.readString();
        desc_complementar_lote = in.readString();
        key_lote = in.readString();
        local_lote = in.readParcelable(Local.class.getClassLoader());
        estrategia_lote = in.readParcelable(Estrategia.class.getClassLoader());
    }

    public static final Creator<Lote> CREATOR = new Creator<Lote>() {
        @Override
        public Lote createFromParcel(Parcel in) {
            return new Lote(in);
        }

        @Override
        public Lote[] newArray(int size) {
            return new Lote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tipo_animais_lote);
        dest.writeInt(periodo_permanencia_lote);
        dest.writeInt(tipo_periodo_lote);
        dest.writeString(nome_lote);
        dest.writeString(desc_complementar_lote);
        dest.writeString(key_lote);
        dest.writeParcelable(local_lote, flags);
        dest.writeParcelable(estrategia_lote, flags);
    }
}
