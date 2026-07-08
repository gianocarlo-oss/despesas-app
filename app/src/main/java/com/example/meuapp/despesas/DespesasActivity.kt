package com.example.meuapp.despesas

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R
import com.example.meuapp.db.DBHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DespesasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var prefs: SharedPreferences
    private lateinit var adapter: DespesaAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var txtVazio: TextView

    private lateinit var editSalario: EditText
    private lateinit var editDescricao: EditText
    private lateinit var editValorDespesa: EditText
    private lateinit var txtTotalGastos: TextView
    private lateinit var txtSaldo: TextView
    private lateinit var rowSaldo: View
    private lateinit var txtMesAno: TextView

    // Mês/ano atualmente selecionado na tela (começa no mês corrente)
    private val calendarioSelecionado: Calendar = Calendar.getInstance()

    private val nomesMeses = arrayOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    companion object {
        private const val PREFS_NAME = "financas_prefs"
        private const val KEY_SALARIO = "salario_valor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesas)

        dbHelper = DBHelper(this)
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        recycler = findViewById(R.id.recyclerDespesas)
        txtVazio = findViewById(R.id.txtVazio)
        editSalario = findViewById(R.id.editSalario)
        editDescricao = findViewById(R.id.editDescricao)
        editValorDespesa = findViewById(R.id.editValorDespesa)
        txtTotalGastos = findViewById(R.id.txtTotalGastos)
        txtSaldo = findViewById(R.id.txtSaldo)
        rowSaldo = findViewById(R.id.rowSaldo)
        txtMesAno = findViewById(R.id.txtMesAno)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.isNestedScrollingEnabled = false
        adapter = DespesaAdapter(mutableListOf()) { despesa -> confirmarExclusao(despesa) }
        recycler.adapter = adapter

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }

        // Navegação entre meses
        findViewById<View>(R.id.btnMesAnterior).setOnClickListener {
            calendarioSelecionado.add(Calendar.MONTH, -1)
            carregarDespesas()
        }
        findViewById<View>(R.id.btnMesProximo).setOnClickListener {
            calendarioSelecionado.add(Calendar.MONTH, 1)
            carregarDespesas()
        }

        // Carrega salário salvo, se houver
        val salarioSalvo = prefs.getString(KEY_SALARIO, "")
        if (!salarioSalvo.isNullOrBlank()) {
            editSalario.setText(salarioSalvo)
        }

        findViewById<View>(R.id.btnSalvarSalario).setOnClickListener {
            val texto = editSalario.text.toString().trim()
            prefs.edit().putString(KEY_SALARIO, texto).apply()
            Toast.makeText(this, "Salário atualizado!", Toast.LENGTH_SHORT).show()
            atualizarResumo()
        }

        findViewById<View>(R.id.btnAdicionarDespesa).setOnClickListener {
            adicionarDespesa()
        }

        carregarDespesas()
    }

    override fun onResume() {
        super.onResume()
        carregarDespesas()
    }

    /** Retorna a chave de armazenamento do mês/ano selecionado, ex: "07/2026". */
    private fun mesAnoChave(): String {
        val mes = calendarioSelecionado.get(Calendar.MONTH) + 1
        val ano = calendarioSelecionado.get(Calendar.YEAR)
        return String.format(Locale("pt", "BR"), "%02d/%04d", mes, ano)
    }

    /** Retorna o texto amigável exibido na tela, ex: "Julho de 2026". */
    private fun mesAnoExibicao(): String {
        val mes = calendarioSelecionado.get(Calendar.MONTH)
        val ano = calendarioSelecionado.get(Calendar.YEAR)
        return "${nomesMeses[mes]} de $ano"
    }

    private fun adicionarDespesa() {
        val descricao = editDescricao.text.toString().trim()
        val valorTexto = editValorDespesa.text.toString().trim().replace(",", ".")

        if (descricao.isEmpty()) {
            Toast.makeText(this, "Informe a descrição da despesa", Toast.LENGTH_SHORT).show()
            return
        }

        val valor = valorTexto.toDoubleOrNull()
        if (valor == null || valor <= 0.0) {
            Toast.makeText(this, "Informe um valor válido para a despesa", Toast.LENGTH_SHORT).show()
            return
        }

        val dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date())
        dbHelper.inserirDespesa(descricao, valor, mesAnoChave(), dataHora)

        editDescricao.text.clear()
        editValorDespesa.text.clear()

        carregarDespesas()
        Toast.makeText(this, "Despesa adicionada em ${mesAnoExibicao()}!", Toast.LENGTH_SHORT).show()
    }

    private fun carregarDespesas() {
        txtMesAno.text = mesAnoExibicao()

        val lista = dbHelper.listarDespesasPorMes(mesAnoChave())
        adapter.atualizarLista(lista)
        txtVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility = if (lista.isEmpty()) View.GONE else View.VISIBLE
        atualizarResumo()
    }

    private fun atualizarResumo() {
        val totalGastos = dbHelper.totalDespesasPorMes(mesAnoChave())
        txtTotalGastos.text = formatarMoeda(totalGastos)

        val salarioTexto = prefs.getString(KEY_SALARIO, "")?.trim() ?: ""

        if (salarioTexto.isBlank()) {
            // Sem salário informado: não calcula nem exibe saldo (evita mostrar negativo indevido)
            rowSaldo.visibility = View.GONE
        } else {
            val salario = salarioTexto.replace(",", ".").toDoubleOrNull()
            if (salario == null) {
                rowSaldo.visibility = View.GONE
            } else {
                val saldo = salario - totalGastos
                rowSaldo.visibility = View.VISIBLE
                txtSaldo.text = formatarMoeda(saldo)
                val corPositivo = resources.getColor(R.color.success, theme)
                val corNegativo = resources.getColor(R.color.danger, theme)
                txtSaldo.setTextColor(if (saldo < 0) corNegativo else corPositivo)
            }
        }
    }

    private fun formatarMoeda(valor: Double): String {
        return String.format(Locale("pt", "BR"), "R$ %.2f", valor)
    }

    private fun confirmarExclusao(despesa: Despesa) {
        AlertDialog.Builder(this)
            .setTitle("Excluir despesa")
            .setMessage("Tem certeza que deseja excluir \"${despesa.descricao}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                dbHelper.excluirDespesa(despesa.id)
                carregarDespesas()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
