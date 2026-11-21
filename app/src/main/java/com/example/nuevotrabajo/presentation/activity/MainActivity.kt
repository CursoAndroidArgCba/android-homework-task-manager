package com.example.nuevotrabajo.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nuevotrabajo.presentation.screen.PantallaTareas
import com.example.nuevotrabajo.presentation.theme.NuevoTrabajoTheme
import com.example.nuevotrabajo.presentation.viewmodel.TareasViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TareasViewModel by viewModels { TareasViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NuevoTrabajoTheme {
                val tareas by viewModel.estadoTareas.collectAsStateWithLifecycle()

                PantallaTareas(
                    tareas = tareas,
                    onAgregarTarea = { titulo, desc -> viewModel.agregarNuevaTarea(titulo, desc) },
                    onCambiarEstado = { id -> viewModel.alternarEstado(id) },
                    onEliminarTarea = { id -> viewModel.eliminarTarea(id) },
                    onCompletarTarea = { id -> viewModel.completeTask(id) },
                    onEditarTarea = { id, nuevoTitulo, nuevaDesc ->
                        viewModel.editarTarea(id, nuevoTitulo, nuevaDesc)
                    }
                )
            }
        }
    }
}