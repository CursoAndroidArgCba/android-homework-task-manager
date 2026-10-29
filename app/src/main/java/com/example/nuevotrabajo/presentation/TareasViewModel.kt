package com.example.nuevotrabajo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
 * Además, se aplica el Principio de Inversión de Dependencias (DIP) inyectando
 * los Casos de Uso mediante el constructor en lugar de instanciarlos dentro de la clase.
 */
class TareasViewModel(
    private val obtenerTareasUseCase: ObtenerTareasUseCase,
    private val crearTareaUseCase: CrearTareaUseCase,
    private val cambiarEstadoTareaUseCase: CambiarEstadoTareaUseCase
) : ViewModel() { // Se hereda de ViewModel() en vez de AndroidViewModel, ya no necesitamos el contexto aquí (mejor Clean Architecture).

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

    /**
     * Factoría para inyectar dependencias manualmente al ViewModel.
     * Al usar este patrón cumplimos con el Principio de Inversión de Dependencias (DIP).
     * Evitamos que el ViewModel cree sus propias dependencias, haciéndolo 100% testeable.
     */
    class Factory(
        private val obtenerTareasUseCase: ObtenerTareasUseCase,
        private val crearTareaUseCase: CrearTareaUseCase,
        private val cambiarEstadoTareaUseCase: CambiarEstadoTareaUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
                return TareasViewModel(
                    obtenerTareasUseCase,
                    crearTareaUseCase,
                    cambiarEstadoTareaUseCase
                ) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}
