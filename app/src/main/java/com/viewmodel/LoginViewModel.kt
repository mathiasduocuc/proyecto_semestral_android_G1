package com.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.data.RetrofitClient
import com.model.LoginRequest
import com.model.LoginUiState
import com.model.RecoverRequest
import com.model.RegisterRequest
import com.model.Rol
import com.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val preferencias = application.getSharedPreferences(
        "cuentas_demo",
        Context.MODE_PRIVATE
    )

    private var usuarioActual: Usuario? = null

    var listaObservadores by mutableStateOf<List<Usuario>>(emptyList())
        private set

    fun cargarObservadores() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.getUsers()
                if (response.isSuccessful) {
                    val todosLosUsuarios = response.body() ?: emptyList()
                    // Filtramos solo los observadores
                    listaObservadores = todosLosUsuarios.filter { it.rol == Rol.OBSERVADOR }
                    Log.d("API_USERS", "Usuarios cargados: ${listaObservadores.size}")
                } else {
                    Log.e("API_USERS", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                // IMPORTANTE: Esto te dirá en el Logcat si Retrofit falló al leer el JSON
                Log.e("API_USERS", "Fallo al cargar usuarios: ${e.message}", e)
            }
        }
    }

    fun obtenerUsuarioActual(): Usuario? = usuarioActual

    fun obtenerRolActual(): Rol? = usuarioActual?.rol

    fun onUsuarioChange(valor: String) {
        _uiState.update { it.copy(usuario = valor, errorUsuario = null, inicioExitoso = false, mensaje = "") }
    }

    fun onContrasenaChange(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, inicioExitoso = false, mensaje = "") }
    }

    fun iniciarSesion() {
        val estado = _uiState.value
        val correo = estado.usuario.trim()
        val contrasena = estado.contrasena

        if (correo.isBlank() || contrasena.isBlank()) {
            _uiState.update { it.copy(errorUsuario = "Ingrese correo", errorContrasena = "Ingrese contraseña") }
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.login(LoginRequest(correo, contrasena))

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        preferencias.edit()
                            .putString("jwt_token", data.token)
                            .putString("user_correo", data.correo)
                            .putString("user_rol", data.rol)
                            .apply()

                        usuarioActual = Usuario(
                            id = 0,
                            nombre = if(data.rol == "SUPERVISOR") "Supervisor" else "Observador",
                            correo = data.correo,
                            contrasena = "",
                            rol = if (data.rol == "SUPERVISOR") Rol.SUPERVISOR else Rol.OBSERVADOR
                        )

                        _uiState.update { it.copy(inicioExitoso = true, mensaje = "Bienvenido a Rincon Sur") }
                    }
                } else {
                    _uiState.update { it.copy(inicioExitoso = false, mensaje = "Credenciales incorrectas") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(inicioExitoso = false, mensaje = "Error de conexión al servidor") }
            }
        }
    }

    fun recuperarContrasena(
        correo: String,
        nuevaContrasena: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.recoverPassword(
                    RecoverRequest(correo, nuevaContrasena)
                )

                if (response.isSuccessful) {
                    _uiState.update { it.copy(usuario = correo, contrasena = "", mensaje = "Contraseña actualizada") }
                    onResult(true, "Contraseña actualizada correctamente")
                } else {
                    onResult(false, "Correo no encontrado en el servidor")
                }
            } catch (e: Exception) {
                onResult(false, "Error de conexión al servidor")
            }
        }
    }

    fun cambiarContrasena(
        contrasenaActual: String,
        nuevaContrasena: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val correo = usuarioActual?.correo ?: preferencias.getString("user_correo", "") ?: ""

        if (correo.isBlank()) {
            onResult(false, "Debe iniciar sesión")
            return
        }
        if (nuevaContrasena.length < 4) {
            onResult(false, "La nueva contrasena debe tener al menos 4 caracteres")
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.recoverPassword(
                    RecoverRequest(correo, nuevaContrasena)
                )
                if (response.isSuccessful) {
                    onResult(true, "Contrasena actualizada correctamente")
                } else {
                    onResult(false, "No se pudo actualizar la contrasena en el servidor")
                }
            } catch (e: Exception) {
                onResult(false, "Error de conexión al servidor")
            }
        }
    }

    fun crearNuevoObservador(correo: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.register(RegisterRequest(correo, contrasena, "OBSERVADOR"))
                if (response.isSuccessful) {
                    cargarObservadores()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun modificarContrasenaObservador(correo: String, nuevaContrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.recoverPassword(RecoverRequest(correo, nuevaContrasena))
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun eliminarUsuario(correo: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.deleteUser(correo)
                if (response.isSuccessful) {
                    cargarObservadores()
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun cerrarSesion() {
        usuarioActual = null
        preferencias.edit().clear().apply()
        _uiState.value = LoginUiState()
    }
}