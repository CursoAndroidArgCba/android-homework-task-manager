package com.example.nuevotrabajo.data.mock

import com.example.nuevotrabajo.domain.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * CAPA: Data : se encarga de conectar la aplicacion con el mundo real.
 *
 * 1. ¿Qué representa?: Representa la fuente de datos
 *
 *
 * 2. ¿Por qué se utiliza esta estructura?: Se implementa mediante un StateFlow para manejar flujos de datos
 * reactivos y dinámicos que la UI pueda observar en tiempo real.
 *
 * 3. Principio SOLID aplicado: Principio Abierto/Cerrado (OCP) y Sustitución de Liskov (LSP),
 * ya que podemos cambiar o agregar nuevas fuentes de datos (ej. Room) sin modificar la capa de Dominio.
 */
class DatabaseMock {
    private val _tareasFlow = MutableStateFlow<List<Tarea>>(emptyList())

    fun obtenerTareas(): Flow<List<Tarea>> = _tareasFlow.asStateFlow()

    fun guardarTarea(tarea: Tarea) {
        val listaActual = _tareasFlow.value.toMutableList()
        listaActual.add(tarea)
        _tareasFlow.value = listaActual
    }

    fun cambiarEstado(idTarea: String) {
        val listaActual = _tareasFlow.value.map { tarea ->
            if (tarea.id == idTarea) tarea.copy(estaCompletada = !tarea.estaCompletada) else tarea
        }
        _tareasFlow.value = listaActual
    }
}