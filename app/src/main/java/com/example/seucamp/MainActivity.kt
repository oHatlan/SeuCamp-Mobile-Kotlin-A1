package com.example.seucamp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.seucamp.data.database.SeuCampDatabase
import com.example.seucamp.data.repository.InscricaoRepository
import com.example.seucamp.data.repository.TorneioRepository
import com.example.seucamp.model.Torneio
import com.example.seucamp.ui.theme.SeuCampMobileTheme
import com.example.seucamp.view.CadastroInscricao
import com.example.seucamp.view.CadastroTorneio
import com.example.seucamp.view.DetalheTorneio
import com.example.seucamp.view.ListaTorneios
import com.example.seucamp.view.ListaJogos
import com.example.seucamp.view.MinhasEquipes
import com.example.seucamp.view.Sobre
import com.example.seucamp.viewmodel.InscricaoViewModel
import com.example.seucamp.viewmodel.InscricaoViewModelFactory
import com.example.seucamp.viewmodel.TorneioViewModel
import com.example.seucamp.viewmodel.TorneioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            SeuCampDatabase::class.java,
            "seucamp.db"
        ).build()
        val torneioRepository = TorneioRepository(database.torneioDao())
        val inscricaoRepository = InscricaoRepository(database.inscricaoDao())

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            SeuCampMobileTheme {
                TelaPrincipal(torneioRepository, inscricaoRepository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPrincipal(
    torneioRepository: TorneioRepository,
    inscricaoRepository: InscricaoRepository
) {
    val torneioViewModel: TorneioViewModel = viewModel(
        factory = TorneioViewModelFactory(torneioRepository)
    )
    val inscricaoViewModel: InscricaoViewModel = viewModel(
        factory = InscricaoViewModelFactory(inscricaoRepository)
    )

    val abas = listOf("Início", "Jogos", "Sobre", "Torneios", "Equipes")
    val aba = torneioViewModel.abaSelecionada
    val jogo = torneioViewModel.jogoSelecionado
    val torneio = torneioViewModel.torneioSelecionado
    val cadastroTorneio = torneioViewModel.exibirCadastroTorneio
    val cadastroInscricao = inscricaoViewModel.exibirCadastroInscricao
    val podeVoltar = cadastroTorneio || cadastroInscricao || torneio != null || jogo != null
    val operacaoEmAndamento = torneioViewModel.operacaoEmAndamento ||
        inscricaoViewModel.operacaoEmAndamento
    val voltar = {
        if (!operacaoEmAndamento) {
            when {
                cadastroInscricao -> inscricaoViewModel.cancelarCadastro()
                cadastroTorneio -> torneioViewModel.cancelarCadastro()
                torneio != null -> torneioViewModel.voltarParaLista()
                else -> torneioViewModel.fecharJogo()
            }
        }
    }
    val abrirTorneio: (Torneio) -> Unit = {
        inscricaoViewModel.carregarInscricoes(it.id)
        torneioViewModel.abrirTorneio(it)
    }

    LaunchedEffect(aba, podeVoltar) {
        if (aba == 4 && !podeVoltar) inscricaoViewModel.carregarEquipes()
    }

    BackHandler(enabled = podeVoltar, onBack = voltar)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when {
                            cadastroInscricao -> if (inscricaoViewModel.modoEdicao) {
                                "Editar inscrição"
                            } else {
                                "Inscrever equipe"
                            }
                            cadastroTorneio -> if (torneioViewModel.modoEdicao) {
                                "Editar torneio"
                            } else {
                                "Novo torneio"
                            }
                            torneio != null -> "Detalhes do torneio"
                            jogo != null -> jogo.nome
                            else -> "SeuCamp"
                        },
                        fontWeight = if (podeVoltar) FontWeight.Medium else FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (podeVoltar) {
                        TextButton(onClick = voltar, enabled = !operacaoEmAndamento) {
                            Text("Voltar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = if (podeVoltar) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            if (!podeVoltar) {
                TabRow(
                    selectedTabIndex = aba,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                        .navigationBarsPadding()
                ) {
                    abas.forEachIndexed { indice, titulo ->
                        Tab(
                            selected = aba == indice,
                            onClick = { torneioViewModel.selecionarAba(indice) },
                            modifier = Modifier.height(50.dp),
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                titulo,
                                fontSize = 12.sp,
                                fontWeight = if (aba == indice) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when {
            cadastroTorneio -> CadastroTorneio(torneioViewModel, modifier)
            cadastroInscricao && torneio != null -> CadastroInscricao(
                inscricaoViewModel, torneio.nome, modifier
            )
            torneio != null -> DetalheTorneio(torneio, torneioViewModel, inscricaoViewModel, modifier)
            jogo != null || aba == 3 -> ListaTorneios(
                torneioViewModel,
                onAbrir = abrirTorneio,
                onNovo = {
                    if (jogo != null) torneioViewModel.abrirCadastroTorneio(jogo.idJogo)
                    else torneioViewModel.selecionarAba(1)
                },
                modifier = modifier,
                jogo = jogo
            )
            aba == 2 -> Sobre(modifier)
            aba == 4 -> MinhasEquipes(inscricaoViewModel, torneioViewModel.torneios, abrirTorneio, modifier)
            else -> ListaJogos(inicio = aba == 0, onAbrir = torneioViewModel::abrirJogo, modifier = modifier)
        }
    }
}
