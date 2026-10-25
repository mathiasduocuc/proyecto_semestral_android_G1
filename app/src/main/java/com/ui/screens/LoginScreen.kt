
package com.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginExitoso: () -> Unit
) {

    val estado by viewModel.uiState.collectAsState()

    var mostrarContrasena by remember { mutableStateOf(false) }
    var mostrarRecuperacion by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensajeRecuperacion by remember { mutableStateOf("") }
    var recuperacionExitosa by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Rincon Sur",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (mostrarRecuperacion) {
                "Recuperar contrasena"
            } else {
                "Inicio de sesion"
            },
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (!mostrarRecuperacion) {

                    OutlinedTextField(
                        value = estado.usuario,
                        onValueChange = {
                            viewModel.onUsuarioChange(it)
                        },
                        label = {
                            Text("Correo electronico")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true,
                        isError = estado.errorUsuario != null,
                        supportingText = {
                            estado.errorUsuario?.let {
                                Text(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = estado.contrasena,
                        onValueChange = {
                            viewModel.onContrasenaChange(it)
                        },
                        label = {
                            Text("Contrasena")
                        },
                        singleLine = true,
                        isError = estado.errorContrasena != null,
                        visualTransformation = if (mostrarContrasena) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            TextButton(
                                onClick = {
                                    mostrarContrasena = !mostrarContrasena
                                }
                            ) {
                                Text(
                                    if (mostrarContrasena) {
                                        "Ocultar"
                                    } else {
                                        "Mostrar"
                                    }
                                )
                            }
                        },
                        supportingText = {
                            estado.errorContrasena?.let {
                                Text(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (estado.mensaje.isNotEmpty()) {

                        Text(
                            text = estado.mensaje,
                            color = if (estado.inicioExitoso) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }

                    Button(
                        onClick = {

                            viewModel.iniciarSesion()

                            if (viewModel.uiState.value.inicioExitoso) {
                                onLoginExitoso()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar sesion")
                    }

                    TextButton(
                        onClick = {

                            mostrarRecuperacion = true

                            correoRecuperacion = estado.usuario

                            nuevaContrasena = ""
                            confirmarContrasena = ""
                            mensajeRecuperacion = ""
                            recuperacionExitosa = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Olvidaste tu contrasena?")
                    }

                } else {

                    Text(
                        text = "Ingresa tu correo electronico para recuperar tu contrasena.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = correoRecuperacion,
                        onValueChange = {
                            correoRecuperacion = it
                        },
                        label = {
                            Text("Correo electronico")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = nuevaContrasena,
                        onValueChange = {
                            nuevaContrasena = it
                        },
                        label = {
                            Text("Nueva contrasena")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                        },
                        label = {
                            Text("Confirmar contrasena")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensajeRecuperacion.isNotEmpty()) {

                        Text(
                            text = mensajeRecuperacion,
                            color = if (recuperacionExitosa) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }

                    Button(
                        onClick = {

                            when {

                                correoRecuperacion.isBlank() -> {
                                    recuperacionExitosa = false
                                    mensajeRecuperacion =
                                        "Ingrese su correo electronico"
                                }

                                !android.util.Patterns.EMAIL_ADDRESS
                                    .matcher(correoRecuperacion.trim())
                                    .matches() -> {

                                    recuperacionExitosa = false
                                    mensajeRecuperacion =
                                        "Ingrese un correo electronico valido"
                                }

                                nuevaContrasena.length < 4 -> {
                                    recuperacionExitosa = false
                                    mensajeRecuperacion =
                                        "La contrasena debe tener al menos 4 caracteres"
                                }

                                nuevaContrasena != confirmarContrasena -> {
                                    recuperacionExitosa = false
                                    mensajeRecuperacion =
                                        "Las contrasenas no coinciden"
                                }

                                else -> {

                                    val resultado =
                                        viewModel.recuperarContrasena(
                                            correoRecuperacion.trim(),
                                            nuevaContrasena
                                        )

                                    recuperacionExitosa = resultado.first
                                    mensajeRecuperacion = resultado.second

                                    if (resultado.first) {
                                        viewModel.onUsuarioChange(
                                            correoRecuperacion.trim()
                                        )

                                        viewModel.onContrasenaChange("")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cambiar contrasena")
                    }

                    TextButton(
                        onClick = {

                            mostrarRecuperacion = false
                            mensajeRecuperacion = ""
                            recuperacionExitosa = false
                            nuevaContrasena = ""
                            confirmarContrasena = ""
                            mostrarContrasena = false

                            viewModel.onContrasenaChange("")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Regresar al inicio de sesion")
                    }
                }
            }
        }
    }
}
