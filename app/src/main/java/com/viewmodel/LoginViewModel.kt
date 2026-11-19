
package com.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.model.LoginUiState
import com.model.Rol
import com.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val preferencias = application.getSharedPreferences(
        "cuentas_demo",
        Context.MODE_PRIVATE
    )

    private val usuarios = mutableListOf(
        Usuario(
            id = 1,
            nombre = "Observador",
            correo = "observador@rinconsur.cl",
            contrasena = preferencias.getString(
                "clave_observador@rinconsur.cl",
                "1234"
            ) ?: "1234",
            rol = Rol.OBSERVADOR
        ),
        Usuario(
            id = 2,
            nombre = "Supervisor",
            correo = "supervisor@rinconsur.cl",
            contrasena = preferencias.getString(
                "clave_supervisor@rinconsur.cl",
                "1234"
            ) ?: "1234",
            rol = Rol.SUPERVISOR
        )
    )

    private var usuarioActual: Usuario? = null

    fun obtenerUsuarioActual(): Usuario? {
        return usuarioActual
    }

    fun obtenerRolActual(): Rol? {
        return usuarioActual?.rol
    }

    fun onUsuarioChange(valor: String) {
        _uiState.update {
            it.copy(
                usuario = valor,
                errorUsuario = null,
                inicioExitoso = false,
                mensaje = ""
            )
        }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update {
            it.copy(
                contrasena = valor,
                errorContrasena = null,
                inicioExitoso = false,
                mensaje = ""
            )
        }
    }

    fun iniciarSesion() {
        val estado = _uiState.value
        val correo = estado.usuario.trim()

        val usuarioEncontrado = usuarios.find {
            it.correo.equals(correo, ignoreCase = true)
        }

        val errorUsuario = when {
            correo.isBlank() -> "Ingrese su correo"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                "Correo electronico no valido"
            usuarioEncontrado == null -> "Correo no registrado"
            else -> null
        }

        val errorContrasena = when {
            estado.contrasena.isBlank() -> "Ingrese su contrasena"
            usuarioEncontrado != null &&
                    estado.contrasena != usuarioEncontrado.contrasena ->
                "Contrasena incorrecta"
            else -> null
        }

        val correcto = errorUsuario == null &&
                errorContrasena == null &&
                usuarioEncontrado != null

        usuarioActual = if (correcto) usuarioEncontrado else null

        _uiState.update {
            it.copy(
                errorUsuario = errorUsuario,
                errorContrasena = errorContrasena,
                inicioExitoso = correcto,
                mensaje = if (correcto) {
                    "Bienvenido a Rincon Sur"
                } else {
                    "Revise sus datos"
                }
            )
        }
    }

    fun cambiarContrasena(
        contrasenaActual: String,
        nuevaContrasena: String
    ): Pair<Boolean, String> {

        val usuario = usuarioActual
            ?: return false to "Debe iniciar sesion"

        if (contrasenaActual.isBlank()) {
            return false to "Ingrese su contrasena actual"
        }

        if (contrasenaActual != usuario.contrasena) {
            return false to "La contrasena actual es incorrecta"
        }

        if (nuevaContrasena.length < 4) {
            return false to "La nueva contrasena debe tener al menos 4 caracteres"
        }

        if (nuevaContrasena == contrasenaActual) {
            return false to "La nueva contrasena debe ser diferente"
        }

        val indice = usuarios.indexOfFirst {
            it.id == usuario.id
        }

        if (indice == -1) {
            return false to "Usuario no encontrado"
        }

        val actualizado = usuario.copy(
            contrasena = nuevaContrasena
        )

        val guardado = preferencias.edit()
            .putString(
                "clave_${usuario.correo.lowercase()}",
                nuevaContrasena
            )
            .commit()

        if (!guardado) {
            return false to "No se pudo guardar la contrasena"
        }

        usuarios[indice] = actualizado
        usuarioActual = actualizado

        return true to "Contrasena actualizada correctamente"
    }

    fun recuperarContrasena(
        usuario: String,
        nuevaContrasena: String
    ): Pair<Boolean, String> {

        val correo = usuario.trim()

        val indice = usuarios.indexOfFirst {
            it.correo.equals(correo, ignoreCase = true)
        }

        if (indice == -1) {
            return false to "Correo no encontrado"
        }

        if (nuevaContrasena.length < 4) {
            return false to "La contrasena debe tener al menos 4 caracteres"
        }

        val usuarioActualizado = usuarios[indice].copy(
            contrasena = nuevaContrasena
        )

        val guardado = preferencias.edit()
            .putString(
                "clave_${usuarioActualizado.correo.lowercase()}",
                nuevaContrasena
            )
            .commit()

        if (!guardado) {
            return false to "No se pudo actualizar la contrasena"
        }

        usuarios[indice] = usuarioActualizado
        usuarioActual = null

        _uiState.update {
            it.copy(
                usuario = correo,
                contrasena = "",
                errorUsuario = null,
                errorContrasena = null,
                inicioExitoso = false,
                mensaje = "Contrasena actualizada"
            )
        }

        return true to "Contrasena actualizada"
    }

    fun cerrarSesion() {
        usuarioActual = null
        _uiState.value = LoginUiState()
    }
}

