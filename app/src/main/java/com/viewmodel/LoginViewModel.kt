
package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        _uiState.update {
            it.copy(
                usuario = valor,
                errorUsuario = null,
                mensaje = ""
            )
        }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update {
            it.copy(
                contrasena = valor,
                errorContrasena = null,
                mensaje = ""
            )
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
            estado.contrasena != "1234" -> "Contrasena incorrecta"
            else -> null
        }

        val correcto = errorUsuario == null &&
                errorContrasena == null

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

    fun cerrarSesion() {
        _uiState.value = LoginUiState()
    }
}
