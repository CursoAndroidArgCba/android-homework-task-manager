package com.example.nuevotrabajo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nuevotrabajo.data.local.database.AppDatabase
import com.example.nuevotrabajo.data.repository.ImpRepository
import com.example.nuevotrabajo.domain.model.Tarea
import com.example.nuevotrabajo.domain.usecase.CambiarEstadoTareaUseCase
import com.example.nuevotrabajo.domain.usecase.CrearTareaUseCase
import com.example.nuevotrabajo.domain.usecase.EliminarTareaUseCase
import com.example.nuevotrabajo.domain.usecase.ObtenerTareasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class TareasViewModel(
    private val obtenerTareasUseCase: ObtenerTareasUseCase,
    private val crearTareaUseCase: CrearTareaUseCase,
    private val cambiarEstadoTareaUseCase: CambiarEstadoTareaUseCase,
    private val eliminarTareaUseCase: EliminarTareaUseCase
) : ViewModel() {

    val estadoTareas: StateFlow<List<Tarea>> = obtenerTareasUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _ultimaTareaCompletada = MutableStateFlow<String?>(null)
    val ultimaTareaCompletada: StateFlow<String?> = _ultimaTareaCompletada.asStateFlow()

    fun agregarNuevaTarea(titulo: String, descripcion: String?) {
        viewModelScope.launch {
            val nueva = Tarea(
                id = UUID.randomUUID().toString(),
                titulo = titulo,
                descripcion = descripcion,
                estaCompletada = false
            )
            crearTareaUseCase(nueva)
        }
    }

    fun alternarEstado(idTarea: String) {
        viewModelScope.launch {
            cambiarEstadoTareaUseCase(idTarea)
        }
    }

    fun eliminarTarea(idTarea: String) {
        viewModelScope.launch {
            eliminarTareaUseCase(idTarea)
        }
    }

    fun completeTask(taskId: String) {
        Log.d("TareasViewModel", "Tarea completada con ID: $taskId")
        _ultimaTareaCompletada.value = taskId
    }

    fun editarTarea(id: String, nuevoTitulo: String, nuevaDescripcion: String?) {
        viewModelScope.launch {
            val tareaActual = estadoTareas.value.find { it.id == id }
            val tareaActualizada = Tarea(
                id = id,
                titulo = tareaActual?.titulo ?: nuevoTitulo,
                descripcion = nuevaDescripcion?.ifBlank { null },
                estaCompletada = tareaActual?.estaCompletada ?: false
            )
            crearTareaUseCase(tareaActualizada)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                val database = AppDatabase.getDatabase(application)
                val repository = ImpRepository(database.tareaDao())

                val obtenerTareas = ObtenerTareasUseCase(repository)
                val crearTarea = CrearTareaUseCase(repository)
                val cambiarEstado = CambiarEstadoTareaUseCase(repository)
                val eliminarTarea = EliminarTareaUseCase(repository)

                return TareasViewModel(
                    obtenerTareasUseCase = obtenerTareas,
                    crearTareaUseCase = crearTarea,
                    cambiarEstadoTareaUseCase = cambiarEstado,
                    eliminarTareaUseCase = eliminarTarea
                ) as T
            }
        }
    }
}