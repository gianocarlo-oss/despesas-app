package com.example.meuapp.agenda

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meuapp.R
import com.example.meuapp.db.DBHelper

class ContatoEditActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var contatoId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato_edit)

        dbHelper = DBHelper(this)
        val editNome = findViewById<EditText>(R.id.editNome)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editTelefone = findViewById<EditText>(R.id.editTelefone)
        val editObservacoes = findViewById<EditText>(R.id.editObservacoes)

        contatoId = intent.getLongExtra("contato_id", -1L)
        if (contatoId != -1L) {
            val contato = dbHelper.listarContatos().find { it.id == contatoId }
            if (contato != null) {
                editNome.setText(contato.nome)
                editEmail.setText(contato.email)
                editTelefone.setText(contato.telefone)
                editObservacoes.setText(contato.observacoes)
            }
        }

        findViewById<android.view.View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<android.view.View>(R.id.btnSalvar).setOnClickListener {
            val nome = editNome.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val telefone = editTelefone.text.toString().trim()
            val observacoes = editObservacoes.text.toString().trim()

            if (nome.isEmpty()) {
                Toast.makeText(this, "Informe o nome do contato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contatoId == -1L) {
                dbHelper.inserirContato(nome, email, telefone, observacoes)
            } else {
                dbHelper.atualizarContato(contatoId, nome, email, telefone, observacoes)
            }

            Toast.makeText(this, "Contato salvo!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
