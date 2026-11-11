package com.example.seucamp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seucamp.model.Inscricao

@Dao
interface InscricaoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(inscricao: Inscricao): Long

    @Query("SELECT * FROM inscricao WHERE idTorneio = :idTorneio ORDER BY nomeEquipe COLLATE NOCASE")
    suspend fun listarPorTorneio(idTorneio: Int): List<Inscricao>

    @Query("SELECT * FROM inscricao ORDER BY id DESC")
    suspend fun listar(): List<Inscricao>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun atualizar(inscricao: Inscricao): Int

    @Delete
    suspend fun excluir(inscricao: Inscricao): Int
}
