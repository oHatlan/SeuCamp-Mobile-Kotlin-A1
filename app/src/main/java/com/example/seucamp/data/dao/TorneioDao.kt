package com.example.seucamp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seucamp.model.Torneio

@Dao
interface TorneioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(torneio: Torneio): Long

    @Query("SELECT * FROM torneio ORDER BY id DESC")
    suspend fun listar(): List<Torneio>

    @Update
    suspend fun atualizar(torneio: Torneio): Int

    @Delete
    suspend fun excluir(torneio: Torneio): Int
}
