package com.example.seucamp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import org.junit.Rule
import org.junit.Test

class FluxoSeuCampTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun criaEditaEExcluiTorneioEInscricao() {
        val nome = "Copa ${System.currentTimeMillis()}"

        compose.onNodeWithText("Jogos disponíveis").assertIsDisplayed()
        clicar("Sobre")
        compose.onNodeWithText("Sobre o SeuCamp").assertIsDisplayed()
        clicar("Torneios")
        clicar("Escolher jogo para cadastrar")
        clicarNaLista("Ver torneios de Valorant")
        clicarNaLista("Novo torneio")
        preencher("Nome do torneio", nome)
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText(nome).assertIsDisplayed()
        compose.onNodeWithText("Jogo: Valorant").assertIsDisplayed()
        preencher("Descrição", "Campeonato local")
        clicarNoFormulario("Adicionar local por CEP (opcional)")
        compose.onNodeWithText("CEP").assertExists()
        clicarNoFormulario("Remover local")
        clicarNoFormulario("Salvar torneio")
        aguardarTexto(nome)
        compose.onNode(hasScrollToNodeAction()).performScrollToNode(hasText(nome))
        compose.onNodeWithContentDescription("Abrir $nome").performClick()

        clicarNaLista("Inscrever equipe")
        preencher("Nome da equipe", "Equipe A")
        for (i in 1..5) preencher("Nick $i", "Player$i")
        clicarNoFormulario("Gravar equipe")
        clicarNaLista("Editar equipe")
        preencher("Nome da equipe", "Equipe B")
        clicarNoFormulario("Gravar equipe")
        aguardarTexto("Equipe B")
        clicar("Voltar")
        pressBack()
        compose.onNodeWithText("Ver torneios de Valorant").assertIsDisplayed()
        clicar("Equipes")
        aguardarTexto("Equipe B")
        clicarNaLista("Abrir torneio")
        clicarNaLista("Cancelar inscrição")
        clicar("Confirmar")
        aguardarTexto("Nenhuma equipe inscrita.")

        clicarNaLista("Editar torneio")
        preencher("Nome do torneio", "$nome editada")
        clicarNoFormulario("Salvar torneio")
        aguardarTexto("$nome editada")
        clicarNaLista("Excluir torneio")
        clicar("Excluir")
        aguardarTexto("Você ainda não inscreveu equipes.")
        compose.onNodeWithText("$nome editada").assertDoesNotExist()
    }

    private fun preencher(rotulo: String, valor: String) {
        compose.onNodeWithText(rotulo).performScrollTo().performTextReplacement(valor)
        closeSoftKeyboard()
    }

    private fun clicar(texto: String) {
        compose.onNodeWithText(texto).performClick()
    }

    private fun clicarNoFormulario(texto: String) {
        compose.onNodeWithText(texto).performScrollTo().performClick()
    }

    private fun clicarNaLista(texto: String) {
        compose.onNode(hasScrollToNodeAction()).performScrollToNode(hasText(texto))
        compose.onNodeWithText(texto).performClick()
    }

    private fun aguardarTexto(texto: String) {
        compose.waitUntil(5_000) {
            compose.onAllNodes(hasText(texto)).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
