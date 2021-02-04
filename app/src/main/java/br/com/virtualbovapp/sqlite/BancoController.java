package br.com.virtualbovapp.sqlite;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.virtualbovapp.model.Estrategia;
import br.com.virtualbovapp.model.Animal;
import br.com.virtualbovapp.model.Local;
import br.com.virtualbovapp.model.Lote;
import br.com.virtualbovapp.model.Lote_Medicamento;
import br.com.virtualbovapp.model.Lote_Vacina;
import br.com.virtualbovapp.model.Medicamento;
import br.com.virtualbovapp.model.ProtocoloSanitario;
import br.com.virtualbovapp.model.Raca;
import br.com.virtualbovapp.model.Vacina;

public class BancoController {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context) {
        banco = new CriaBanco(context);
    }

    public Raca carregaRacaCodigo(int _id_raca) {
        Raca raca = new Raca();
        Cursor cursor;

        String[] campos = {"_id_raca", "_nome_raca", "_origem_raca"};
        String where = "_id_raca = " + _id_raca;

        db = banco.getReadableDatabase();
        cursor = db.query("raca", campos, where, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            raca = fillRaca(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return raca;
    }

    public Raca fillRaca(Cursor c) {
        Raca raca = new Raca();

        raca.setId_raca(c.getInt(0));
        raca.setNome_raca(c.getString(1));
        raca.setOrigem_raca(c.getInt(2));

        return raca;
    }

    public String criaAnimal(Animal animal) {
        ContentValues valores;

        long resultado;

        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("_brinco_animal", animal.getBrinco_animal());
        valores.put("_nome_animal", animal.getNome_animal());
        valores.put("_data_nascimento_animal", animal.getData_nascimento_animal());
        valores.put("_sexo_animal", animal.getSexo_animal());
        valores.put("_sisbov_animal", animal.getSisbov_animal());
        valores.put("_data_bnd_animal", animal.getData_bnd_animal());
        valores.put("_nome_pai_animal", animal.getNome_pai_animal());
        valores.put("_nome_mae_animal", animal.getNome_mae_animal());
        valores.put("_desmame_animal", animal.getDesmame_animal());
        valores.put("_morte_animal", animal.getMorte_animal());
        valores.put("_id_raca_animal", animal.getRaca_animal().getId_raca());
        valores.put("_pesagem_animal", animal.getPesagem_animal());

        resultado = db.insert("animal", null, valores);

        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "Registro inserido com sucesso";
    }

    public String alteraAnimal(Animal animal) {
        ContentValues valores;
        long resultado;

        String where = "_brinco_animal = '" + animal.getBrinco_animal() + "'";

        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("_nome_animal", animal.getNome_animal());
        valores.put("_data_nascimento_animal", animal.getData_nascimento_animal());
        valores.put("_sexo_animal", animal.getSexo_animal());
        valores.put("_sisbov_animal", animal.getSisbov_animal());
        valores.put("_data_bnd_animal", animal.getData_bnd_animal());
        valores.put("_nome_pai_animal", animal.getNome_pai_animal());
        valores.put("_nome_mae_animal", animal.getNome_mae_animal());
        valores.put("_desmame_animal", animal.getDesmame_animal());
        valores.put("_morte_animal", animal.getMorte_animal());
        valores.put("_id_raca_animal", animal.getRaca_animal().getId_raca());
        valores.put("_pesagem_animal", animal.getPesagem_animal());

        resultado = db.update("animal", valores, where, null);

        db.close();

        if (resultado == -1)
            return "Erro ao atualizar registro";
        else
            return "Registro atualizado com sucesso";
    }

    public void deletaAnimal(String _brinco_animal) {
        String where = "_brinco_animal = '" + _brinco_animal + "'";

        db = banco.getReadableDatabase();
        db.delete("animal", where, null);
        db.close();
    }

    public ArrayList<Animal> carregaAnimais() {
        ArrayList<Animal> list = new ArrayList<Animal>();
        Cursor cursor;
        Animal animal;

        String[] campos = {"_brinco_animal", "_nome_animal", "_data_nascimento_animal", "_sexo_animal", "_sisbov_animal",
        "_data_bnd_animal", "_nome_pai_animal", "_nome_mae_animal", "_desmame_animal", "_morte_animal", "_id_raca_animal",
        "_pesagem_animal"};
        String order = "_brinco_animal";

        db = banco.getReadableDatabase();
        cursor = db.query("animal", campos, null, null, null, null, order, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            animal = fillAnimal(cursor);
            list.add(animal);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public Animal fillAnimal(Cursor c) {
        Animal animal = new Animal();

        animal.setBrinco_animal(c.getString(0));
        animal.setNome_animal(c.getString(1));
        animal.setData_nascimento_animal(c.getString(2));
        animal.setSexo_animal(c.getString(3));
        animal.setSisbov_animal(c.getString(4));
        animal.setData_bnd_animal(c.getString(5));
        animal.setNome_pai_animal(c.getString(6));
        animal.setNome_mae_animal(c.getString(7));
        animal.setDesmame_animal(c.getString(8));
        animal.setMorte_animal(c.getString(9));
        animal.setRaca_animal(carregaRacaCodigo(c.getInt(10)));
        animal.setPesagem_animal(c.getInt(11));

        return animal;
    }
}
