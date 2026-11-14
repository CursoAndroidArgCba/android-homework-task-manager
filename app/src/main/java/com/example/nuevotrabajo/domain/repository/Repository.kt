package com.example.nuevotrabajo.domain.repository

import kotlinx.coroutines.flow.Flow
import com.example.nuevotrabajo.domain.model.Tarea

/**
 * CAPA: Domain (Repository )
 *
 * 1. ¿Qué representa?: Define el contrato abstracto para acceder, guardar y modificar las tareas,
 * estableciendo una union entre la capa de Dominio y la capa de Datos.
 *
 * 2. ¿Por qué se utiliza este tipo de estructura?: Se utiliza una `interface` definimos el
 * contrato o las reglas de lo que la aplicación puede hacer con los datos, pero sin meternos
 * a programar cómo se hacen
 *
 * 3. Principio SOLID aplicado: Principio de Inversión de Dependencias (DIP), ya que el Dominio
 *  no depende de la capa de Datos , sino de esta abstracción.
 */
interface Repository {
    fun obtenerTareas(): Flow<List<Tarea>>
    suspend fun guardarTarea(tarea: Tarea)
    suspend fun cambiarEstadoTarea(idTarea: String)
    suspend fun eliminarTarea(idTarea: String)
}
