package br.com.virtualbovapp.model;

public class TipoAnimaisLote {
    private int id_tipo;
    private String nome_tipo;

    public TipoAnimaisLote(int id_tipo, String nome_tipo) {
        this.id_tipo = id_tipo;
        this.nome_tipo = nome_tipo;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNome_tipo() {
        return nome_tipo;
    }

    public void setNome_tipo(String nome_tipo) {
        this.nome_tipo = nome_tipo;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nome_tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoAnimaisLote) {
            TipoAnimaisLote o = (TipoAnimaisLote) obj;
            if (o.getNome_tipo().equals(nome_tipo) && o.getId_tipo() == id_tipo)
                return true;
        }

        return false;
    }
}