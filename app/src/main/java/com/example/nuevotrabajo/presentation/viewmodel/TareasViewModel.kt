package com.example.nuevotrabajo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nuevotrabajo.data.repository.ImpRepository
import com.example.nuevotrabajo.data.local.database.AppDatabase
import com.example.nuevotrabajo.domain.usecase.CambiarEstadoTareaUseCase
import com.example.nuevotrabajo.domain.usecase.CrearTareaUseCase
import com.example.nuevotrabajo.domain.usecase.EliminarTareaUseCase // 1. Importa tu caso de uso de eliminar
import com.example.nuevotrabajo.domain.usecase.ObtenerTareasUseCase
import com.example.nuevotrabajo.domain.model.Tarea
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class TareasViewModel(
    private val obtenerTareasUseCase: ObtenerTareasUseCase,
    private val crearTareaUseCase: CrearTareaUseCase,
    private val cambiarEstadoTareaUseCase: CambiarEstadoTareaUseCase,
    private val eliminarTareaUseCase: EliminarTareaUseCase // 2. Añádelo al constructor
) : ViewModel() {

    val estadoTareas: StateFlow<List<Tarea>> = obtenerTareasUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    // 3. Agrega la función que invocará la UI al hacer clic en el botón de eliminar
    fun eliminarTarea(idTarea: String) {
        viewModelScope.launch {
            eliminarTareaUseCase(idTarea)
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
                val eliminarTarea = EliminarTareaUseCase(repository) // 4. Instancia el caso de uso en la Factory

                return TareasViewModel(
                    obtenerTareasUseCase = obtenerTareas,
                    crearTareaUseCase = crearTarea,
                    cambiarEstadoTareaUseCase = cambiarEstado,
                    eliminarTareaUseCase = eliminarTarea // 5. Pásalo al constructor del ViewModel
                ) as T
            }
        }
    }
}