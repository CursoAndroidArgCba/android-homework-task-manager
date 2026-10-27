package com.example.nuevotrabajo.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.nuevotrabajo.presentation.PantallaTareas
import com.example.nuevotrabajo.presentation.TareasViewModel
import com.example.nuevotrabajo.ui.theme.NuevoTrabajoTheme
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {

    private val viewModel: TareasViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NuevoTrabajoTheme {
                val tareas by viewModel.estadoTareas.collectAsState()

                PantallaTareas(
                    tareas = tareas,
                    onAgregarTarea = { titulo, desc -> viewModel.agregarNuevaTarea(titulo, desc) },
                    onCambiarEstado = { id -> viewModel.alternarEstado(id) }
                )
            }
        }
    }
}