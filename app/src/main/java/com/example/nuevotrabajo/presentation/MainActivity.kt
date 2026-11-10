package com.example.nuevotrabajo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nuevotrabajo.domain.CambiarEstadoTareaUseCase
import com.example.nuevotrabajo.domain.CrearTareaUseCase
import com.example.nuevotrabajo.domain.ObtenerTareasUseCase
import com.example.nuevotrabajo.ui.theme.NuevoTrabajoTheme

class MainActivity : ComponentActivity() {

    // Se instancian las dependencias (Database, Repository, UseCases) y se inyectan 
    // a través del Factory manual, aplicando el Principio de Inversión de Dependencias (DIP).
    // Ahora MainActivity ensambla las piezas, y el ViewModel no sabe de dónde vienen los datos.
    private val viewModel: TareasViewModel by viewModels { TareasViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NuevoTrabajoTheme {
                // collectAsStateWithLifecycle respeta el ciclo de vida de la UI, 
                // pausando la recolección de datos cuando la app está en segundo plano.
                // Esto optimiza el uso de batería y recursos del dispositivo.
                val tareas by viewModel.estadoTareas.collectAsStateWithLifecycle()

                PantallaTareas(
                    tareas = tareas,
                    onAgregarTarea = { titulo, desc -> viewModel.agregarNuevaTarea(titulo, desc) },
                    onCambiarEstado = { id -> viewModel.alternarEstado(id) },
                    onEliminarTarea = { id -> viewModel.eliminarTarea(id) } // <-- AQUÍ SE AGREGA
                )
            }
        }
    }
}