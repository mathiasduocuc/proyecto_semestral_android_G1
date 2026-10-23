package com.model

enum class ClasificacionCliente(val etiqueta: String) {
    TRANSITO("Transito"),
    VISITA("Visita"),
    NO_DETERMINADO("No determinado")
}

data class FormularioUiState (


    val acceso: String = "",

    val clasificacion: ClasificacionCliente? = null,

    val rangoEtario: String = "",

    val tipoCompania: String = "",

    val comprasOtraTienda: Boolean = false,

    val errorAcceso: String? = null,
    val errorClasificacion: String? = null,
    val errorRangoEtario: String? = null,
    val errorTipoCompania: String? = null,

    val mensaje: String = "",

    val registroExitoso: Boolean = false

)