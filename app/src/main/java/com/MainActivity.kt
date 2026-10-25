
package com

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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

import com.model.Rol
import com.ui.screens.FormularioScreen
import com.ui.screens.LoginScreen
import com.ui.screens.MenuScreen
import com.ui.screens.PerfilScreen
import com.ui.theme.MiRegistroTheme
import com.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val preferencias = getSharedPreferences(
            "preferencias_app",
            MODE_PRIVATE
        )

        setContent {

            var pantallaActual by rememberSaveable {
                mutableStateOf("login")
            }

            var modoOscuro by rememberSaveable {
                mutableStateOf(
                    preferencias.getBoolean("modo_oscuro", false)
                )
            }

            MiRegistroTheme(
                darkTheme = modoOscuro,
                dynamicColor = false
            ) {

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
                                    viewModel = loginViewModel,
                                    onLoginExitoso = {
                                        pantallaActual = "menu"
                                    }
                                )
                            }

                            "menu" -> {

                                val rolActual =
                                    loginViewModel.obtenerRolActual()

                                if (rolActual != null) {

                                    MenuScreen(
                                        rol = rolActual,

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
                                            loginViewModel.cerrarSesion()
                                            pantallaActual = "login"
                                        },

                                        modoOscuro = modoOscuro,

                                        onCambiarModo = {
                                            modoOscuro = !modoOscuro

                                            preferencias.edit()
                                                .putBoolean(
                                                    "modo_oscuro",
                                                    modoOscuro
                                                )
                                                .apply()
                                        },

                                        onMiperfil = {
                                            pantallaActual = "perfil"
                                        }
                                    )
                                }
                            }

                            "perfil" -> {

                                val usuarioActual =
                                    loginViewModel.obtenerUsuarioActual()

                                if (usuarioActual != null) {

                                    PerfilScreen(
                                        correo = usuarioActual.correo,

                                        rol = if (
                                            usuarioActual.rol == Rol.OBSERVADOR
                                        ) {
                                            "Observador"
                                        } else {
                                            "Supervisor"
                                        },

                                        loginViewModel = loginViewModel,

                                        onVolver = {
                                            pantallaActual = "menu"
                                        }
                                    )
                                }
                            }

                            "formulario" -> {

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Text("Volver al menu")
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

                                    verticalArrangement =
                                        Arrangement.Center
                                ) {

                                    Text(
                                        text = "Ver registros",
                                        style = MaterialTheme
                                            .typography
                                            .headlineMedium
                                    )

                                    Text(
                                        text = "Esta pantalla se implementara mas adelante"
                                    )

                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        }
                                    ) {
                                        Text("Volver al menu")
                                    }
                                }
                            }

                            "estadisticas" -> {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),

                                    verticalArrangement =
                                        Arrangement.Center
                                ) {

                                    Text(
                                        text = "Estadisticas",
                                        style = MaterialTheme
                                            .typography
                                            .headlineMedium
                                    )

                                    Text(
                                        text = "Esta pantalla se implementara mas adelante"
                                    )

                                    Button(
                                        onClick = {
                                            pantallaActual = "menu"
                                        }
                                    ) {
                                        Text("Volver al menu")
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
