package com.example.seucamp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seucamp.model.Torneio
import com.example.seucamp.model.Jogo
import com.example.seucamp.model.listaJogos
import com.example.seucamp.viewmodel.TorneioViewModel

@Composable
fun ListaTorneios(
    viewModel: TorneioViewModel,
    onAbrir: (Torneio) -> Unit,
    onNovo: () -> Unit,
    modifier: Modifier = Modifier,
    jogo: Jogo? = null
) {
    val torneios = if (jogo == null) viewModel.torneios
        else viewModel.torneios.filter { it.idJogo == jogo.idJogo }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            if (jogo != null) {
                Image(
                    painter = painterResource(jogo.imagem),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(2.35f)
                        .clip(RoundedCornerShape(8.dp))
                )
                Text(
                    "Visão geral",
                    modifier = Modifier.padding(top = 14.dp, bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    jogo.descricao,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                if (jogo == null) "Meus torneios" else "Torneios",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = onNovo,
                enabled = !viewModel.operacaoEmAndamento,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(if (jogo == null) "Escolher jogo para cadastrar" else "Novo torneio")
            }
        }

        if (viewModel.mensagemErro.isNotBlank()) {
            item {
                Text(viewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = viewModel::carregarTorneios) { Text("Tentar novamente") }
            }
        } else if (torneios.isEmpty()) {
            item {
                Text(
                    if (viewModel.operacaoEmAndamento) "Carregando torneios..."
                    else "Nenhum torneio cadastrado."
                )
            }
        }

        items(torneios, key = { it.id }) { torneio ->
            val jogoTorneio = listaJogos.find { it.idJogo == torneio.idJogo }
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (jogo == null && jogoTorneio != null) {
                    Image(
                        painter = painterResource(jogoTorneio.imagem),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(width = 84.dp, height = 64.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(torneio.nome, style = MaterialTheme.typography.titleMedium)
                    if (jogo == null) Text(jogoTorneio?.nome.orEmpty())
                    Text(torneio.descricao, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (torneio.premiacao.isNotBlank()) {
                        Text("Premiação: ${torneio.premiacao}", color = MaterialTheme.colorScheme.primary)
                    }
                }
                TextButton(
                    onClick = { onAbrir(torneio) },
                    enabled = !viewModel.operacaoEmAndamento,
                    modifier = Modifier.semantics { contentDescription = "Abrir ${torneio.nome}" }
                ) { Text("Abrir") }
            }
        }
    }
}
