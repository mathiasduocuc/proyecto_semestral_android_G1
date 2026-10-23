
package com.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Rincon Sur",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Inicio de sesion",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = estado.usuario,
                    onValueChange = {
                        viewModel.onUsuarioChange(it)
                    },
                    label = {
                        Text("Usuario")
                    },
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
                                text = if (mostrarContrasena) {
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
            }
        }
    }
}
