package com.example.meuapp.notas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R
import com.example.meuapp.db.DBHelper

class NotasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: NotaAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var txtVazio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        dbHelper = DBHelper(this)
        recycler = findViewById(R.id.recyclerNotas)
        txtVazio = findViewById(R.id.txtVazio)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = NotaAdapter(
            mutableListOf(),
            onClick = { nota ->
                val intent = Intent(this, NotaEditActivity::class.java)
                intent.putExtra("nota_id", nota.id)
                startActivity(intent)
            },
            onDelete = { nota -> confirmarExclusao(nota) }
        )
        recycler.adapter = adapter

        findViewById<View>(R.id.btnNovaNota).setOnClickListener {
            startActivity(Intent(this, NotaEditActivity::class.java))
        }

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        carregarNotas()
    }

    private fun carregarNotas() {
        val lista = dbHelper.listarNotas()
        adapter.atualizarLista(lista)
        txtVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility = if (lista.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun confirmarExclusao(nota: Nota) {
        AlertDialog.Builder(this)
            .setTitle("Excluir nota")
            .setMessage("Tem certeza que deseja excluir \"${nota.titulo.ifBlank { "Sem título" }}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                dbHelper.excluirNota(nota.id)
                carregarNotas()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
