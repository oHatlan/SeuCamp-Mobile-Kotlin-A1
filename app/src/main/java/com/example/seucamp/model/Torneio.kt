package com.example.seucamp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "torneio")
data class Torneio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val idJogo: Int,
    val descricao: String,
    val data: String,
    val premiacao: String,
    val cep: String,
    val logradouro: String,
    val numero: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)
