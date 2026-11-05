package com.zack.login_personal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zack.login_personal.ui.theme.Login_PersonalTheme
import com.zack.login_personal.ui.theme.Night
import com.zack.login_personal.ui.theme.Panel
import com.zack.login_personal.ui.theme.Peach
import com.zack.login_personal.ui.theme.Perimeter
import com.zack.login_personal.ui.theme.PurpleMist
import com.zack.login_personal.ui.theme.TextMuted
import com.zack.login_personal.ui.theme.TextPrimary
import com.zack.login_personal.ui.theme.Warning

private const val DEMO_EMAIL = "aventurero@rpg.com"
private const val DEMO_PASSWORD = "runa2026"

class MainActivity : ComponentActivity() {
    // Android llama a este método al crear la pantalla principal de la aplicación.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Permite que la interfaz dibuje contenido también en las zonas del sistema.
        enableEdgeToEdge()
        // Define la interfaz completa usando Jetpack Compose.
        setContent {
            // Aplica los colores y la tipografía personalizados de la aplicación.
            Login_PersonalTheme {
                // Este estado central decide qué pantalla se muestra, sin una librería de navegación.
                var nombreUsuario by rememberSaveable { mutableStateOf<String?>(null) }

                // Proporciona la superficie de fondo común a ambas pantallas.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Night
                ) {
                    // Si no hay usuario autenticado, muestra el formulario de acceso.
                    if (nombreUsuario == null) {
                        // Al validar las credenciales, guarda el nombre para mostrar la bienvenida.
                        LoginPanel(onLoginCorrecto = { nombreUsuario = it })
                    } else {
                        // Si el acceso fue correcto, muestra la pantalla de bienvenida.
                        WelcomePanel(
                            nombre = nombreUsuario.orEmpty(),
                            // Cerrar sesión borra el nombre y vuelve al formulario.
                            onCerrarSesion = { nombreUsuario = null }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginPanel(onLoginCorrecto: (String) -> Unit) {
    // rememberSaveable mantiene el formulario si Android recrea la actividad, por ejemplo al girar.
    // Guarda el texto introducido en el campo del correo.
    var correo by rememberSaveable { mutableStateOf("") }
    // Guarda la contraseña mientras el usuario completa el formulario.
    var contrasena by rememberSaveable { mutableStateOf("") }
    // Controla si la contraseña se ve como texto o permanece oculta.
    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    // Indica si las últimas credenciales introducidas fueron incorrectas.
    var hayError by rememberSaveable { mutableStateOf(false) }

    // Permite consultar el espacio disponible para adaptar el formulario a la pantalla.
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            // Dibuja el fondo oscuro con un degradado vertical.
            .background(
                Brush.verticalGradient(
                    colors = listOf(Night, Panel.copy(alpha = 0.72f), Night)
                )
            )
    ) {
        // Organiza el formulario en columna, centrado y desplazable si falta espacio.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dibuja la tarjeta y le entrega los valores y acciones del formulario.
            LoginCard(
                correo = correo,
                contrasena = contrasena,
                mostrarContrasena = mostrarContrasena,
                hayError = hayError,
                onCorreoChange = {
                    // Actualiza el correo y quita el aviso de error al volver a editar.
                    correo = it
                    hayError = false
                },
                onContrasenaChange = {
                    // Actualiza la contraseña y quita el aviso de error al volver a editar.
                    contrasena = it
                    hayError = false
                },
                // Invierte el estado para mostrar u ocultar la contraseña.
                onCambiarVisibilidad = { mostrarContrasena = !mostrarContrasena },
                onEntrar = {
                    // Compara el correo sin distinguir mayúsculas y quita espacios exteriores.
                    if (correo.trim().equals(DEMO_EMAIL, ignoreCase = true) &&
                        // La contraseña debe coincidir exactamente con la credencial de prueba.
                        contrasena == DEMO_PASSWORD
                    ) {
                        // Obtiene una versión legible del nombre a partir del correo.
                        val nombre = correo.substringBefore("@")
                            .substringBefore(".")
                            .replaceFirstChar { it.uppercase() }
                        // Notifica el acceso correcto para cambiar a la pantalla de bienvenida.
                        onLoginCorrecto(nombre)
                    } else {
                        // Activa el mensaje visual de error si alguna credencial no coincide.
                        hayError = true
                    }
                }
            )
        }
    }
}

@Composable
private fun LoginCard(
    // Texto actual del campo de correo, recibido desde el estado del formulario.
    correo: String,
    // Texto actual de la contraseña.
    contrasena: String,
    // Determina si el contenido de la contraseña se muestra sin ocultar.
    mostrarContrasena: Boolean,
    // Indica si ambos campos deben marcarse como inválidos.
    hayError: Boolean,
    // Acción que devuelve al formulario un cambio en el correo.
    onCorreoChange: (String) -> Unit,
    // Acción que devuelve al formulario un cambio en la contraseña.
    onContrasenaChange: (String) -> Unit,
    // Acción para invertir la visibilidad de la contraseña.
    onCambiarVisibilidad: () -> Unit,
    // Acción para intentar validar e iniciar sesión.
    onEntrar: () -> Unit
) {
    // Construye la tarjeta que contiene el logotipo, los campos y el botón de acceso.
    Column(
        modifier = Modifier
            .widthIn(max = 440.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Panel.copy(alpha = 0.94f))
            .border(1.dp, Perimeter, RoundedCornerShape(30.dp))
            .padding(horizontal = 26.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Muestra el emblema cerrado, asociado a la pantalla de acceso.
        LogoCircular(abierto = false, modifier = Modifier.size(76.dp))
        Spacer(Modifier.height(20.dp))
        Text(
            text = "PORTAL DEL GREMIO",
            color = Peach,
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.4.sp
        )
        Spacer(Modifier.height(9.dp))
        Text(
            text = "Identificación\nde Aventurero",
            color = TextPrimary,
            fontSize = 27.sp,
            lineHeight = 33.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Introduce tus credenciales para entrar",
            color = TextMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(27.dp))

        // Campo de correo con teclado optimizado para direcciones electrónicas.
        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            // Hace que el campo ocupe el ancho disponible dentro de la tarjeta.
            modifier = Modifier.fillMaxWidth(),
            // Evita que el correo salte a una segunda línea.
            singleLine = true,
            // Etiqueta del campo que desaparece al comenzar a escribir.
            label = { Text("Correo") },
            // Icono decorativo que identifica el campo como una dirección de correo.
            leadingIcon = { Text("✉", color = PurpleMist, fontSize = 18.sp) },
            // Resalta el campo cuando hay un error de validación.
            isError = hayError,
            // Abre teclado de correo y permite avanzar al campo siguiente.
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(16.dp),
            colors = fieldColors()
        )
        Spacer(Modifier.height(13.dp))
        // Campo de contraseña con control para alternar su visibilidad.
        OutlinedTextField(
            value = contrasena,
            onValueChange = onContrasenaChange,
            // Mantiene el campo alineado con el campo de correo.
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Contraseña") },
            // Muestra un símbolo alusivo a la contraseña.
            leadingIcon = { Text("◇", color = PurpleMist, fontSize = 22.sp) },
            // Añade el control táctil para mostrar u ocultar el contenido.
            trailingIcon = {
                Text(
                    text = if (mostrarContrasena) "◉" else "◎",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onCambiarVisibilidad)
                        .padding(10.dp),
                    color = TextMuted,
                    fontSize = 18.sp
                )
            },
            visualTransformation = if (mostrarContrasena) {
                // Presenta los caracteres tal como fueron escritos.
                VisualTransformation.None
            } else {
                // Sustituye los caracteres por puntos para ocultar la contraseña.
                PasswordVisualTransformation()
            },
            isError = hayError,
            // Muestra un teclado de contraseña con una acción para enviar el formulario.
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onEntrar() }),
            // Redondea las esquinas y aplica la paleta común de los campos.
            shape = RoundedCornerShape(16.dp),
            colors = fieldColors()
        )

        if (hayError) {
            // Solo se muestra una explicación cuando las credenciales no son válidas.
            Spacer(Modifier.height(11.dp))
            Text(
                text = "Las credenciales no coinciden. Revisa tus datos.",
                modifier = Modifier.fillMaxWidth(),
                color = Warning,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))
        // Ejecuta la misma validación que la acción "Done" del teclado.
        Button(
            onClick = onEntrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PurpleMist,
                contentColor = Night
            )
        ) {
            Text("Entrar al gremio", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(8.dp))
            Text("↗", fontSize = 18.sp)
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = "ACCESO DE PRUEBA",
            color = TextMuted,
            fontSize = 10.sp,
            letterSpacing = 1.8.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "$DEMO_EMAIL  ·  $DEMO_PASSWORD",
            color = TextMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
// Centraliza los colores de los campos para mantener coherente su aspecto y sus errores.
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedBorderColor = PurpleMist,
    unfocusedBorderColor = Perimeter,
    focusedLabelColor = PurpleMist,
    unfocusedLabelColor = TextMuted,
    cursorColor = Peach,
    errorBorderColor = Warning,
    errorLabelColor = Warning,
    errorCursorColor = Warning,
    focusedLeadingIconColor = PurpleMist,
    unfocusedLeadingIconColor = PurpleMist
)

@Composable
private fun WelcomePanel(nombre: String, onCerrarSesion: () -> Unit) {
    // nombre personaliza el saludo; onCerrarSesion devuelve el control a la pantalla de acceso.
    // Prepara el fondo decorativo de la pantalla posterior al inicio de sesión.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Night, Panel.copy(alpha = 0.7f), Night)
                )
            )
    ) {
        // Dibuja los destellos decorativos detrás del contenido.
        GeometricSparkles(modifier = Modifier.fillMaxSize())
        // Distribuye el mensaje, el emblema y la acción para cerrar sesión.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 26.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(12.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // El emblema abierto indica visualmente que se completó el acceso.
                LogoCircular(abierto = true, modifier = Modifier.size(92.dp))
                Spacer(Modifier.height(24.dp))
                Text(
                    // Señala que se ha completado la identificación.
                    text = "MISIÓN COMPLETADA",
                    color = Peach,
                    fontSize = 11.sp,
                    letterSpacing = 2.5.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    // Personaliza el saludo con el nombre derivado del correo.
                    text = "¡Bienvenido,\n$nombre!",
                    color = TextPrimary,
                    fontSize = 34.sp,
                    lineHeight = 41.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Tu próxima aventura empieza aquí.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            // Muestra el emblema geométrico central de la pantalla de bienvenida.
            GeometricEmblem(modifier = Modifier.size(230.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "RANGO 01  ·  EXPLORADOR",
                    color = PurpleMist,
                    fontSize = 11.sp,
                    letterSpacing = 1.7.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(16.dp))
                // Borra el usuario actual para regresar al formulario de acceso.
                Button(
                    onClick = onCerrarSesion,
                    // Limita el ancho y mantiene el botón cómodo para tocar.
                    modifier = Modifier
                        .widthIn(max = 360.dp)
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Panel,
                        contentColor = TextPrimary
                    )
                ) {
                    Text("Abandonar mazmorra")
                    Spacer(Modifier.width(8.dp))
                    Text("↗", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun LogoCircular(abierto: Boolean, modifier: Modifier = Modifier) {
    // abierto cambia el símbolo para diferenciar el acceso pendiente del acceso correcto.
    // Usa un lienzo para dibujar el escudo y la runa sin imágenes externas.
    Canvas(
        modifier = modifier
            .clip(CircleShape)
            .background(
                Brush.linearGradient(listOf(Panel, PurpleMist.copy(alpha = 0.18f)))
            )
            .border(1.dp, PurpleMist.copy(alpha = 0.45f), CircleShape)
            .padding(16.dp)
    ) {
        // Define el contorno del escudo usando puntos relativos al tamaño del lienzo.
        val shield = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.08f)
            lineTo(size.width * 0.84f, size.height * 0.22f)
            lineTo(size.width * 0.79f, size.height * 0.62f)
            quadraticTo(
                size.width * 0.75f,
                size.height * 0.82f,
                size.width * 0.5f,
                size.height * 0.94f
            )
            quadraticTo(
                size.width * 0.25f,
                size.height * 0.82f,
                size.width * 0.21f,
                size.height * 0.62f
            )
            lineTo(size.width * 0.16f, size.height * 0.22f)
            close()
        }
        drawPath(
            path = shield,
            // Dibuja el contorno del escudo con un trazo violeta translúcido.
            color = PurpleMist.copy(alpha = 0.14f),
            style = Stroke(width = size.minDimension * 0.055f)
        )
        // Traza la runa central y, si el acceso fue correcto, añade una marca de validación.
        val rune = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.28f)
            lineTo(size.width * 0.5f, size.height * 0.67f)
            moveTo(size.width * 0.34f, size.height * 0.47f)
            lineTo(size.width * 0.5f, size.height * 0.34f)
            lineTo(size.width * 0.66f, size.height * 0.47f)
            if (abierto) {
                moveTo(size.width * 0.37f, size.height * 0.7f)
                lineTo(size.width * 0.5f, size.height * 0.79f)
                lineTo(size.width * 0.68f, size.height * 0.61f)
            }
        }
        drawPath(
            path = rune,
            // Usa el color cálido para el estado abierto y el violeta para el estado cerrado.
            color = if (abierto) Peach else PurpleMist,
            style = Stroke(
                width = size.minDimension * 0.075f,
                cap = StrokeCap.Round
            )
        )
        drawCircle(
            color = if (abierto) Peach else PurpleMist,
            radius = size.minDimension * 0.035f,
            center = Offset(size.width * 0.5f, size.height * 0.22f)
        )
    }
}

@Composable
private fun GeometricEmblem(modifier: Modifier = Modifier) {
    // Canvas permite dibujar el emblema con coordenadas relativas, sin imágenes externas.
    Canvas(modifier = modifier) {
        // Calcula el centro y los radios para que el dibujo escale con el tamaño disponible.
        val center = Offset(size.width / 2f, size.height / 2f)
        val outerRadius = size.minDimension * 0.39f
        val innerRadius = size.minDimension * 0.29f
        // Dibuja ocho radios distribuidos regularmente alrededor del centro.
        for (index in 0 until 8) {
            val angle = Math.toRadians(index * 45.0 - 90.0)
            val start = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * innerRadius,
                center.y + kotlin.math.sin(angle).toFloat() * innerRadius
            )
            val end = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * outerRadius,
                center.y + kotlin.math.sin(angle).toFloat() * outerRadius
            )
            drawLine(
                // Traza cada radio alternando los dos colores de acento.
                color = if (index % 2 == 0) Peach.copy(alpha = 0.82f) else PurpleMist,
                start = start,
                end = end,
                strokeWidth = size.minDimension * 0.008f,
                cap = StrokeCap.Round
            )
            drawCircle(
                // Coloca un pequeño círculo en el extremo de cada radio.
                color = if (index % 2 == 0) Peach else PurpleMist,
                radius = size.minDimension * 0.016f,
                center = end
            )
        }
        // Construye el rombo interior del emblema.
        val diamond = Path().apply {
            moveTo(center.x, center.y - innerRadius)
            lineTo(center.x + innerRadius * 0.58f, center.y)
            lineTo(center.x, center.y + innerRadius)
            lineTo(center.x - innerRadius * 0.58f, center.y)
            close()
        }
        drawPath(
            path = diamond,
            // Rellena el rombo con transparencia para mantener visible el fondo.
            color = PurpleMist.copy(alpha = 0.08f)
        )
        drawPath(
            path = diamond,
            // Marca el perímetro del rombo con un borde violeta.
            color = PurpleMist.copy(alpha = 0.7f),
            style = Stroke(width = size.minDimension * 0.008f)
        )
        drawCircle(color = Peach, radius = size.minDimension * 0.027f, center = center)
    }
}

@Composable
private fun GeometricSparkles(modifier: Modifier = Modifier) {
    // Pinta pequeñas estrellas en posiciones proporcionales a toda la pantalla.
    Canvas(modifier = modifier) {
        val points = listOf(
            Offset(size.width * 0.13f, size.height * 0.2f),
            Offset(size.width * 0.83f, size.height * 0.18f),
            Offset(size.width * 0.1f, size.height * 0.72f),
            Offset(size.width * 0.88f, size.height * 0.68f),
            Offset(size.width * 0.78f, size.height * 0.86f)
        )
        // Alterna tamaño y color; algunos puntos reciben además trazos en forma de cruz.
        points.forEachIndexed { index, point ->
            val radius = size.minDimension * if (index % 2 == 0) 0.007f else 0.004f
            drawCircle(
                // Dibuja el destello con tamaño y color alternos.
                color = if (index % 2 == 0) Peach.copy(alpha = 0.68f) else PurpleMist,
                radius = radius,
                center = point
            )
            if (index == 1 || index == 3) {
                // Añade trazos horizontales y verticales para dar forma de estrella.
                drawLine(
                    color = PurpleMist.copy(alpha = 0.48f),
                    start = Offset(point.x - radius * 2.5f, point.y),
                    end = Offset(point.x + radius * 2.5f, point.y),
                    strokeWidth = radius * 0.5f
                )
                drawLine(
                    color = PurpleMist.copy(alpha = 0.48f),
                    start = Offset(point.x, point.y - radius * 2.5f),
                    end = Offset(point.x, point.y + radius * 2.5f),
                    strokeWidth = radius * 0.5f
                )
            }
        }
    }
}
