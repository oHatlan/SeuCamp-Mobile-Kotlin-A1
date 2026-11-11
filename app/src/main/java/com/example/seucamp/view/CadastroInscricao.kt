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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seucamp.viewmodel.InscricaoViewModel

@Composable
fun CadastroInscricao(
    viewModel: InscricaoViewModel,
    nomeTorneio: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(nomeTorneio, style = MaterialTheme.typography.titleMedium)
        CampoCadastro("Nome da equipe", viewModel.nomeEquipe, viewModel::atualizarNomeEquipe)
        CampoCadastro("Nick 1", viewModel.nick1, viewModel::atualizarNick1)
        CampoCadastro("Nick 2", viewModel.nick2, viewModel::atualizarNick2)
        CampoCadastro("Nick 3", viewModel.nick3, viewModel::atualizarNick3)
        CampoCadastro("Nick 4", viewModel.nick4, viewModel::atualizarNick4)
        CampoCadastro("Nick 5", viewModel.nick5, viewModel::atualizarNick5)

        if (viewModel.mensagemErro.isNotBlank()) {
            Text(viewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = viewModel::gravarInscricao,
                enabled = !viewModel.operacaoEmAndamento
            ) { Text("Gravar equipe") }
            OutlinedButton(
                onClick = viewModel::cancelarCadastro,
                enabled = !viewModel.operacaoEmAndamento
            ) { Text("Cancelar") }
        }
    }
}
