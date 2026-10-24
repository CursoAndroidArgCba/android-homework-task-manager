package com.example.nuevotrabajo.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nuevotrabajo.data.ImpRepository
import com.example.nuevotrabajo.data.localdatabase.AppDatabase
import com.example.nuevotrabajo.domain.CambiarEstadoTareaUseCase
import com.example.nuevotrabajo.domain.CrearTareaUseCase
import com.example.nuevotrabajo.domain.ObtenerTareasUseCase
import com.example.nuevotrabajo.domain.Tarea
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * CAPA: ViewModel: es el intermediario entre la ui y el dominio. Se encarga de gestionar el estado
 * de la pantalla y procesar las acciones del usuario.
 *
 * 2. ¿Por qué se utiliza esta estructura?: Porque el ViewModel sobrevive a los cambios
 * de configuración del celular (como rotar la pantalla) y utiliza un StateFlow
 * para avisarle a la UI de forma reactiva cuando hay cambios.
 *
 * 3. Principio SOLID aplicado: Principio de Responsabilidad Única (SRP), ya que el ViewModel
 * separa la lógica de la interfaz visual y delega todas las reglas de negocio
 * estrictamente en los Casos de Uso.
 */
class TareasViewModel(
    application: Application
) : AndroidViewModel(application) {

    val database = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
    val repository = ImpRepository(database.tareaDao()) // Asegúrate de que el método del DAO coincida con tu interfaz

    val obtenerTareasUseCase = ObtenerTareasUseCase(repository)
    val crearTareaUseCase = CrearTareaUseCase(repository)
    val cambiarEstadoTareaUseCase = CambiarEstadoTareaUseCase(repository)

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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return TareasViewModel(application) as T
            }
        }
    }
}