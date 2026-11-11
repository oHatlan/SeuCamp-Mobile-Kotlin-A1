package com.example.seucamp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = VerdeEscuro,
    primaryContainer = VerdeEscuro,
    onPrimaryContainer = VerdeClaro,
    background = Grafite,
    onBackground = TextoClaro,
    surface = Grafite,
    onSurface = TextoClaro,
    surfaceVariant = GrafiteClaro,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = GrafiteElevado,
    surfaceContainerHigh = GrafiteClaro,
    outline = Contorno,
    outlineVariant = GrafiteClaro,
    error = CoralClaro
)

@Composable
fun SeuCampMobileTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
