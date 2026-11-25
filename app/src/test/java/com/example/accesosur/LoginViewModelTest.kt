
package com.viewmodel

import android.app.Application
import com.model.LoginUiState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        val application: Application =
            RuntimeEnvironment.getApplication()

        viewModel = LoginViewModel(application)
    }

    @Test
    fun verificarEstadoInicial() {
        val estado = viewModel.uiState.value

        assertEquals("", estado.usuario)
        assertEquals("", estado.contrasena)
        assertFalse(estado.inicioExitoso)
        assertNull(estado.errorUsuario)
        assertNull(estado.errorContrasena)
    }

    @Test
    fun verificarIngresoCorreo() {
        viewModel.onUsuarioChange("observador@rinconsur.cl")

        assertEquals(
            "observador@rinconsur.cl",
            viewModel.uiState.value.usuario
        )

        assertNull(viewModel.uiState.value.errorUsuario)
    }

    @Test
    fun verificarIngresoContrasena() {
        viewModel.onContrasenaChange("1234")

        assertEquals(
            "1234",
            viewModel.uiState.value.contrasena
        )

        assertNull(viewModel.uiState.value.errorContrasena)
    }

    @Test
    fun validarCredencialesVacias() {
        viewModel.onUsuarioChange("")
        viewModel.onContrasenaChange("")

        viewModel.iniciarSesion()

        val estado = viewModel.uiState.value

        assertEquals("Ingrese correo", estado.errorUsuario)
        assertEquals("Ingrese contraseña", estado.errorContrasena)
        assertFalse(estado.inicioExitoso)
    }
    
    @Test
    fun verificarCerrarSesion() {
        viewModel.onUsuarioChange("supervisor@rinconsur.cl")
        viewModel.onContrasenaChange("1234")

        viewModel.cerrarSesion()

        assertEquals(
            LoginUiState(),
            viewModel.uiState.value
        )

        assertNull(viewModel.obtenerUsuarioActual())
        assertNull(viewModel.obtenerRolActual())
    }
}
