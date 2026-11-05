package com.zack.login_personal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Define los colores que Material 3 asigna a cada rol de la interfaz.
private val NightColorScheme = darkColorScheme(
    primary = PurpleMist,
    onPrimary = Night,
    secondary = Peach,
    onSecondary = Night,
    background = Night,
    onBackground = TextPrimary,
    surface = Panel,
    onSurface = TextPrimary,
    error = Warning,
    onError = Night
)

@Composable
// Aplica el tema oscuro, la paleta propia y la tipografía a todo el contenido recibido.
fun Login_PersonalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NightColorScheme,
        typography = Typography,
        content = content
    )
}
