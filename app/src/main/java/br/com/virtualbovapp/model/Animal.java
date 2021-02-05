package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Animal implements Parcelable {
    private String key_animal, brinco_animal, nome_animal, data_nascimento_animal, sexo_animal, sisbov_animal, data_bnd_animal, nome_pai_animal,
            nome_mae_animal, desmame_animal, morte_animal;
    private Raca raca_animal;
    private float pesagem_animal;

    public Animal() {
    }

    public String getBrinco_animal() {
        return brinco_animal;
    }

    public void setBrinco_animal(String brinco_animal) {
        this.brinco_animal = brinco_animal;
    }

    public String getNome_animal() {
        return nome_animal;
    }

    public void setNome_animal(String nome_animal) {
        this.nome_animal = nome_animal;
    }

    public String getData_nascimento_animal() {
        return data_nascimento_animal;
    }

    public void setData_nascimento_animal(String data_nascimento_animal) {
        this.data_nascimento_animal = data_nascimento_animal;
    }

    public String getSexo_animal() {
        return sexo_animal;
    }

    public void setSexo_animal(String sexo_animal) {
        this.sexo_animal = sexo_animal;
    }

    public String getSisbov_animal() {
        return sisbov_animal;
    }

    public void setSisbov_animal(String sisbov_animal) {
        this.sisbov_animal = sisbov_animal;
    }

    public String getData_bnd_animal() {
        return data_bnd_animal;
    }

    public void setData_bnd_animal(String data_bnd_animal) {
        this.data_bnd_animal = data_bnd_animal;
    }

    public String getNome_pai_animal() {
        return nome_pai_animal;
    }

    public void setNome_pai_animal(String nome_pai_animal) {
        this.nome_pai_animal = nome_pai_animal;
    }

    public String getNome_mae_animal() {
        return nome_mae_animal;
    }

    public void setNome_mae_animal(String nome_mae_animal) {
        this.nome_mae_animal = nome_mae_animal;
    }

    public String getDesmame_animal() {
        return desmame_animal;
    }

    public void setDesmame_animal(String desmame_animal) {
        this.desmame_animal = desmame_animal;
    }

    public String getMorte_animal() {
        return morte_animal;
    }

    public void setMorte_animal(String morte_animal) {
        this.morte_animal = morte_animal;
    }

    public Raca getRaca_animal() {
        return raca_animal;
    }

    public void setRaca_animal(Raca raca_animal) {
        this.raca_animal = raca_animal;
    }

    public float getPesagem_animal() {
        return pesagem_animal;
    }

    public void setPesagem_animal(float pesagem_animal) {
        this.pesagem_animal = pesagem_animal;
    }

    public String getKey_animal() {
        return key_animal;
    }

    public void setKey_animal(String key_animal) {
        this.key_animal = key_animal;
    }

    protected Animal(Parcel in) {
        key_animal = in.readString();
        brinco_animal = in.readString();
        nome_animal = in.readString();
        data_nascimento_animal = in.readString();
        sexo_animal = in.readString();
        sisbov_animal = in.readString();
        data_bnd_animal = in.readString();
        nome_pai_animal = in.readString();
        nome_mae_animal = in.readString();
        desmame_animal = in.readString();
        morte_animal = in.readString();
        raca_animal = in.readParcelable(Raca.class.getClassLoader());
        pesagem_animal = in.readFloat();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key_animal);
        dest.writeString(brinco_animal);
        dest.writeString(nome_animal);
        dest.writeString(data_nascimento_animal);
        dest.writeString(sexo_animal);
        dest.writeString(sisbov_animal);
        dest.writeString(data_bnd_animal);
        dest.writeString(nome_pai_animal);
        dest.writeString(nome_mae_animal);
        dest.writeString(desmame_animal);
        dest.writeString(morte_animal);
        dest.writeParcelable(raca_animal, flags);
        dest.writeFloat(pesagem_animal);
    }
}
