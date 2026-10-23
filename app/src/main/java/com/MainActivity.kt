

package com

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.ui.screens.FormularioScreen
import com.ui.screens.LoginScreen
import com.ui.screens.MenuScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var pantallaActual by rememberSaveable {
                mutableStateOf("login")
            }

            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (pantallaActual) {

                            "login" -> {
                                LoginScreen(
                                    onLoginExitoso = {
                                        pantallaActual = "menu"
                                    }
                                )
                            }

                            "menu" -> {
                                MenuScreen(
                                    onRegistrarClientes = {
                                        pantallaActual = "formulario"
                                    },
                                    onVerRegistros = {
                                        pantallaActual = "registros"
                                    },
                                    onEstadisticas = {
                                        pantallaActual = "estadisticas"
                                    },
                                    onCerrarSesion = {
                                        pantallaActual = "login"
                                    }
                                )
                            }

                            "formulario" -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Text("Volver al menú")
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        FormularioScreen()
                                    }
                                }
                            }

                            "registros" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Ver registros",
                                        style = MaterialTheme.typography.headlineMedium
                                    )

                                    Text(
                                        text = "Esta pantalla se implementará más adelante"
                                    )

                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        }
                                    ) {
                                        Text("Volver al menú")
                                    }
                                }
                            }

                            "estadisticas" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Estadísticas",
                                        style = MaterialTheme.typography.headlineMedium
                                    )

                                    Text(
                                        text = "Esta pantalla se implementará más adelante"
                                    )

                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        }
                                    ) {
                                        Text("Volver al menú")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
