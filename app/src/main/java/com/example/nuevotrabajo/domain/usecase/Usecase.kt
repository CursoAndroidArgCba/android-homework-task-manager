package com.example.nuevotrabajo.domain.usecase

import com.example.nuevotrabajo.domain.repository.Repository
import com.example.nuevotrabajo.domain.model.Tarea

/**
 * CAPA: Use Case
 *
 * 1. ¿Qué representa?: Se establece las tareas que realiza el programa
 *
 * 2. ¿Por qué se utiliza esta estructura?: Se utiliza una clase normal ya que son tareas especificas
 * y asi mantenemos de manera ordenada el codigo
 *
 * 3. Principio SOLID aplicado: Principio de Responsabilidad Única (SRP). Esta clase tiene una
 * única razón para cambiar: modificaciones en la regla de negocio para obtener las tareas.
 */
class ObtenerTareasUseCase(private val repository: Repository) {
    operator fun invoke() = repository.obtenerTareas()
}

class CrearTareaUseCase(private val repository: Repository) {
    suspend operator fun invoke(tarea: Tarea) {
        repository.guardarTarea(tarea)
    }
}

class CambiarEstadoTareaUseCase(private val repository: Repository) {
    suspend operator fun invoke(idTarea: String) {
        repository.cambiarEstadoTarea(idTarea)
    }
}
class EliminarTareaUseCase(
    private val repository: Repository // O el nombre de tu interfaz en Repository.kt
) {
    suspend operator fun invoke(idTarea: String) {
        repository.eliminarTarea(idTarea)
    }
}