
package com.viewmodel

import androidx.lifecycle.ViewModel
import com.model.Turno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TurnoViewModel : ViewModel() {

    private val _turnos = MutableStateFlow<List<Turno>>(emptyList())
    val turnos = _turnos.asStateFlow()

    var ultimoError: String = ""
        private set

    fun asignarTurno(
        correoObservador: String,
        acceso: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ): Boolean {

        ultimoError = ""

        val correo = correoObservador.trim().lowercase()

        if (correo.isBlank() || acceso.isBlank() ||
            fecha.isBlank() || horaInicio.isBlank() ||
            horaFin.isBlank()
        ) {
            ultimoError = "Complete todos los campos"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            ultimoError = "Ingrese un correo valido"
            return false
        }

        if (acceso !in listOf("A1", "A2", "A3", "A4")) {
            ultimoError = "Seleccione un acceso valido"
            return false
        }

        val formatoFecha = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.US
        ).apply {
            isLenient = false
        }

        val fechaTurno = try {
            formatoFecha.parse(fecha)
        } catch (e: Exception) {
            null
        }

        if (fechaTurno == null) {
            ultimoError = "Fecha no valida"
            return false
        }

        val hoy = formatoFecha.parse(
            formatoFecha.format(Date())
        )!!

        if (fechaTurno.before(hoy)) {
            ultimoError = "No puede asignar turnos en fechas pasadas"
            return false
        }

        val formatoHora = SimpleDateFormat(
            "HH:mm",
            Locale.US
        ).apply {
            isLenient = false
        }

        val inicio = try {
            formatoHora.parse(horaInicio)
        } catch (e: Exception) {
            null
        }

        val fin = try {
            formatoHora.parse(horaFin)
        } catch (e: Exception) {
            null
        }

        if (inicio == null || fin == null || !inicio.before(fin)) {
            ultimoError = "El horario seleccionado no es valido"
            return false
        }

        val ahora = Date()

        if (fechaTurno == hoy) {
            val horaActual = formatoHora.format(ahora)

            if (horaInicio <= horaActual) {
                ultimoError = "La hora de inicio debe ser futura"
                return false
            }
        }

        val turnoSuperpuesto = _turnos.value.any { turno ->
            turno.activo &&
                    turno.correoObservador.equals(correo, true) &&
                    turno.fecha == fecha &&
                    horaInicio < turno.horaFin &&
                    horaFin > turno.horaInicio
        }

        if (turnoSuperpuesto) {
            ultimoError = "El observador ya tiene un turno en ese horario"
            return false
        }

        val nuevoTurno = Turno(
            id = System.currentTimeMillis(),
            correoObservador = correo,
            acceso = acceso,
            fecha = fecha,
            horaInicio = horaInicio,
            horaFin = horaFin
        )

        _turnos.update { lista ->
            lista + nuevoTurno
        }

        return true
    }

    fun obtenerTurno(
        correoObservador: String,
        fecha: String,
        horaActual: String
    ): Turno? {

        return _turnos.value.find { turno ->
            turno.correoObservador.equals(
                correoObservador.trim(),
                ignoreCase = true
            ) &&
                    turno.fecha == fecha &&
                    turno.activo &&
                    horaActual >= turno.horaInicio &&
                    horaActual < turno.horaFin
        }
    }

    fun desactivarTurno(id: Long) {
        _turnos.update { lista ->
            lista.map { turno ->
                if (turno.id == id) {
                    turno.copy(activo = false)
                } else {
                    turno
                }
            }
        }
    }
}


