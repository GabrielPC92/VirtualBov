package br.com.virtualbovapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CriaBanco extends SQLiteOpenHelper
{
    private static final String NOME_BANCO = "banco.db";
    private static final int VERSAO = 1;
    private final String CNT_LOG = "AcessoBD";
    private String sql;

    public CriaBanco(Context context)
    {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(CNT_LOG, "Iniciando a criação das tabelas");

        try {
            db.beginTransaction();

            sql	=  "create table raca(_id_raca integer primary key autoincrement, _nome_raca text, _origem_raca integer);";
            db.execSQL(sql);

            sql	=  "create table animal(_brinco_animal text primary key, _nome_animal text, _data_nascimento_animal text,";
            sql	+= " _sexo_animal text, _sisbov_animal text, _data_bnd_animal text, _nome_pai_animal text, _nome_mae_animal text,";
            sql += " _id_raca_animal integer, _pesagem_animal real, _desmame_animal text, _morte_animal text,";
            sql += " FOREIGN KEY(_id_raca_animal) REFERENCES raca (_id_raca) ON DELETE RESTRICT ON UPDATE CASCADE);";
            db.execSQL(sql);

            sql	=  "create table local(_id_local integer primary key autoincrement, _nome_local text, _desc_complementar_local text, _capacidade_local integer);";
            db.execSQL(sql);

            sql	=  "create table estrategia(_id_estrategia integer primary key autoincrement, _nome_estrategia text, _desc_complementar_estrategia text, _tipo_estrategia integer);";
            db.execSQL(sql);

            sql	=  "create table lote(_id_lote integer primary key autoincrement, _nome_lote text, _desc_complementar_lote text, _tipo_animais_lote integer, _periodo_permanencia_lote integer, _tipo_periodo_lote integer, _id_estrategia_lote integer, _id_local_lote integer,";
            sql += " FOREIGN KEY(_id_estrategia_lote) REFERENCES estrategia (_id_estrategia) ON DELETE RESTRICT ON UPDATE CASCADE,";
            sql += " FOREIGN KEY(_id_local_lote) REFERENCES local (_id_local) ON DELETE RESTRICT ON UPDATE CASCADE);";
            db.execSQL(sql);

            sql	=  "create table vacina(_id_vacina integer primary key autoincrement, _nome_vacina text, _desc_complementar_vacina text);";
            db.execSQL(sql);

            sql	=  "create table lote_vacina(_id_vacina integer, _numero_lote_vacina text, _data_fabricacao_lote_vacina text, _data_validade_lote_vacina text,";
            sql += " PRIMARY KEY (_id_vacina, _numero_lote_vacina));";
            db.execSQL(sql);

            sql	=  "create table medicamento(_id_medicamento integer primary key autoincrement, _nome_medicamento text, _desc_complementar_medicamento text);";
            db.execSQL(sql);

            sql	=  "create table lote_medicamento(_id_medicamento integer, _numero_lote_medicamento text, _data_fabricacao_lote_medicamento text, _data_validade_lote_medicamento text,";
            sql += " PRIMARY KEY (_id_medicamento, _numero_lote_medicamento));";
            db.execSQL(sql);

            sql	=  "create table protocoloSanitario(_id_protocolo integer primary key autoincrement, _nome_protocolo text, _desc_complementar_protocolo text, _idade_protocolo integer, _id_vacina_protocolo integer, _id_medicamento_protocolo integer, _outros_procedimentos_protocolo text,";
            sql += " FOREIGN KEY(_id_vacina_protocolo) REFERENCES vacina (_id_vacina) ON DELETE RESTRICT ON UPDATE CASCADE,";
            sql += " FOREIGN KEY(_id_medicamento_protocolo) REFERENCES medicamento (_id_medicamento) ON DELETE RESTRICT ON UPDATE CASCADE);";
            db.execSQL(sql);

//            sql	=  "create table animal(_id_layout integer primary key autoincrement, _nome_layout text, _lotacao_layout integer, _layout_01 integer, _layout_02 integer,";
//            sql += "_layout_03 integer, _layout_04 integer, _layout_05 integer, _layout_06 integer, _layout_07 integer, _layout_08 integer, _layout_09 integer,";
//            sql += "_layout_10 integer, _layout_11 integer, _layout_12 integer, _layout_13 integer, _layout_14 integer, _layout_15 integer, _layout_16 integer,";
//            sql += "_layout_17 integer, _layout_18 integer, _layout_19 integer, _layout_20 integer, _layout_21 integer, _layout_22 integer, _layout_23 integer,";
//            sql += "_layout_24 integer, _layout_25 integer, _layout_26 integer, _layout_27 integer, _layout_28 integer, _layout_29 integer, _layout_30 integer,";
//            sql += "_layout_31 integer, _layout_32 integer, _layout_33 integer, _layout_34 integer, _layout_35 integer, _layout_36 integer, _layout_37 integer,";
//            sql += "_layout_38 integer, _layout_39 integer, _layout_40 integer, _layout_41 integer, _layout_42 integer, _layout_43 integer, _layout_44 integer,";
//            sql += "_layout_45 integer, _layout_46 integer, _layout_47 integer, _layout_48 integer, _layout_49 integer, _layout_50 integer, _layout_51 integer, _layout_52 integer);";
//            db.execSQL(sql);
//
//            sql	=  "create table veiculo(_id_veiculo integer primary key autoincrement, _nome_veiculo text, _id_layout_veiculo integer,";
//            sql += " FOREIGN KEY(_id_layout_veiculo) REFERENCES layout (_id_layout) ON DELETE RESTRICT ON UPDATE CASCADE);";
//            db.execSQL(sql);
//
//            sql	=  "create table viagem(_id_viagem integer primary key autoincrement, _nome_viagem text, _dados_viagem text, _data_saida_viagem text, _data_chegada_viagem text);";
//            db.execSQL(sql);
//
//            sql	=  "create table veiculos_viagem(_id_viagem integer, _id_veiculo_viagem integer,";
//            sql += " PRIMARY KEY (_id_viagem, _id_veiculo_viagem));";
//            db.execSQL(sql);
//
//            sql	=  "create table reserva(_id_reserva integer primary key autoincrement, _cpf_titular_reserva text, _id_viagem_reserva integer,";
//            sql += " FOREIGN KEY(_cpf_titular_reserva) REFERENCES passageiro (_cpf_passageiro) ON DELETE RESTRICT ON UPDATE CASCADE,";
//            sql += " FOREIGN KEY(_id_viagem_reserva) REFERENCES viagem (_id_viagem) ON DELETE RESTRICT ON UPDATE CASCADE);";
//            db.execSQL(sql);
//
//            sql	=  "create table reserva_passageiro(_id_viagem_reserva integer, _cpf_passagerio_reserva text, _id_reserva integer, _id_veiculo_reserva integer, _num_poltrona_reserva integer, _cpf_passagerio_colo text,";
//            sql += " PRIMARY KEY (_id_viagem_reserva, _cpf_passagerio_reserva)) ;";
//            db.execSQL(sql);
//
//            /*layout padrão*/
//            sql	=  "INSERT INTO layout(_id_layout, _nome_layout, _lotacao_layout, _layout_01, _layout_02,";
//            sql += "_layout_03, _layout_04, _layout_05, _layout_06, _layout_07, _layout_08, _layout_09,";
//            sql += "_layout_10, _layout_11, _layout_12, _layout_13, _layout_14, _layout_15, _layout_16,";
//            sql += "_layout_17, _layout_18, _layout_19, _layout_20, _layout_21, _layout_22, _layout_23,";
//            sql += "_layout_24, _layout_25, _layout_26, _layout_27, _layout_28, _layout_29, _layout_30,";
//            sql += "_layout_31, _layout_32, _layout_33, _layout_34, _layout_35, _layout_36, _layout_37,";
//            sql += "_layout_38, _layout_39, _layout_40, _layout_41, _layout_42, _layout_43, _layout_44,";
//            sql += "_layout_45, _layout_46, _layout_47, _layout_48, _layout_49, _layout_50, _layout_51, _layout_52)";
//            sql += " VALUES (1, 'LAYOUT ÔNIBUS', 52, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,";
//            sql += " 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,";
//            sql += " 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO layout(_id_layout, _nome_layout, _lotacao_layout, _layout_01, _layout_02,";
//            sql += "_layout_03, _layout_04, _layout_05, _layout_06, _layout_07, _layout_08, _layout_09,";
//            sql += "_layout_10, _layout_11, _layout_12, _layout_13, _layout_14, _layout_15, _layout_16,";
//            sql += "_layout_17, _layout_18, _layout_19, _layout_20, _layout_21, _layout_22, _layout_23,";
//            sql += "_layout_24, _layout_25, _layout_26, _layout_27, _layout_28, _layout_29, _layout_30,";
//            sql += "_layout_31, _layout_32, _layout_33, _layout_34, _layout_35, _layout_36, _layout_37,";
//            sql += "_layout_38, _layout_39, _layout_40, _layout_41, _layout_42, _layout_43, _layout_44,";
//            sql += "_layout_45, _layout_46, _layout_47, _layout_48, _layout_49, _layout_50, _layout_51, _layout_52)";
//            sql += " VALUES (2, 'LAYOUT VAN', 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 0, 0,";
//            sql += " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,";
//            sql += " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);";
//            db.execSQL(sql);
//
//            /*Passageiros*/
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('356.188.268-44', 'GABRIEL PUPIN CHENCI', '11/11/1992');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('421.076.348-90', 'LETÍCIA DA SILVA PIZA CHENCI', '25/01/1994');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('368.880.490-27', 'MATHEUS PUPIN CHENCI', '27/12/1995');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('758.924.520-85', 'MARCIA MARIA PUPIN CHENCI', '24/08/1965');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('048.272.480-36', 'JOÃO DONIZETE CHENCI', '10/02/1964');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('194.366.910-41', 'GUILHERME DA SILVA PIZA', '17/12/1989');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('717.996.470-05', 'ANTÔNIO DONIZETE PIZA', '02/11/1963');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO passageiro(_cpf_passageiro, _nome_passageiro, _data_nascimento_passageiro)";
//            sql += " VALUES ('094.263.630-92', 'MARIA APARECIDA DA SILVA PIZA', '14/03/1965');";
//            db.execSQL(sql);
//
//            /*Veículos*/
//            sql	=  "INSERT INTO veiculo(_id_veiculo, _nome_veiculo, _id_layout_veiculo)";
//            sql += " VALUES (1, 'ÔNIBUS NOGUEIRA (FGD-4569)', 1);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO veiculo(_id_veiculo, _nome_veiculo, _id_layout_veiculo)";
//            sql += " VALUES (2, 'VAN NOGUEIRA (GDO-0946)', 2);";
//            db.execSQL(sql);
//
//            /*Viagens*/
//            sql	=  "INSERT INTO viagem(_id_viagem, _nome_viagem, _dados_viagem, _data_saida_viagem, _data_chegada_viagem)";
//            sql += " VALUES (1, 'GRAMADO', 'Dados Viagem Teste', '01/09/2020 22:30', '07/09/2020 00:30');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO viagem(_id_viagem, _nome_viagem, _dados_viagem, _data_saida_viagem, _data_chegada_viagem)";
//            sql += " VALUES (2, 'CALDAS NOVAS', 'Dados Viagem Teste', '02/10/2020 23:30', '08/10/2020 22:30');";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO viagem(_id_viagem, _nome_viagem, _dados_viagem, _data_saida_viagem, _data_chegada_viagem)";
//            sql += " VALUES (3, 'BERTIOGA', 'Dados Viagem Teste', '03/12/2020 00:30', '09/12/2020 23:30');";
//            db.execSQL(sql);
//
//            /*Veículos das viagens*/
//            sql	=  "INSERT INTO veiculos_viagem(_id_viagem, _id_veiculo_viagem)";
//            sql += " VALUES (1, 1);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO veiculos_viagem(_id_viagem, _id_veiculo_viagem)";
//            sql += " VALUES (1, 2);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO veiculos_viagem(_id_viagem, _id_veiculo_viagem)";
//            sql += " VALUES (2, 2);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO veiculos_viagem(_id_viagem, _id_veiculo_viagem)";
//            sql += " VALUES (3, 1);";
//            db.execSQL(sql);
//
//            sql	=  "INSERT INTO veiculos_viagem(_id_viagem, _id_veiculo_viagem)";
//            sql += " VALUES (3, 2);";
//            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (1, 'ABERDEEN ANGUS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (2, 'AYRSHIRE', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (3, 'BLOND D AQUITAINE', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (4, 'BRAHMAN', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (5, 'BUBALINO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (6, 'CANCHIM', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (7, 'CARACU', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (8, 'CHAROLÊS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (9, 'CHIANINA', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (10, 'CRIOULO LAGEANO', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (11, 'DEVON', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (12, 'FRISIA', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (13, 'GELBVIEH', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (14, 'GIR', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (15, 'GIROLANDO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (16, 'GUZOLANDO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (17, 'GUZONEL', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (18, 'HARIANA', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (19, 'HEREFORD', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (20, 'HOLANDÊS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (21, 'HOLSTEIN', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (22, 'INDUBRASIL', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (23, 'JERSEY', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (24, 'JERSOLANDO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (25, 'LIMOUSIN', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (26, 'MARCHIGIANA', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (27, 'MESTIÇO', 3);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (28, 'MOCHO NACIONAL', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (29, 'NELOGIR', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (30, 'NELORE', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (31, 'NELORANDO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (32, 'NGUNI', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (33, 'NORMANDOS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (34, 'OUTRO', 3);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (35, 'PANTANEIRO', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (36, 'PARDO HOLANDÊS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (37, 'PARDO SUIÇO', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (38, 'PIEMONTÊS', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (39, 'PITANGUEIRAS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (40, 'PURUNÃ', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (41, 'RED ANGUS', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (42, 'SENEPOL', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (43, 'SHORTHORN', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (44, 'SINDOLANDO', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (45, 'SINJER', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (46, 'SIMBRAS', 2);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (47, 'SIMENTAL', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (48, 'SINDI', 1);";
            db.execSQL(sql);

            sql	=  "INSERT INTO raca(_id_raca, _nome_raca, _origem_raca)";
            sql += " VALUES (49, 'TABAPUÃ', 1);";
            db.execSQL(sql);

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }

        Log.i(CNT_LOG, "Tabelas criadas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if ((newVersion - oldVersion) > 0) {
            db.execSQL("DROP TABLE IF EXISTS passageiro; DROP TABLE IF EXISTS layout; DROP TABLE IF EXISTS veiculo; DROP TABLE IF EXISTS viagem; DROP TABLE IF EXISTS reserva;");
            onCreate(db);
        }
    }
}