package com.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viewmodel.LoginViewModel
import com.example.accesosur.R


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginExitoso: () -> Unit
) {

    val estado by viewModel.uiState.collectAsState()

    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    var mostrarRecuperacion by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(estado.inicioExitoso) {
        if (estado.inicioExitoso) {
            onLoginExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_rincon_sur),
            "Logo Rincon Sur",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(220.dp)
                .padding(bottom = 6.dp)
                .align(Alignment.CenterHorizontally)

        )
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
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar sesion")
                    }

                    TextButton(
                        onClick = {
                            mostrarRecuperacion = true
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Olvidaste tu contrasena?")
                    }

                } else {

                    Text(
                        text = "Contacte con el supervisor para recuperar el acceso a su cuenta.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    TextButton(
                        onClick = {
                            mostrarRecuperacion = false
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
