
package com.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.viewmodel.LoginViewModel
import java.io.File
import java.security.MessageDigest

@Composable
fun PerfilScreen(
    correo: String,
    rol: String,
    loginViewModel: LoginViewModel,
    onVolver: () -> Unit
) {

    val context = LocalContext.current

    val preferencias = remember(context) {
        context.getSharedPreferences(
            "perfiles_usuarios",
            Context.MODE_PRIVATE
        )
    }

    val claveNombre = "nombre_$correo"
    val claveTelefono = "telefono_$correo"

    var nombre by remember(correo) {
        mutableStateOf(
            preferencias.getString(claveNombre, "") ?: ""
        )
    }

    var telefono by remember(correo) {
        mutableStateOf(
            preferencias.getString(claveTelefono, "") ?: ""
        )
    }

    var contrasenaActual by remember(correo) {
        mutableStateOf("")
    }

    var contrasenaNueva by remember(correo) {
        mutableStateOf("")
    }

    var mensaje by remember(correo) {
        mutableStateOf("")
    }

    var fotoPerfil by remember(correo) {
        mutableStateOf(cargarFoto(context, correo))
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if (bitmap != null) {

            val guardado = guardarFoto(
                context = context,
                correo = correo,
                bitmap = bitmap
            )

            if (guardado) {
                fotoPerfil = bitmap
                mensaje = "Foto de perfil guardada correctamente"
            } else {
                mensaje = "Error al guardar la foto"
            }

        } else {
            mensaje = "No se tomo ninguna foto"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        fotoPerfil?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Button(
            onClick = {
                camaraLauncher.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto de perfil")
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Informacion personal",
                    style = MaterialTheme.typography.titleLarge
                )

                Text("Correo: $correo")

                Text("Rol: $rol")

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        mensaje = ""
                    },
                    label = {
                        Text("Nombre")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        telefono = it
                        mensaje = ""
                    },
                    label = {
                        Text("Telefono")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        when {

                            nombre.isBlank() -> {
                                mensaje = "Ingrese su nombre"
                            }

                            telefono.isBlank() -> {
                                mensaje = "Ingrese su telefono"
                            }

                            !telefono.all { it.isDigit() } -> {
                                mensaje = "El telefono solo debe contener numeros"
                            }

                            telefono.length < 8 -> {
                                mensaje = "Ingrese un telefono valido"
                            }

                            else -> {

                                preferencias.edit()
                                    .putString(
                                        claveNombre,
                                        nombre.trim()
                                    )
                                    .putString(
                                        claveTelefono,
                                        telefono.trim()
                                    )
                                    .apply()

                                mensaje = "Datos guardados correctamente"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Cambiar contrasena",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = {
                        contrasenaActual = it
                    },
                    label = {
                        Text("Contrasena actual")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = contrasenaNueva,
                    onValueChange = {
                        contrasenaNueva = it
                    },
                    label = {
                        Text("Nueva contrasena")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {

                        val resultado = loginViewModel.cambiarContrasena(
                            contrasenaActual = contrasenaActual,
                            nuevaContrasena = contrasenaNueva
                        )

                        mensaje = resultado.second

                        if (resultado.first) {
                            contrasenaActual = ""
                            contrasenaNueva = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cambiar contrasena")
                }
            }
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menu")
        }
    }
}

private fun archivoFoto(
    context: Context,
    correo: String
): File {

    val hash = MessageDigest.getInstance("SHA-256")
        .digest(correo.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    return File(
        context.filesDir,
        "perfil_$hash.png"
    )
}

private fun guardarFoto(
    context: Context,
    correo: String,
    bitmap: Bitmap
): Boolean {

    return try {

        archivoFoto(context, correo).outputStream().use { salida ->
            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                salida
            )
        }

    } catch (e: Exception) {
        false
    }
}

private fun cargarFoto(
    context: Context,
    correo: String
): Bitmap? {

    return try {

        val archivo = archivoFoto(context, correo)

        if (archivo.exists()) {
            BitmapFactory.decodeFile(archivo.absolutePath)
        } else {
            null
        }

    } catch (e: Exception) {
        null
    }
}
