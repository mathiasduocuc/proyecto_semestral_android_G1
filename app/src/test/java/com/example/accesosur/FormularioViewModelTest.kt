
package com.viewmodel

import android.app.Application
import com.model.ClasificacionCliente
import com.model.FormularioUiState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class FormularioViewModelTest {

    private lateinit var viewModel: FormularioViewModel

    @Before
    fun setUp() {
        val application: Application =
            RuntimeEnvironment.getApplication()

        viewModel = FormularioViewModel(application)
    }

    @Test
    fun verificarSeleccionAcceso() {
        viewModel.onAccesoChange("Acceso 1")

        assertEquals(
            "Acceso 1",
            viewModel.uiState.value.acceso
        )

        assertNull(viewModel.uiState.value.errorAcceso)
    }

    @Test
    fun verificarClasificacionCliente() {
        viewModel.onClasificacionChange(
            ClasificacionCliente.TRANSITO
        )

        assertEquals(
            ClasificacionCliente.TRANSITO,
            viewModel.uiState.value.clasificacion
        )

        assertNull(
            viewModel.uiState.value.errorClasificacion
        )
    }

    @Test
    fun validarFormularioVacio() {
        val resultado = viewModel.validarFormulario()
        val estado = viewModel.uiState.value

        assertFalse(resultado)

        assertEquals(
            "Debe seleccionar un acceso",
            estado.errorAcceso
        )

        assertEquals(
            "Debe seleccionar una clasificacion",
            estado.errorClasificacion
        )

        assertEquals(
            "Debe seleccionar un rango etario",
            estado.errorRangoEtario
        )

        assertEquals(
            "Debe seleccionar el tipo de compania",
            estado.errorTipoCompania
        )
    }

    @Test
    fun validarFormularioCompleto() {
        viewModel.onAccesoChange("Acceso 1")

        viewModel.onClasificacionChange(
            ClasificacionCliente.VISITA
        )

        viewModel.onRangoEtarioChange("Adulto")
        viewModel.onTipoCompaniaChange("Solo")
        viewModel.onComprasOtraTiendaChange(true)

        val resultado = viewModel.validarFormulario()
        val estado = viewModel.uiState.value

        assertTrue(resultado)

        assertNull(estado.errorAcceso)
        assertNull(estado.errorClasificacion)
        assertNull(estado.errorRangoEtario)
        assertNull(estado.errorTipoCompania)
        assertTrue(estado.comprasOtraTienda)
    }

    @Test
    fun verificarLimpiezaFormulario() {
        viewModel.onAccesoChange("Acceso 2")

        viewModel.onClasificacionChange(
            ClasificacionCliente.VISITA
        )

        viewModel.onRangoEtarioChange("Adulto")
        viewModel.onTipoCompaniaChange("Solo")
        viewModel.onComprasOtraTiendaChange(true)

        viewModel.limpiarFormulario()

        assertEquals(
            FormularioUiState(),
            viewModel.uiState.value
        )
    }
}
