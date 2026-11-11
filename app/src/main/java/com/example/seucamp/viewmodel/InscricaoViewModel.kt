package com.example.seucamp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seucamp.data.repository.InscricaoRepository
import com.example.seucamp.model.Inscricao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class InscricaoViewModel(
    private val repository: InscricaoRepository
) : ViewModel() {
    var inscricoes by mutableStateOf<List<Inscricao>>(emptyList())
        private set

    var equipes by mutableStateOf<List<Inscricao>>(emptyList())
        private set

    private var idTorneio = 0
    private var idInscricao = 0

    var exibirCadastroInscricao by mutableStateOf(false)
        private set

    var modoEdicao by mutableStateOf(false)
        private set

    var operacaoEmAndamento by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set

    var nomeEquipe by mutableStateOf("")
        private set

    var nick1 by mutableStateOf("")
        private set

    var nick2 by mutableStateOf("")
        private set

    var nick3 by mutableStateOf("")
        private set

    var nick4 by mutableStateOf("")
        private set

    var nick5 by mutableStateOf("")
        private set

    fun carregarInscricoes(id: Int) {
        idTorneio = id
        inscricoes = emptyList()
        executarOperacao {
            inscricoes = repository.listarPorTorneio(idTorneio)
        }
    }

    fun carregarEquipes() {
        executarOperacao {
            equipes = repository.listar()
        }
    }

    fun abrirCadastroInscricao() {
        reiniciarTela()
        exibirCadastroInscricao = true
    }

    fun editarInscricao(inscricao: Inscricao) {
        reiniciarTela()
        idInscricao = inscricao.id
        nomeEquipe = inscricao.nomeEquipe
        nick1 = inscricao.nick1
        nick2 = inscricao.nick2
        nick3 = inscricao.nick3
        nick4 = inscricao.nick4
        nick5 = inscricao.nick5
        modoEdicao = true
        exibirCadastroInscricao = true
    }

    fun atualizarNomeEquipe(texto: String) {
        nomeEquipe = texto
    }

    fun atualizarNick1(texto: String) {
        nick1 = texto
    }

    fun atualizarNick2(texto: String) {
        nick2 = texto
    }

    fun atualizarNick3(texto: String) {
        nick3 = texto
    }

    fun atualizarNick4(texto: String) {
        nick4 = texto
    }

    fun atualizarNick5(texto: String) {
        nick5 = texto
    }

    fun gravarInscricao() {
        if (operacaoEmAndamento) return
        if (listOf(nomeEquipe, nick1, nick2, nick3, nick4, nick5).any { it.isBlank() }) {
            mensagemErro = "Preencha o nome da equipe e os cinco nicks."
            return
        }

        val inscricao = Inscricao(
            id = idInscricao,
            idTorneio = idTorneio,
            nomeEquipe = nomeEquipe.trim(),
            nick1 = nick1.trim(),
            nick2 = nick2.trim(),
            nick3 = nick3.trim(),
            nick4 = nick4.trim(),
            nick5 = nick5.trim()
        )

        executarOperacao {
            val gravou = if (modoEdicao) {
                repository.atualizar(inscricao) > 0
            } else {
                repository.inserir(inscricao) != -1L
            }

            if (gravou) {
                inscricoes = repository.listarPorTorneio(idTorneio)
                reiniciarTela()
            } else {
                mensagemErro = "Não foi possível gravar. Verifique se esse nome já está em uso no torneio."
            }
        }
    }

    fun excluirInscricao(inscricao: Inscricao) {
        executarOperacao {
            if (repository.excluir(inscricao) > 0) {
                inscricoes = repository.listarPorTorneio(idTorneio)
            } else {
                mensagemErro = "Não foi possível cancelar a inscrição."
            }
        }
    }

    fun cancelarCadastro() {
        if (!operacaoEmAndamento) reiniciarTela()
    }

    private fun reiniciarTela() {
        idInscricao = 0
        nomeEquipe = ""
        nick1 = ""
        nick2 = ""
        nick3 = ""
        nick4 = ""
        nick5 = ""
        mensagemErro = ""
        modoEdicao = false
        exibirCadastroInscricao = false
    }

    private fun executarOperacao(acao: suspend () -> Unit) {
        if (operacaoEmAndamento) return
        mensagemErro = ""
        operacaoEmAndamento = true

        viewModelScope.launch {
            try {
                acao()
            } catch (erro: CancellationException) {
                throw erro
            } catch (erro: Exception) {
                mensagemErro = "Não foi possível concluir a operação com o banco."
            } finally {
                operacaoEmAndamento = false
            }
        }
    }
}
