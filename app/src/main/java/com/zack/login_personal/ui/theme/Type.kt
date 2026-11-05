package com.zack.login_personal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Configura los estilos tipográficos de Material que utiliza la aplicación.
val Typography = Typography(
    // Define el estilo base para textos largos o descriptivos.
    bodyLarge = TextStyle(
        // Usa la familia tipográfica predeterminada del sistema.
        fontFamily = FontFamily.Default,
        // Mantiene el grosor normal para facilitar la lectura.
        fontWeight = FontWeight.Normal,
        // Establece el tamaño principal del texto en 16 puntos escalables.
        fontSize = 16.sp,
        // Reserva 24 puntos entre líneas para mejorar la legibilidad.
        lineHeight = 24.sp,
        // Añade un ligero espacio entre caracteres.
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)