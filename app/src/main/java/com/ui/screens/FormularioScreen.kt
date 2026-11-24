
package com.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Rincon Sur",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Registro rapido de clientes",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Acceso de la tienda",
            style = MaterialTheme.typography.labelLarge
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("A1", "A2", "A3", "A4").forEach { acceso ->
                OpcionCompacta(
                    texto = acceso,
                    seleccionado = estado.acceso == acceso,
                    onClick = {
                        viewModel.onAccesoChange(acceso)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        estado.errorAcceso?.let {
            TextoErrorCompacto(it)
        }

        Text(
            text = "Clasificacion del cliente",
            style = MaterialTheme.typography.labelLarge
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ClasificacionCliente.entries.forEach { clasificacion ->
                OpcionCompacta(
                    texto = clasificacion.etiqueta,
                    seleccionado = estado.clasificacion == clasificacion,
                    onClick = {
                        viewModel.onClasificacionChange(clasificacion)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        estado.errorClasificacion?.let {
            TextoErrorCompacto(it)
        }

        Text(
            text = "Rango etario",
            style = MaterialTheme.typography.labelLarge
        )

        val rangos = listOf(
            "Nino/a",
            "Joven",
            "Adulto/a",
            "Adulto/a mayor"
        )

        rangos.chunked(2).forEach { fila ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                fila.forEach { rango ->
                    OpcionCompacta(
                        texto = rango,
                        seleccionado = estado.rangoEtario == rango,
                        onClick = {
                            viewModel.onRangoEtarioChange(rango)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        estado.errorRangoEtario?.let {
            TextoErrorCompacto(it)
        }

        Text(
            text = "Tipo de compania",
            style = MaterialTheme.typography.labelLarge
        )

        val companias = listOf(
            "Solo/a",
            "En pareja",
            "En grupo",
            "Con ninos/as"
        )

        companias.chunked(2).forEach { fila ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                fila.forEach { compania ->
                    OpcionCompacta(
                        texto = compania,
                        seleccionado = estado.tipoCompania == compania,
                        onClick = {
                            viewModel.onTipoCompaniaChange(compania)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        estado.errorTipoCompania?.let {
            TextoErrorCompacto(it)
        }

        OpcionCompacta(
            texto = "Lleva compras de otra tienda",
            seleccionado = estado.comprasOtraTienda,
            onClick = {
                viewModel.onComprasOtraTiendaChange(
                    !estado.comprasOtraTienda
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.guardarObservacion()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar observacion")
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
            Text("Limpiar formulario")
        }
    }
}

@Composable
private fun OpcionCompacta(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (seleccionado) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = if (seleccionado) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        border = BorderStroke(
            1.dp,
            if (seleccionado) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = texto,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                maxLines = 2,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun TextoErrorCompacto(mensaje: String) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

