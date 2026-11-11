package com.example.seucamp.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CampoCadastro(
    rotulo: String,
    valor: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    teclado: KeyboardType = KeyboardType.Text,
    linhaUnica: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(rotulo) },
        singleLine = linhaUnica,
        minLines = if (linhaUnica) 1 else 3,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = modifier
    )
}
