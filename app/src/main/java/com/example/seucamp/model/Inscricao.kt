package com.example.seucamp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inscricao",
    foreignKeys = [
        ForeignKey(
            entity = Torneio::class,
            parentColumns = ["id"],
            childColumns = ["idTorneio"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idTorneio", "nomeEquipe"], unique = true)]
)
data class Inscricao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idTorneio: Int,
    val nomeEquipe: String,
    val nick1: String,
    val nick2: String,
    val nick3: String,
    val nick4: String,
    val nick5: String
)
