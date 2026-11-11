package com.example.seucamp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seucamp.model.Jogo
import com.example.seucamp.model.Torneio
import com.example.seucamp.model.listaJogos
import com.example.seucamp.viewmodel.InscricaoViewModel

@Composable
fun ListaJogos(
    inicio: Boolean,
    onAbrir: (Jogo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (inicio) {
            item {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Bem-vindo ao SeuCamp!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Boa sorte nas próximas partidas.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        item {
            Text(
                if (inicio) "Jogos disponíveis" else "Jogos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
        items(listaJogos, key = { it.idJogo }) { jogo ->
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(jogo.imagem),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(width = 96.dp, height = 80.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            jogo.nome,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (!inicio) {
                            Text(
                                jogo.descricao,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        TextButton(onClick = { onAbrir(jogo) }, contentPadding = PaddingValues(0.dp)) {
                            Text("Ver torneios de ${jogo.nome}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MinhasEquipes(
    viewModel: InscricaoViewModel,
    torneios: List<Torneio>,
    onAbrir: (Torneio) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Minhas equipes", style = MaterialTheme.typography.headlineSmall)
            when {
                viewModel.mensagemErro.isNotBlank() -> {
                    Text(viewModel.mensagemErro, color = MaterialTheme.colorScheme.error)
                    TextButton(onClick = viewModel::carregarEquipes) { Text("Tentar novamente") }
                }
                viewModel.operacaoEmAndamento -> Text("Carregando equipes...")
                viewModel.equipes.isEmpty() -> Text("Você ainda não inscreveu equipes.")
            }
        }
        items(viewModel.equipes, key = { it.id }) { equipe ->
            val torneio = torneios.find { it.id == equipe.idTorneio }
            if (torneio != null) {
                val jogo = listaJogos.find { it.idJogo == torneio.idJogo }
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(equipe.nomeEquipe, style = MaterialTheme.typography.titleMedium)
                        Text("Jogo: ${jogo?.nome.orEmpty()}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Torneio: ${torneio.nome}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        TextButton(
                            onClick = { onAbrir(torneio) },
                            enabled = !viewModel.operacaoEmAndamento,
                            contentPadding = PaddingValues(0.dp)
                        ) { Text("Abrir torneio") }
                    }
                }
            }
        }
    }
}

@Composable
fun Sobre(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Sobre o SeuCamp", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            Text("O SeuCamp organiza campeonatos de jogos e facilita a inscrição de equipes.")
        }
        item {
            Text("Sobre o trabalho", style = MaterialTheme.typography.titleLarge)
            Text("Projeto Android desenvolvido em Kotlin com Jetpack Compose e banco de dados local com Room.")
        }
    }
}
