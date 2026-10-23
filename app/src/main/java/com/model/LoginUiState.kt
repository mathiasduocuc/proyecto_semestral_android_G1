package com.model

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val errorUsuario: String? = null,
    val errorContrasena: String? = null,
    val mensaje: String = "",
    val inicioExitoso: Boolean = false
)