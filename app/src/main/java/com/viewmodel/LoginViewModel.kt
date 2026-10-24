package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private var contrasenaCorrecta = "1234"

    fun onUsuarioChange(valor: String) {
        _uiState.update {
            it.copy(usuario = valor, errorUsuario = null, mensaje = "")
        }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update {
            it.copy(contrasena = valor, errorContrasena = null, mensaje = "")
        }
    }

    fun iniciarSesion() {
        val estado = _uiState.value

        val errorUsuario = when {
            estado.usuario.isBlank() -> "Ingrese su usuario"
            estado.usuario != "observador" -> "Usuario incorrecto"
            else -> null
        }

        val errorContrasena = when {
            estado.contrasena.isBlank() -> "Ingrese su contrasena"
            estado.contrasena != contrasenaCorrecta -> "Contrasena incorrecta"
            else -> null
        }

        val correcto = errorUsuario == null && errorContrasena == null

        _uiState.update {
            it.copy(
                errorUsuario = errorUsuario,
                errorContrasena = errorContrasena,
                inicioExitoso = correcto,
                mensaje = if (correcto) "Bienvenido a Rincon Sur" else "Revise sus datos"
            )
        }
    }

    /**
     * Recuperación de demostración local.
     * En una aplicación real se debe verificar la identidad mediante un servidor,
     * por ejemplo con un enlace o código enviado al correo registrado.
     */
    fun recuperarContrasena(usuario: String, nuevaContrasena: String): Pair<Boolean, String> {
        if (usuario != "observador") {
            return false to "No se encontró ese usuario."
        }

        contrasenaCorrecta = nuevaContrasena
        _uiState.update {
            it.copy(
                usuario = usuario,
                contrasena = "",
                errorUsuario = null,
                errorContrasena = null,
                inicioExitoso = false,
                mensaje = "Contraseña actualizada. Inicia sesión con tu nueva clave."
            )
        }
        return true to "Contraseña actualizada. Ahora inicia sesión con tu nueva clave."
    }

    fun cerrarSesion() {
        _uiState.value = LoginUiState()
    }

}
