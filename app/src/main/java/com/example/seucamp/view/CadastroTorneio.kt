package com.example.seucamp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.seucamp.model.listaJogos
import com.example.seucamp.utils.NetworkUtils
import com.example.seucamp.viewmodel.TorneioViewModel

@Composable
fun CadastroTorneio(
    viewModel: TorneioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val jogo = listaJogos.find { it.idJogo == viewModel.idJogo }
    var mostrarCep by rememberSaveable { mutableStateOf(viewModel.cep.isNotBlank()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Jogo: ${jogo?.nome.orEmpty()}", style = MaterialTheme.typography.titleMedium)
        CampoCadastro("Nome do torneio", viewModel.nome, viewModel::atualizarNome)
        CampoCadastro("Descrição", viewModel.descricao, viewModel::atualizarDescricao, linhaUnica = false)
        CampoCadastro("Premiação (opcional)", viewModel.premiacao, viewModel::atualizarPremiacao)

        if (!mostrarCep) {
            TextButton(onClick = { mostrarCep = true }) {
                Text("Adicionar local por CEP (opcional)")
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CampoCadastro(
                    "CEP", viewModel.cep, viewModel::atualizarCep,
                    modifier = Modifier.weight(1f),
                    teclado = KeyboardType.Number
                )
                OutlinedButton(
                    onClick = { viewModel.buscarCep(NetworkUtils.isInternetAvailable(context)) },
                    enabled = viewModel.cep.length == 8 &&
                        !viewModel.buscandoCep && !viewModel.operacaoEmAndamento
                ) { Text("Buscar") }
            }
            if (viewModel.buscandoCep) Text("Consultando CEP...")
            if (viewModel.mensagemCep.isNotBlank()) {
                Text(viewModel.mensagemCep, color = MaterialTheme.colorScheme.error)
            }
            if (viewModel.localidade.isNotBlank()) {
                Text(listOf(viewModel.logradouro, viewModel.bairro, "${viewModel.localidade}/${viewModel.uf}")
                    .filter { it.isNotBlank() }.joinToString(" · "))
            }
            TextButton(
                onClick = {
                    viewModel.atualizarCep("")
                    mostrarCep = false
                },
                enabled = !viewModel.operacaoEmAndamento
            ) { Text("Remover local") }
        }

        if (viewModel.mensagemErro.isNotBlank()) {
            Text(viewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = viewModel::gravarTorneio,
                enabled = !viewModel.operacaoEmAndamento && !viewModel.buscandoCep
            ) { Text("Salvar torneio") }
            OutlinedButton(
                onClick = viewModel::cancelarCadastro,
                enabled = !viewModel.operacaoEmAndamento
            ) { Text("Cancelar") }
        }
    }
}
