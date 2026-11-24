package com.model


data class Turno(
    val id: Long = 0,
    val correoObservador: String,
    val acceso: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val activo: Boolean = true
)
