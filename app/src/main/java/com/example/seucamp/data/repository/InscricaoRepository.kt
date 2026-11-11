package com.example.seucamp.data.repository

import com.example.seucamp.data.dao.InscricaoDao
import com.example.seucamp.model.Inscricao

class InscricaoRepository(
    private val dao: InscricaoDao
) {
    suspend fun inserir(inscricao: Inscricao) = dao.inserir(inscricao)
    suspend fun listar() = dao.listar()
    suspend fun listarPorTorneio(idTorneio: Int) = dao.listarPorTorneio(idTorneio)
    suspend fun atualizar(inscricao: Inscricao) = dao.atualizar(inscricao)
    suspend fun excluir(inscricao: Inscricao) = dao.excluir(inscricao)
}
