
package com.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun MenuScreen(
    onRegistrarClientes: () -> Unit,
    onVerRegistros: () -> Unit,
    onEstadisticas: () -> Unit,
    onCerrarSesion: () -> Unit
) {

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
            text = "Menu principal",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Bienvenido, Observador",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

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
                    Text("Ver registros")
                }

                Button(
                    onClick = onEstadisticas,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Estadisticas")
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
