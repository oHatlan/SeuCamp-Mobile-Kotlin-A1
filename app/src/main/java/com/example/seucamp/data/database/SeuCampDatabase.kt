package com.example.seucamp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seucamp.data.dao.InscricaoDao
import com.example.seucamp.data.dao.TorneioDao
import com.example.seucamp.model.Inscricao
import com.example.seucamp.model.Torneio

@Database(
    entities = [Torneio::class, Inscricao::class],
    version = 1,
    exportSchema = false
)
abstract class SeuCampDatabase : RoomDatabase() {
    abstract fun torneioDao(): TorneioDao
    abstract fun inscricaoDao(): InscricaoDao
}
