
package com.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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

@Composable
fun RegistrosScreen(
    rol: Rol,
    correoUsuario: String,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    val dbHelper = remember(context) {
        RegistroDbHelper(context.applicationContext)
    }

    DisposableEffect(dbHelper) {
        onDispose {
            dbHelper.close()
        }
    }

    var registros by remember {
        mutableStateOf<List<Registro>>(emptyList())
    }

    var mostrarTodos by remember {
        mutableStateOf(false)
    }

    var fechaFiltro by remember {
        mutableStateOf("")
    }

    fun cargarRegistros() {
        registros = if (rol == Rol.SUPERVISOR) {
            dbHelper.listarRegistros()
        } else {
            dbHelper.buscarPorObservador(correoUsuario)
        }
    }

    androidx.compose.runtime.LaunchedEffect(rol, correoUsuario) {
        cargarRegistros()
    }

    val registrosFiltrados = registros.filter { registro ->
        fechaFiltro.isBlank() || registro.fecha == fechaFiltro.trim()
    }

    val registrosVisibles = if (mostrarTodos) {
        registrosFiltrados
    } else {
        registrosFiltrados.take(3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = if (rol == Rol.SUPERVISOR) {
                "Ver registros"
            } else {
                "Mis registros"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Historial de observaciones")

        OutlinedTextField(
            value = fechaFiltro,
            onValueChange = {
                fechaFiltro = it
            },
            label = {
                Text("Filtrar por fecha")
            },
            placeholder = {
                Text("dd/MM/yyyy")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = {
                fechaFiltro = ""
                cargarRegistros()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar filtro y actualizar")
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (registrosVisibles.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron registros",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            items(
                items = registrosVisibles,
                key = { it.id }
            ) { registro ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "Registro #${registro.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Fecha: ${registro.fecha}")

                        Text("Hora: ${registro.hora}")

                        Text("Acceso: ${registro.acceso}")

                        Text("Clasificacion: ${registro.clasificacion}")

                        Text("Rango etario: ${registro.rangoEtario}")

                        Text("Tipo de compania: ${registro.tipoCompania}")

                        Text(
                            text = "Compras de otra tienda: " +
                                    if (registro.comprasOtraTienda) "Si" else "No"
                        )

                        if (rol == Rol.SUPERVISOR) {
                            Text("Observador: ${registro.observador}")
                        }
                    }
                }
            }
        }

        if (registrosFiltrados.size > 3) {
            OutlinedButton(
                onClick = {
                    mostrarTodos = !mostrarTodos
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (mostrarTodos) {
                        "Mostrar ultimos 3"
                    } else {
                        "Ver todos los registros"
                    }
                )
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menu")
        }
    }
}
