package com.example.meuapp.notas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R

class NotaAdapter(
    private val notas: MutableList<Nota>,
    private val onClick: (Nota) -> Unit,
    private val onDelete: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    class NotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val conteudo: TextView = view.findViewById(R.id.txtConteudo)
        val data: TextView = view.findViewById(R.id.txtData)
        val btnExcluir: View = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.titulo.text = nota.titulo.ifBlank { "(Sem título)" }
        holder.conteudo.text = nota.conteudo
        holder.data.text = nota.dataHora
        holder.itemView.setOnClickListener { onClick(nota) }
        holder.btnExcluir.setOnClickListener { onDelete(nota) }
    }

    override fun getItemCount(): Int = notas.size

    fun atualizarLista(novaLista: List<Nota>) {
        notas.clear()
        notas.addAll(novaLista)
        notifyDataSetChanged()
    }
}
