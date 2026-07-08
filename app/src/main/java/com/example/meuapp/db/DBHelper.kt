package com.example.meuapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.meuapp.agenda.Contato
import com.example.meuapp.despesas.Despesa
import com.example.meuapp.notas.Nota

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "meuapp.db"
        private const val DATABASE_VERSION = 2

        // Notas
        private const val TABLE_NOTAS = "notas"
        private const val COL_NOTA_ID = "_id"
        private const val COL_NOTA_TITULO = "titulo"
        private const val COL_NOTA_CONTEUDO = "conteudo"
        private const val COL_NOTA_DATA = "data_hora"

        // Contatos
        private const val TABLE_CONTATOS = "contatos"
        private const val COL_CONTATO_ID = "_id"
        private const val COL_CONTATO_NOME = "nome"
        private const val COL_CONTATO_EMAIL = "email"
        private const val COL_CONTATO_TELEFONE = "telefone"
        private const val COL_CONTATO_OBS = "observacoes"

        // Despesas
        private const val TABLE_DESPESAS = "despesas"
        private const val COL_DESPESA_ID = "_id"
        private const val COL_DESPESA_DESCRICAO = "descricao"
        private const val COL_DESPESA_VALOR = "valor"
        private const val COL_DESPESA_DATA = "data_hora"
        private const val COL_DESPESA_MES_ANO = "mes_ano" // formato "MM/yyyy", ex: "07/2026"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE_NOTAS (
                $COL_NOTA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOTA_TITULO TEXT,
                $COL_NOTA_CONTEUDO TEXT,
                $COL_NOTA_DATA TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE $TABLE_CONTATOS (
                $COL_CONTATO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTATO_NOME TEXT,
                $COL_CONTATO_EMAIL TEXT,
                $COL_CONTATO_TELEFONE TEXT,
                $COL_CONTATO_OBS TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE $TABLE_DESPESAS (
                $COL_DESPESA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DESPESA_DESCRICAO TEXT,
                $COL_DESPESA_VALOR REAL,
                $COL_DESPESA_DATA TEXT,
                $COL_DESPESA_MES_ANO TEXT
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Adiciona a coluna de mês/ano sem apagar despesas já cadastradas
            try {
                db.execSQL("ALTER TABLE $TABLE_DESPESAS ADD COLUMN $COL_DESPESA_MES_ANO TEXT DEFAULT ''")
            } catch (e: Exception) {
                // Coluna já existe ou tabela não existe ainda: ignora
            }
        }
    }

    // ---------- NOTAS ----------

    fun inserirNota(titulo: String, conteudo: String, dataHora: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTA_TITULO, titulo)
            put(COL_NOTA_CONTEUDO, conteudo)
            put(COL_NOTA_DATA, dataHora)
        }
        return db.insert(TABLE_NOTAS, null, values)
    }

    fun atualizarNota(id: Long, titulo: String, conteudo: String, dataHora: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTA_TITULO, titulo)
            put(COL_NOTA_CONTEUDO, conteudo)
            put(COL_NOTA_DATA, dataHora)
        }
        db.update(TABLE_NOTAS, values, "$COL_NOTA_ID=?", arrayOf(id.toString()))
    }

    fun excluirNota(id: Long) {
        writableDatabase.delete(TABLE_NOTAS, "$COL_NOTA_ID=?", arrayOf(id.toString()))
    }

    fun listarNotas(): List<Nota> {
        val lista = mutableListOf<Nota>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NOTAS, null, null, null, null, null, "$COL_NOTA_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Nota(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_NOTA_ID)),
                        titulo = it.getString(it.getColumnIndexOrThrow(COL_NOTA_TITULO)) ?: "",
                        conteudo = it.getString(it.getColumnIndexOrThrow(COL_NOTA_CONTEUDO)) ?: "",
                        dataHora = it.getString(it.getColumnIndexOrThrow(COL_NOTA_DATA)) ?: ""
                    )
                )
            }
        }
        return lista
    }

    // ---------- CONTATOS ----------

    fun inserirContato(nome: String, email: String, telefone: String, obs: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CONTATO_NOME, nome)
            put(COL_CONTATO_EMAIL, email)
            put(COL_CONTATO_TELEFONE, telefone)
            put(COL_CONTATO_OBS, obs)
        }
        return db.insert(TABLE_CONTATOS, null, values)
    }

    fun atualizarContato(id: Long, nome: String, email: String, telefone: String, obs: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CONTATO_NOME, nome)
            put(COL_CONTATO_EMAIL, email)
            put(COL_CONTATO_TELEFONE, telefone)
            put(COL_CONTATO_OBS, obs)
        }
        db.update(TABLE_CONTATOS, values, "$COL_CONTATO_ID=?", arrayOf(id.toString()))
    }

    fun excluirContato(id: Long) {
        writableDatabase.delete(TABLE_CONTATOS, "$COL_CONTATO_ID=?", arrayOf(id.toString()))
    }

    fun listarContatos(): List<Contato> {
        val lista = mutableListOf<Contato>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTATOS, null, null, null, null, null, "$COL_CONTATO_NOME ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Contato(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_CONTATO_ID)),
                        nome = it.getString(it.getColumnIndexOrThrow(COL_CONTATO_NOME)) ?: "",
                        email = it.getString(it.getColumnIndexOrThrow(COL_CONTATO_EMAIL)) ?: "",
                        telefone = it.getString(it.getColumnIndexOrThrow(COL_CONTATO_TELEFONE)) ?: "",
                        observacoes = it.getString(it.getColumnIndexOrThrow(COL_CONTATO_OBS)) ?: ""
                    )
                )
            }
        }
        return lista
    }

    // ---------- DESPESAS ----------

    /** Insere uma despesa vinculada a um mês/ano específico (formato "MM/yyyy", ex: "07/2026"). */
    fun inserirDespesa(descricao: String, valor: Double, mesAno: String, dataHora: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DESPESA_DESCRICAO, descricao)
            put(COL_DESPESA_VALOR, valor)
            put(COL_DESPESA_MES_ANO, mesAno)
            put(COL_DESPESA_DATA, dataHora)
        }
        return db.insert(TABLE_DESPESAS, null, values)
    }

    fun excluirDespesa(id: Long) {
        writableDatabase.delete(TABLE_DESPESAS, "$COL_DESPESA_ID=?", arrayOf(id.toString()))
    }

    /** Lista apenas as despesas do mês/ano informado (formato "MM/yyyy"). */
    fun listarDespesasPorMes(mesAno: String): List<Despesa> {
        val lista = mutableListOf<Despesa>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DESPESAS, null, "$COL_DESPESA_MES_ANO=?", arrayOf(mesAno), null, null, "$COL_DESPESA_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Despesa(
                        id = it.getLong(it.getColumnIndexOrThrow(COL_DESPESA_ID)),
                        descricao = it.getString(it.getColumnIndexOrThrow(COL_DESPESA_DESCRICAO)) ?: "",
                        valor = it.getDouble(it.getColumnIndexOrThrow(COL_DESPESA_VALOR)),
                        mesAno = it.getString(it.getColumnIndexOrThrow(COL_DESPESA_MES_ANO)) ?: "",
                        dataHora = it.getString(it.getColumnIndexOrThrow(COL_DESPESA_DATA)) ?: ""
                    )
                )
            }
        }
        return lista
    }

    /** Soma o total de despesas apenas do mês/ano informado. */
    fun totalDespesasPorMes(mesAno: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($COL_DESPESA_VALOR) FROM $TABLE_DESPESAS WHERE $COL_DESPESA_MES_ANO=?",
            arrayOf(mesAno)
        )
        var total = 0.0
        cursor.use {
            if (it.moveToFirst()) {
                total = it.getDouble(0)
            }
        }
        return total
    }
}
