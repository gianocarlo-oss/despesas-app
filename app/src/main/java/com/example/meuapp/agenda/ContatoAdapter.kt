package com.example.meuapp.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meuapp.R

class ContatoAdapter(
    private val contatos: MutableList<Contato>,
    private val onClick: (Contato) -> Unit,
    private val onDelete: (Contato) -> Unit
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    class ContatoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.txtNome)
        val telefone: TextView = view.findViewById(R.id.txtTelefone)
        val email: TextView = view.findViewById(R.id.txtEmail)
        val btnExcluir: View = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contato, parent, false)
        return ContatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val contato = contatos[position]
        holder.nome.text = contato.nome.ifBlank { "(Sem nome)" }
        holder.telefone.text = if (contato.telefone.isNotBlank()) "📞 ${contato.telefone}" else ""
        holder.email.text = if (contato.email.isNotBlank()) "✉ ${contato.email}" else ""
        holder.itemView.setOnClickListener { onClick(contato) }
        holder.btnExcluir.setOnClickListener { onDelete(contato) }
    }

    override fun getItemCount(): Int = contatos.size

    fun atualizarLista(novaLista: List<Contato>) {
        contatos.clear()
        contatos.addAll(novaLista)
        notifyDataSetChanged()
    }
}
