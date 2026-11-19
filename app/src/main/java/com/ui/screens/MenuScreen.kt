
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.model.Rol

@Composable
fun MenuScreen(
    rol: Rol,
    onRegistrarClientes: () -> Unit,
    onVerRegistros: () -> Unit,
    onEstadisticas: () -> Unit,
    onCerrarSesion: () -> Unit,
    modoOscuro: Boolean,
    onCambiarModo: () -> Unit,
    onMiperfil: () -> Unit
) {

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
            text = "Menu principal",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = if (rol == Rol.OBSERVADOR) {
                        "Bienvenido, Observador"
                    } else {
                        "Bienvenido, Supervisor"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                if (rol == Rol.OBSERVADOR) {

                    Button(
                        onClick = onRegistrarClientes,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrar clientes")
                    }

                    Button(
                        onClick = onVerRegistros,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mis registros")
                    }
                }

                if (rol == Rol.SUPERVISOR) {

                    Button(
                        onClick = onVerRegistros,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver registros")
                    }

                    Button(
                        onClick = onEstadisticas,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Estadisticas")
                    }
                }

                Button(
                    onClick = onMiperfil,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mi perfil")
                }

                OutlinedButton(
                    onClick = onCambiarModo,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (modoOscuro) {
                            "Cambiar a modo claro"
                        } else {
                            "Cambiar a modo oscuro"
                        }
                    )
                }

                OutlinedButton(
                    onClick = onCerrarSesion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesion")
                }
            }
        }
    }
}
