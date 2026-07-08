package com.example.meuapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meuapp.agenda.AgendaActivity
import com.example.meuapp.despesas.DespesasActivity
import com.example.meuapp.notas.NotasActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.cardNotas).setOnClickListener {
            startActivity(Intent(this, NotasActivity::class.java))
        }

        findViewById<android.view.View>(R.id.cardAgenda).setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }

        findViewById<android.view.View>(R.id.cardDespesas).setOnClickListener {
            startActivity(Intent(this, DespesasActivity::class.java))
        }
    }
}
