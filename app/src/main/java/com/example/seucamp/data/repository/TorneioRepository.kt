package com.example.seucamp.data.repository

import com.example.seucamp.data.api.RetrofitInstance
import com.example.seucamp.data.dao.TorneioDao
import com.example.seucamp.model.Torneio

class TorneioRepository(
    private val dao: TorneioDao
) {
    suspend fun inserir(torneio: Torneio) = dao.inserir(torneio)
    suspend fun listar() = dao.listar()
    suspend fun atualizar(torneio: Torneio) = dao.atualizar(torneio)
    suspend fun excluir(torneio: Torneio) = dao.excluir(torneio)
    suspend fun buscarCep(cep: String) = RetrofitInstance.api.buscarCep(cep)
}
