package com.example.meuapp.agenda

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R
import com.example.meuapp.db.DBHelper

class AgendaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ContatoAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var txtVazio: TextView
    private var listaCompleta: List<Contato> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        dbHelper = DBHelper(this)
        recycler = findViewById(R.id.recyclerContatos)
        txtVazio = findViewById(R.id.txtVazio)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ContatoAdapter(
            mutableListOf(),
            onClick = { contato ->
                val intent = Intent(this, ContatoEditActivity::class.java)
                intent.putExtra("contato_id", contato.id)
                startActivity(intent)
            },
            onDelete = { contato -> confirmarExclusao(contato) }
        )
        recycler.adapter = adapter

        findViewById<View>(R.id.btnNovoContato).setOnClickListener {
            startActivity(Intent(this, ContatoEditActivity::class.java))
        }

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        val editBusca = findViewById<EditText>(R.id.editBusca)
        editBusca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        carregarContatos()
    }

    private fun carregarContatos() {
        listaCompleta = dbHelper.listarContatos()
        adapter.atualizarLista(listaCompleta)
        atualizarEstadoVazio(listaCompleta)
    }

    private fun filtrar(termo: String) {
        val filtrada = if (termo.isBlank()) {
            listaCompleta
        } else {
            listaCompleta.filter { it.nome.contains(termo, ignoreCase = true) }
        }
        adapter.atualizarLista(filtrada)
        atualizarEstadoVazio(filtrada)
    }

    private fun atualizarEstadoVazio(lista: List<Contato>) {
        txtVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility = if (lista.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun confirmarExclusao(contato: Contato) {
        AlertDialog.Builder(this)
            .setTitle("Excluir contato")
            .setMessage("Tem certeza que deseja excluir \"${contato.nome}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                dbHelper.excluirContato(contato.id)
                carregarContatos()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
