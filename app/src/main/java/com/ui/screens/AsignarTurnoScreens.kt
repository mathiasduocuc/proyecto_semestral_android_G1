package com.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viewmodel.TurnoViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun AsignarTurnoScreen(
    onVolver: () -> Unit,
    viewModel: TurnoViewModel = viewModel()
) {
    val context = LocalContext.current

    var correo by remember { mutableStateOf("") }
    var acceso by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }

    fun seleccionarHora(alSeleccionar: (String) -> Unit) {
        val calendario = Calendar.getInstance()

        TimePickerDialog(
            context,
            { _, hora, minuto ->
                alSeleccionar(
                    String.format(
                        Locale.US,
                        "%02d:%02d",
                        hora,
                        minuto
                    )
                )
            },
            calendario.get(Calendar.HOUR_OF_DAY),
            calendario.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Asignar turno",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Asignacion de horarios para observadores")

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                mensaje = ""
            },
            label = {
                Text("Correo del observador")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Seleccionar acceso")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("A1", "A2", "A3", "A4").forEach { opcion ->

                if (acceso == opcion) {
                    Button(
                        onClick = {
                            acceso = opcion
                            mensaje = ""
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(opcion)
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            acceso = opcion
                            mensaje = ""
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text(opcion)
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                val calendario = Calendar.getInstance()

                DatePickerDialog(
                    context,
                    { _, anio, mes, dia ->
                        fecha = String.format(
                            Locale.US,
                            "%02d/%02d/%04d",
                            dia,
                            mes + 1,
                            anio
                        )
                        mensaje = ""
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (fecha.isBlank()) {
                    "Seleccionar fecha"
                } else {
                    "Fecha: $fecha"
                }
            )
        }

        OutlinedButton(
            onClick = {
                seleccionarHora {
                    horaInicio = it
                    mensaje = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (horaInicio.isBlank()) {
                    "Seleccionar hora de inicio"
                } else {
                    "Inicio: $horaInicio"
                }
            )
        }

        OutlinedButton(
            onClick = {
                seleccionarHora {
                    horaFin = it
                    mensaje = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (horaFin.isBlank()) {
                    "Seleccionar hora de termino"
                } else {
                    "Termino: $horaFin"
                }
            )
        }

        Button(
            onClick = {
                val guardado = viewModel.asignarTurno(
                    correoObservador = correo,
                    acceso = acceso,
                    fecha = fecha,
                    horaInicio = horaInicio,
                    horaFin = horaFin
                )

                guardadoExitoso = guardado

                mensaje = if (guardado) {
                    "Turno asignado correctamente"
                } else {
                    viewModel.ultimoError
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Asignar turno")
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = if (guardadoExitoso) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menu")
        }
    }
}

