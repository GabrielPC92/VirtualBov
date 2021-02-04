package br.com.virtualbovapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ProtocoloSanitario implements Parcelable {
    private int idade_protocolo;
    private String key_protocolo, nome_protocolo, desc_complementar_protocolo, outros_procedimentos_protocolo;
    private ArrayList<Vacina> vacina_protocolo;
    private ArrayList<Medicamento> medicamento_protocolo;

    public ProtocoloSanitario() {
    }

    public int getIdade_protocolo() {
        return idade_protocolo;
    }

    public void setIdade_protocolo(int idade_protocolo) {
        this.idade_protocolo = idade_protocolo;
    }

    public String getNome_protocolo() {
        return nome_protocolo;
    }

    public void setNome_protocolo(String nome_protocolo) {
        this.nome_protocolo = nome_protocolo;
    }

    public String getDesc_complementar_protocolo() {
        return desc_complementar_protocolo;
    }

    public void setDesc_complementar_protocolo(String desc_complementar_protocolo) {
        this.desc_complementar_protocolo = desc_complementar_protocolo;
    }

    public String getOutros_procedimentos_protocolo() {
        return outros_procedimentos_protocolo;
    }

    public void setOutros_procedimentos_protocolo(String outros_procedimentos_protocolo) {
        this.outros_procedimentos_protocolo = outros_procedimentos_protocolo;
    }

    public ArrayList<Vacina> getVacina_protocolo() {
        return vacina_protocolo;
    }

    public void setVacina_protocolo(ArrayList<Vacina> vacina_protocolo) {
        this.vacina_protocolo = vacina_protocolo;
    }

    public ArrayList<Medicamento> getMedicamento_protocolo() {
        return medicamento_protocolo;
    }

    public void setMedicamento_protocolo(ArrayList<Medicamento> medicamento_protocolo) {
        this.medicamento_protocolo = medicamento_protocolo;
    }

    public String getKey_protocolo() {
        return key_protocolo;
    }

    public void setKey_protocolo(String key_protocolo) {
        this.key_protocolo = key_protocolo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idade_protocolo);
        dest.writeString(key_protocolo);
        dest.writeString(nome_protocolo);
        dest.writeString(desc_complementar_protocolo);
        dest.writeString(outros_procedimentos_protocolo);
        dest.writeTypedList(vacina_protocolo);
        dest.writeTypedList(medicamento_protocolo);
    }

    protected ProtocoloSanitario(Parcel in) {
        idade_protocolo = in.readInt();
        key_protocolo = in.readString();
        nome_protocolo = in.readString();
        desc_complementar_protocolo = in.readString();
        outros_procedimentos_protocolo = in.readString();
        vacina_protocolo = in.createTypedArrayList(Vacina.CREATOR);
        medicamento_protocolo = in.createTypedArrayList(Medicamento.CREATOR);
    }

    public static final Creator<ProtocoloSanitario> CREATOR = new Creator<ProtocoloSanitario>() {
        @Override
        public ProtocoloSanitario createFromParcel(Parcel in) {
            return new ProtocoloSanitario(in);
        }

        @Override
        public ProtocoloSanitario[] newArray(int size) {
            return new ProtocoloSanitario[size];
        }
    };
}
