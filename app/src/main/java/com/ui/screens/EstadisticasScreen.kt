

package com.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.data.Registro
import com.data.RegistroDbHelper
import com.model.Rol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun EstadisticasScreen(
    rol: Rol,
    correoUsuario: String,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    var registros by remember {
        mutableStateOf<List<Registro>>(emptyList())
    }

    LaunchedEffect(rol, correoUsuario) {
        registros = withContext(Dispatchers.IO) {
            val db = RegistroDbHelper(context.applicationContext)

            try {
                if (rol == Rol.SUPERVISOR) {
                    db.listarRegistros()
                } else {
                    db.buscarPorObservador(correoUsuario)
                }
            } finally {
                db.close()
            }
        }
    }

    val total = registros.size

    val visitas = registros.count {
        it.clasificacion.equals("Visita", ignoreCase = true)
    }

    val transitos = registros.count {
        it.clasificacion.equals("Transito", ignoreCase = true) ||
                it.clasificacion.equals("Tr\u00e1nsito", ignoreCase = true)
    }

    val noDeterminados = registros.count {
        it.clasificacion.equals("No determinado", ignoreCase = true)
    }

    val contadorAnimado = remember {
        Animatable(0f)
    }

    LaunchedEffect(total) {
        contadorAnimado.animateTo(
            targetValue = total.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
    }

    fun porcentaje(cantidad: Int): String {
        return if (total == 0) {
            "0.0%"
        } else {
            String.format(
                Locale.US,
                "%.1f%%",
                cantidad * 100.0 / total
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Estadisticas",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = if (rol == Rol.SUPERVISOR) {
                "Resumen general de observaciones"
            } else {
                "Resumen de mis observaciones"
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Total de observaciones",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = contadorAnimado.value.roundToInt().toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = "Clasificacion de clientes",
            style = MaterialTheme.typography.titleLarge
        )

        listOf(
            "Visitas" to visitas,
            "Transitos" to transitos,
            "No determinados" to noDeterminados
        ).forEach { (nombre, cantidad) ->

            TarjetaEstadisticaAnimada(
                nombre = nombre,
                cantidad = cantidad,
                total = total,
                porcentajeTexto = porcentaje(cantidad)
            )
        }

        Text(
            text = "Observaciones por acceso",
            style = MaterialTheme.typography.titleLarge
        )

        listOf("A1", "A2", "A3", "A4").forEach { acceso ->

            val cantidad = registros.count {
                it.acceso.equals(acceso, ignoreCase = true)
            }

            TarjetaEstadisticaAnimada(
                nombre = "Acceso $acceso",
                cantidad = cantidad,
                total = total,
                porcentajeTexto = porcentaje(cantidad)
            )
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menu")
        }
    }
}

@Composable
private fun TarjetaEstadisticaAnimada(
    nombre: String,
    cantidad: Int,
    total: Int,
    porcentajeTexto: String
) {
    val progresoObjetivo = if (total > 0) {
        cantidad.toFloat() / total
    } else {
        0f
    }

    var iniciarAnimacion by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        iniciarAnimacion = true
    }

    val progresoAnimado by animateFloatAsState(
        targetValue = if (iniciarAnimacion) progresoObjetivo else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Progreso estadistica"
    )

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(nombre)

                Text("$cantidad - $porcentajeTexto")
            }

            LinearProgressIndicator(
                progress = { progresoAnimado },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}
