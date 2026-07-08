package com.example.meuapp.despesas

data class Despesa(
    val id: Long,
    val descricao: String,
    val valor: Double,
    val mesAno: String, // formato "MM/yyyy", ex: "07/2026"
    val dataHora: String
)
