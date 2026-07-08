package com.example.meuapp.despesas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R
import java.util.Locale

class DespesaAdapter(
    private val despesas: MutableList<Despesa>,
    private val onDelete: (Despesa) -> Unit
) : RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder>() {

    class DespesaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descricao: TextView = view.findViewById(R.id.txtDescricao)
        val data: TextView = view.findViewById(R.id.txtData)
        val valor: TextView = view.findViewById(R.id.txtValor)
        val btnExcluir: View = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_despesa, parent, false)
        return DespesaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val despesa = despesas[position]
        holder.descricao.text = despesa.descricao.ifBlank { "(Sem descrição)" }
        holder.data.text = despesa.dataHora
        holder.valor.text = String.format(Locale("pt", "BR"), "- R$ %.2f", despesa.valor)
        holder.btnExcluir.setOnClickListener { onDelete(despesa) }
    }

    override fun getItemCount(): Int = despesas.size

    fun atualizarLista(novaLista: List<Despesa>) {
        despesas.clear()
        despesas.addAll(novaLista)
        notifyDataSetChanged()
    }
}
