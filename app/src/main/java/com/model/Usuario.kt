package com.model

data class Usuario(
    val id: Long,
    val nombre: String? = null,
    val correo: String,
    val contrasena: String,
    val rol: Rol
)
