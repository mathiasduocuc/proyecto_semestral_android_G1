
package com.ui.screens

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.model.ClasificacionCliente
import com.viewmodel.FormularioViewModel

@Composable
fun FormularioScreen(
    correoObservador: String,
    viewModel: FormularioViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(correoObservador) {
        viewModel.establecerObservador(correoObservador)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Tienda Rincon Sur",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Registro de Persona Observada",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Complete los datos de la persona observada"
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Acceso de la tienda",
                    style = MaterialTheme.typography.titleMedium
                )

                listOf("A1", "A2", "A3", "A4").forEach { acceso ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado.acceso == acceso,
                            onClick = {
                                viewModel.onAccesoChange(acceso)
                            }
                        )

                        Text(text = acceso)
                    }
                }

                estado.errorAcceso?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Clasificacion del cliente",
                    style = MaterialTheme.typography.titleMedium
                )

                ClasificacionCliente.entries.forEach { clasificacion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado.clasificacion == clasificacion,
                            onClick = {
                                viewModel.onClasificacionChange(clasificacion)
                            }
                        )

                        Text(text = clasificacion.etiqueta)
                    }
                }

                estado.errorClasificacion?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Rango etario",
                    style = MaterialTheme.typography.titleMedium
                )

                listOf(
                    "Nino/a",
                    "Joven",
                    "Adulto/a",
                    "Adulto/a mayor"
                ).forEach { rango ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado.rangoEtario == rango,
                            onClick = {
                                viewModel.onRangoEtarioChange(rango)
                            }
                        )

                        Text(text = rango)
                    }
                }

                estado.errorRangoEtario?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Tipo de compania",
                    style = MaterialTheme.typography.titleMedium
                )

                listOf(
                    "Solo/a",
                    "En pareja",
                    "En grupo",
                    "Con ninos/as"
                ).forEach { compania ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = estado.tipoCompania == compania,
                            onClick = {
                                viewModel.onTipoCompaniaChange(compania)
                            }
                        )

                        Text(text = compania)
                    }
                }

                estado.errorTipoCompania?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = estado.comprasOtraTienda,
                    onCheckedChange = {
                        viewModel.onComprasOtraTiendaChange(it)
                    }
                )

                Text(
                    text = "Lleva compras de otra tienda"
                )
            }
        }

        Button(
            onClick = {
                viewModel.guardarObservacion()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar observacion")
        }

        if (estado.mensaje.isNotBlank()) {
            Text(
                text = estado.mensaje,
                color = if (estado.registroExitoso) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        OutlinedButton(
            onClick = {
                viewModel.limpiarFormulario()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Limpiar formulario")
        }
    }
}

