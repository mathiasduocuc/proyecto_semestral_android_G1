package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.ClasificacionCliente
import com.model.FormularioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormularioViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(FormularioUiState())

    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()


    fun onAccesoChange(valor: String) {
        _uiState.update { it.copy(acceso = valor, errorAcceso = null) }
    }

    fun onClasificacionChange(valor: ClasificacionCliente) {
        _uiState.update { it.copy(clasificacion = valor, errorClasificacion = null) }
    }

    fun onRangoEtarioChange(valor: String) {
        _uiState.update { it.copy(rangoEtario = valor, errorRangoEtario = null) }
    }


    fun onTipoCompaniaChange(valor: String) {
        _uiState.update { it.copy(tipoCompania = valor, errorTipoCompania = null) }
    }

    fun onComprasOtraTiendaChange(valor: Boolean){
        _uiState.update { it.copy(comprasOtraTienda = valor) }
    }

    fun validarFormulario(): Boolean{
        val estado = _uiState.value
        val errorAcceso = if(estado.acceso.isBlank()) "Debe seleccionar un acceso" else null
        val errorClasificacion = if(estado.clasificacion == null) "Debe seleccionar una clasificación" else null
        val errorRangoEtario = if (estado.rangoEtario.isBlank()) "Debe seleccionar un rango etario" else null
        val errorTipoCompania = if (estado.tipoCompania.isBlank()) "Debe seleccionar el tipo de compañía" else null

        _uiState.update {
            it.copy(
                errorAcceso = errorAcceso,
                errorClasificacion = errorClasificacion,
                errorRangoEtario = errorRangoEtario,
                errorTipoCompania = errorTipoCompania
            )
        }

        return errorAcceso == null &&
                errorClasificacion == null &&
                errorRangoEtario == null &&
                errorTipoCompania == null



    }

    fun guardarObservacion() {
        if (validarFormulario()) {
            _uiState.update {
                it.copy(
                    mensaje = "Observación registrada correctamente",
                    registroExitoso = true
                )
            }

        } else {
            _uiState.update {
                it.copy(
                    mensaje = "Complete los campos obligatorios",
                    registroExitoso = false
                )
            }
        }
    }
        fun limpiarFormulario() {
            _uiState.value = FormularioUiState()
    }

}



