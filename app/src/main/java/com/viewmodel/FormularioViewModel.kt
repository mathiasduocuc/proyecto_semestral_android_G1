
package com.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.data.Registro
import com.data.RegistroDbHelper
import com.model.ClasificacionCliente
import com.model.FormularioUiState
import com.notifications.NotificacionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormularioViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = RegistroDbHelper(application.applicationContext)

    private val _uiState = MutableStateFlow(FormularioUiState())

    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    private var correoObservador = ""

    fun establecerObservador(correo: String) {
        correoObservador = correo
    }

    fun onAccesoChange(valor: String) {
        _uiState.update {
            it.copy(
                acceso = valor,
                errorAcceso = null,
                mensaje = "",
                registroExitoso = false
            )
        }
    }

    fun onClasificacionChange(valor: ClasificacionCliente) {
        _uiState.update {
            it.copy(
                clasificacion = valor,
                errorClasificacion = null,
                mensaje = "",
                registroExitoso = false
            )
        }
    }

    fun onRangoEtarioChange(valor: String) {
        _uiState.update {
            it.copy(
                rangoEtario = valor,
                errorRangoEtario = null,
                mensaje = "",
                registroExitoso = false
            )
        }
    }

    fun onTipoCompaniaChange(valor: String) {
        _uiState.update {
            it.copy(
                tipoCompania = valor,
                errorTipoCompania = null,
                mensaje = "",
                registroExitoso = false
            )
        }
    }

    fun onComprasOtraTiendaChange(valor: Boolean) {
        _uiState.update {
            it.copy(
                comprasOtraTienda = valor,
                mensaje = "",
                registroExitoso = false
            )
        }
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value

        val errorAcceso =
            if (estado.acceso.isBlank()) "Debe seleccionar un acceso" else null

        val errorClasificacion =
            if (estado.clasificacion == null) "Debe seleccionar una clasificacion" else null

        val errorRangoEtario =
            if (estado.rangoEtario.isBlank()) "Debe seleccionar un rango etario" else null

        val errorTipoCompania =
            if (estado.tipoCompania.isBlank()) "Debe seleccionar el tipo de compania" else null

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

        if (!validarFormulario()) {
            _uiState.update {
                it.copy(
                    mensaje = "Complete los campos obligatorios",
                    registroExitoso = false
                )
            }
            return
        }

        if (correoObservador.isBlank()) {
            _uiState.update {
                it.copy(
                    mensaje = "No se ha identificado al observador",
                    registroExitoso = false
                )
            }
            return
        }

        val estado = _uiState.value

        val fechaActual = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).format(Date())

        val horaActual = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val registro = Registro(
            acceso = estado.acceso,
            clasificacion = estado.clasificacion!!.etiqueta,
            rangoEtario = estado.rangoEtario,
            tipoCompania = estado.tipoCompania,
            comprasOtraTienda = estado.comprasOtraTienda,
            fecha = fechaActual,
            hora = horaActual,
            observador = correoObservador
        )

        try {
            val idRegistro = dbHelper.insertarRegistro(registro)

            if (idRegistro != -1L) {

                _uiState.update {
                    it.copy(
                        mensaje = "Observacion guardada en SQLite correctamente",
                        registroExitoso = true
                    )
                }

                NotificacionHelper.mostrarNotificacion(
                    getApplication()
                )

            } else {
                _uiState.update {
                    it.copy(
                        mensaje = "No se pudo guardar la observacion",
                        registroExitoso = false
                    )
                }
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    mensaje = "Error al guardar en la base de datos",
                    registroExitoso = false
                )
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = FormularioUiState()
    }

    override fun onCleared() {
        dbHelper.close()
        super.onCleared()
    }
}



