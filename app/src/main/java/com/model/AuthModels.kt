package com.model

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class LoginResponse(
    val token: String,
    val rol: String,
    val correo: String
)

data class RegisterRequest(
    val correo: String,
    val contrasena: String,
    val rol: String = "OBSERVADOR"
)

data class RecoverRequest(
    val correo: String,
    val nuevaContrasena: String
)