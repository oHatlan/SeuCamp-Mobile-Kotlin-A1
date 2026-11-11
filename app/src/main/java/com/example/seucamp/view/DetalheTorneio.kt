package com.example.seucamp.view

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seucamp.model.Torneio
import com.example.seucamp.model.listaJogos
import com.example.seucamp.viewmodel.InscricaoViewModel
import com.example.seucamp.viewmodel.TorneioViewModel

@Composable
fun DetalheTorneio(
    torneio: Torneio,
    torneioViewModel: TorneioViewModel,
    inscricaoViewModel: InscricaoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val jogo = listaJogos.find { it.idJogo == torneio.idJogo }?.nome.orEmpty()
    val cep = torneio.cep.take(5) + "-" + torneio.cep.takeLast(3)
    val endereco = if (torneio.cep.isBlank()) "" else {
        val rua = listOf(torneio.logradouro, torneio.numero)
            .filter { it.isNotBlank() }.joinToString(", ")
        val local = listOf(torneio.localidade, torneio.uf)
            .filter { it.isNotBlank() }.joinToString("/")
        listOf(rua, torneio.bairro, local, "CEP $cep")
            .filter { it.isNotBlank() }.joinToString(" · ")
    }
    val habilitado = !torneioViewModel.operacaoEmAndamento &&
        !inscricaoViewModel.operacaoEmAndamento
    var confirmarTorneio by rememberSaveable { mutableStateOf(false) }
    var idInscricaoExcluir by rememberSaveable { mutableStateOf(0) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    torneio.nome,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(jogo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(torneio.descricao)
                if (endereco.isNotBlank()) {
                    Text(endereco, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (torneio.premiacao.isNotBlank()) {
                    Text("Premiação: ${torneio.premiacao}")
                }
                Button(onClick = {
                    val textoCompartilhamento = buildString {
                        appendLine("Convite: ${torneio.nome}")
                        appendLine("Jogo: $jogo")
                        append(torneio.descricao)
                        if (endereco.isNotBlank()) append("\nLocal: $endereco")
                    }

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, textoCompartilhamento)
                    context.startActivity(Intent.createChooser(intent, "Compartilhar convite"))
                }) { Text("Compartilhar convite") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = torneioViewModel::editarTorneio, enabled = habilitado) {
                        Text("Editar torneio")
                    }
                    TextButton(onClick = { confirmarTorneio = true }, enabled = habilitado) {
                        Text("Excluir torneio", color = MaterialTheme.colorScheme.error)
                    }
                }
                if (torneioViewModel.mensagemErro.isNotBlank()) {
                    Text(torneioViewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        item {
            HorizontalDivider()
            Text(
                "Equipes inscritas (${inscricaoViewModel.inscricoes.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = inscricaoViewModel::abrirCadastroInscricao,
                enabled = habilitado
            ) { Text("Inscrever equipe") }
            if (inscricaoViewModel.mensagemErro.isNotBlank()) {
                Text(inscricaoViewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
            } else if (inscricaoViewModel.inscricoes.isEmpty()) {
                Text(
                    if (inscricaoViewModel.operacaoEmAndamento) "Carregando inscrições..."
                    else "Nenhuma equipe inscrita."
                )
            }
        }

        items(inscricaoViewModel.inscricoes, key = { it.id }) { inscricao ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                HorizontalDivider()
                Text(inscricao.nomeEquipe, style = MaterialTheme.typography.titleMedium)
                Text(
                    listOf(inscricao.nick1, inscricao.nick2, inscricao.nick3, inscricao.nick4, inscricao.nick5)
                        .joinToString(", ")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { inscricaoViewModel.editarInscricao(inscricao) },
                        enabled = habilitado
                    ) { Text("Editar equipe") }
                    TextButton(
                        onClick = { idInscricaoExcluir = inscricao.id },
                        enabled = habilitado
                    ) { Text("Cancelar inscrição") }
                }
            }
        }
    }

    if (confirmarTorneio) {
        AlertDialog(
            onDismissRequest = { confirmarTorneio = false },
            title = { Text("Excluir torneio?") },
            text = { Text("As inscrições de ${torneio.nome} também serão excluídas.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarTorneio = false
                    torneioViewModel.excluirTorneio()
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarTorneio = false }) { Text("Voltar") }
            }
        )
    }

    val inscricaoExcluir = inscricaoViewModel.inscricoes.find { it.id == idInscricaoExcluir }
    if (inscricaoExcluir != null) {
        AlertDialog(
            onDismissRequest = { idInscricaoExcluir = 0 },
            title = { Text("Cancelar inscrição?") },
            text = { Text("A equipe ${inscricaoExcluir.nomeEquipe} será removida deste torneio.") },
            confirmButton = {
                TextButton(onClick = {
                    idInscricaoExcluir = 0
                    inscricaoViewModel.excluirInscricao(inscricaoExcluir)
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { idInscricaoExcluir = 0 }) { Text("Voltar") }
            }
        )
    }
}
