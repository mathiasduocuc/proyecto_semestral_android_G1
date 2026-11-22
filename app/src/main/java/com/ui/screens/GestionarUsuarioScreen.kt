package com.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.model.Rol
import com.model.Usuario
import com.viewmodel.LoginViewModel

@Composable
fun GestionarUsuarioScreen(
    viewModel: LoginViewModel,
    onVolver: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var modoEdicion by remember { mutableStateOf(false) }
    var usuarioAEditar by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf("") }

    // Variable para forzar el redibujado de la lista
    var recargarLista by remember { mutableStateOf(false) }

    // Quitamos la doble declaración que tenías aquí
    val observadores = remember(recargarLista) {
        viewModel.obtenerListaObservadores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestión de Observadores",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (modoEdicion) "Modificar Contraseña" else "Crear Nuevo Observador",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    enabled = !modoEdicion,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (correo.isBlank() || contrasena.isBlank()) {
                                mensajeError = "Todos los campos son obligatorios"
                            } else {
                                if (modoEdicion) {
                                    // Asegúrate de que el ViewModel tenga esta función exacta
                                    val exito = viewModel.modificarContrasenaObservador(correo, contrasena)
                                    if (exito) {
                                        correo = ""; contrasena = ""
                                        modoEdicion = false; usuarioAEditar = null
                                        mensajeError = ""
                                        recargarLista = !recargarLista // Refrescar lista
                                    }
                                } else {
                                    val exito = viewModel.crearNuevoObservador(correo, contrasena)
                                    if (exito) {
                                        correo = ""; contrasena = ""
                                        mensajeError = ""
                                        recargarLista = !recargarLista // Refrescar lista
                                    } else {
                                        mensajeError = "El correo ya está registrado"
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (modoEdicion) "Guardar Cambios" else "Crear Observador")
                    }

                    if (modoEdicion) {
                        OutlinedButton(
                            onClick = {
                                correo = ""; contrasena = ""
                                modoEdicion = false; usuarioAEditar = null
                                mensajeError = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lista de Observadores (${observadores.size})",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(observadores) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = usuario.correo, style = MaterialTheme.typography.bodyLarge)

                        Row {
                            TextButton(
                                onClick = {
                                    correo = usuario.correo
                                    contrasena = ""
                                    modoEdicion = true
                                    usuarioAEditar = usuario.correo
                                }
                            ) {
                                Text("Editar")
                            }

                            TextButton(
                                onClick = {
                                    val exito = viewModel.eliminarUsuario(usuario.correo)
                                    if (exito) {
                                        recargarLista = !recargarLista
                                    }
                                }
                            ) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Menú Principal")
        }
    }
}