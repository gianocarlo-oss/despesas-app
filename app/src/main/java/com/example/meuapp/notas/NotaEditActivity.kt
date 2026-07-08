package com.example.meuapp.notas

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meuapp.R
import com.example.meuapp.db.DBHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotaEditActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var notaId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_edit)

        dbHelper = DBHelper(this)
        val editTitulo = findViewById<EditText>(R.id.editTitulo)
        val editConteudo = findViewById<EditText>(R.id.editConteudo)

        notaId = intent.getLongExtra("nota_id", -1L)
        if (notaId != -1L) {
            val nota = dbHelper.listarNotas().find { it.id == notaId }
            if (nota != null) {
                editTitulo.setText(nota.titulo)
                editConteudo.setText(nota.conteudo)
            }
        }

        findViewById<android.view.View>(R.id.btnVoltar).setOnClickListener { finish() }

        findViewById<android.view.View>(R.id.btnSalvar).setOnClickListener {
            val titulo = editTitulo.text.toString().trim()
            val conteudo = editConteudo.text.toString().trim()

            if (titulo.isEmpty() && conteudo.isEmpty()) {
                Toast.makeText(this, "Escreva algo antes de salvar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date())

            if (notaId == -1L) {
                dbHelper.inserirNota(titulo, conteudo, dataHora)
            } else {
                dbHelper.atualizarNota(notaId, titulo, conteudo, dataHora)
            }

            Toast.makeText(this, "Nota salva!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
