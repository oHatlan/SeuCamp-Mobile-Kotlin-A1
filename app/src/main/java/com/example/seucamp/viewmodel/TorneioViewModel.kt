package com.example.seucamp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seucamp.data.repository.TorneioRepository
import com.example.seucamp.model.Jogo
import com.example.seucamp.model.Torneio
import com.example.seucamp.model.listaJogos
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class TorneioViewModel(
    private val repository: TorneioRepository
) : ViewModel() {
    var torneios by mutableStateOf<List<Torneio>>(emptyList())
        private set

    var abaSelecionada by mutableStateOf(0)
        private set

    var jogoSelecionado by mutableStateOf<Jogo?>(null)
        private set

    private var idSelecionado by mutableStateOf(0)
    val torneioSelecionado: Torneio?
        get() = torneios.find { it.id == idSelecionado }

    var exibirCadastroTorneio by mutableStateOf(false)
        private set

    var modoEdicao by mutableStateOf(false)
        private set

    var operacaoEmAndamento by mutableStateOf(false)
        private set

    var buscandoCep by mutableStateOf(false)
        private set

    private var numeroBusca = 0

    var mensagemErro by mutableStateOf("")
        private set

    var mensagemCep by mutableStateOf("")
        private set

    var idJogo by mutableStateOf(0)
        private set

    var nome by mutableStateOf("")
        private set

    var descricao by mutableStateOf("")
        private set

    var premiacao by mutableStateOf("")
        private set

    var cep by mutableStateOf("")
        private set

    var logradouro by mutableStateOf("")
        private set

    var numero by mutableStateOf("")
        private set

    var bairro by mutableStateOf("")
        private set

    var localidade by mutableStateOf("")
        private set

    var uf by mutableStateOf("")
        private set

    init {
        carregarTorneios()
    }

    fun selecionarAba(indice: Int) {
        abaSelecionada = indice
    }

    fun abrirJogo(jogo: Jogo) {
        jogoSelecionado = jogo
    }

    fun fecharJogo() {
        jogoSelecionado = null
    }

    fun carregarTorneios() {
        executarOperacao {
            torneios = repository.listar()
        }
    }

    fun abrirTorneio(torneio: Torneio) {
        mensagemErro = ""
        idSelecionado = torneio.id
    }

    fun voltarParaLista() {
        if (operacaoEmAndamento) return
        mensagemErro = ""
        idSelecionado = 0
    }

    fun abrirCadastroTorneio(idJogo: Int) {
        reiniciarTela()
        this.idJogo = idJogo
        exibirCadastroTorneio = true
    }

    fun editarTorneio() {
        val torneio = torneioSelecionado ?: return
        reiniciarTela()
        nome = torneio.nome
        idJogo = torneio.idJogo
        descricao = torneio.descricao
        premiacao = torneio.premiacao
        cep = torneio.cep
        logradouro = torneio.logradouro
        numero = torneio.numero
        bairro = torneio.bairro
        localidade = torneio.localidade
        uf = torneio.uf
        modoEdicao = true
        exibirCadastroTorneio = true
    }

    fun atualizarNome(texto: String) {
        nome = texto
    }

    fun atualizarDescricao(texto: String) {
        descricao = texto
    }

    fun atualizarPremiacao(texto: String) {
        premiacao = texto
    }

    fun atualizarCep(texto: String) {
        val novoCep = texto.filter { it in '0'..'9' }.take(8)
        if (novoCep == cep) return

        cep = novoCep
        numeroBusca++
        buscandoCep = false
        mensagemCep = ""
        logradouro = ""
        numero = ""
        bairro = ""
        localidade = ""
        uf = ""
    }

    fun buscarCep(possuiInternet: Boolean) {
        if (cep.length != 8 || buscandoCep) return

        if (!possuiInternet) {
            mensagemCep = "Sem conexão com a internet. Preencha o endereço manualmente."
            return
        }

        val cepConsultado = cep
        val busca = ++numeroBusca
        buscandoCep = true
        mensagemCep = ""

        viewModelScope.launch {
            try {
                val endereco = repository.buscarCep(cepConsultado)
                if (busca != numeroBusca) return@launch

                if (endereco.erro == true || endereco.cep.isNullOrBlank()) {
                    mensagemCep = "CEP não encontrado."
                } else {
                    logradouro = endereco.logradouro.orEmpty()
                    bairro = endereco.bairro.orEmpty()
                    localidade = endereco.localidade.orEmpty()
                    uf = endereco.uf.orEmpty()
                }
            } catch (erro: CancellationException) {
                throw erro
            } catch (erro: Exception) {
                if (busca == numeroBusca) mensagemCep = "Erro ao consultar o ViaCEP."
            } finally {
                if (busca == numeroBusca) buscandoCep = false
            }
        }
    }

    fun gravarTorneio() {
        if (operacaoEmAndamento || buscandoCep) return
        if (nome.isBlank() || descricao.isBlank() || listaJogos.none { it.idJogo == idJogo }) {
            mensagemErro = "Preencha o nome, o jogo e a descrição."
            return
        }

        if (cep.isNotBlank() && (cep.length != 8 || localidade.isBlank())) {
            mensagemErro = "Busque um CEP válido ou deixe o campo vazio."
            return
        }

        val torneio = Torneio(
            id = if (modoEdicao) idSelecionado else 0,
            nome = nome.trim(),
            idJogo = idJogo,
            descricao = descricao.trim(),
            data = torneioSelecionado?.data.orEmpty(),
            premiacao = premiacao.trim(),
            cep = cep,
            logradouro = logradouro.trim(),
            numero = numero.trim(),
            bairro = bairro.trim(),
            localidade = localidade.trim(),
            uf = uf
        )

        executarOperacao {
            val gravou = if (modoEdicao) {
                repository.atualizar(torneio) > 0
            } else {
                repository.inserir(torneio) != -1L
            }

            if (gravou) {
                torneios = repository.listar()
                reiniciarTela()
            } else {
                mensagemErro = "Não foi possível gravar o torneio."
            }
        }
    }

    fun excluirTorneio() {
        val torneio = torneioSelecionado ?: return

        executarOperacao {
            if (repository.excluir(torneio) > 0) {
                torneios = repository.listar()
                idSelecionado = 0
            } else {
                mensagemErro = "Não foi possível excluir o torneio."
            }
        }
    }

    fun cancelarCadastro() {
        if (!operacaoEmAndamento) reiniciarTela()
    }

    private fun reiniciarTela() {
        nome = ""
        idJogo = 0
        descricao = ""
        premiacao = ""
        cep = ""
        logradouro = ""
        numero = ""
        bairro = ""
        localidade = ""
        uf = ""
        mensagemErro = ""
        mensagemCep = ""
        numeroBusca++
        buscandoCep = false
        modoEdicao = false
        exibirCadastroTorneio = false
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
