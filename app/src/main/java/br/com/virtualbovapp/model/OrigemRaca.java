package br.com.virtualbovapp.model;

public class OrigemRaca {
    private int id_origem;
    private String nome_origem;

    public OrigemRaca(int id_origem, String nome_origem) {
        this.id_origem = id_origem;
        this.nome_origem = nome_origem;
    }

    public int getId_origem() {
        return id_origem;
    }

    public void setId_origem(int id_origem) {
        this.id_origem = id_origem;
    }

    public String getNome_origem() {
        return nome_origem;
    }

    public void setNome_origem(String nome_origem) {
        this.nome_origem = nome_origem;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nome_origem;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OrigemRaca){
            OrigemRaca o = (OrigemRaca)obj;
            if(o.getNome_origem().equals(nome_origem) && o.getId_origem() == id_origem)
                return true;
        }

        return false;
    }
}