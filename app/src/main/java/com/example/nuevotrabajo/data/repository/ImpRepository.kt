package com.example.nuevotrabajo.data.repository

import com.example.nuevotrabajo.data.local.dao.TareaDao
import com.example.nuevotrabajo.data.local.entity.toDomain
import com.example.nuevotrabajo.data.local.entity.toEntity
import com.example.nuevotrabajo.domain.repository.Repository
import com.example.nuevotrabajo.domain.model.Tarea
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * CAPA: ImpRepository :puente que toma reglas del modelo de dominio y las hace realidad tomando los datos
 * de la base de datos
 *
 * ¿Por qué se utiliza esta estructura?: Recibe la fuente de datos, separa el origen de los datosde
 * de la implementacion del repositoriopara que cada clase tenga una tarea determinada.
 *
 * 3. Principio SOLID aplicado: Principio de Inversión de Dependencias (DIP), porque la capa de
 * datos se adapta al contrato del Dominio y nos permite cambiar la base de datos
 * sin romper nada de la lógica central
 */
class ImpRepository(
    private val tareaDao: TareaDao
) : Repository {

    override fun obtenerTareas(): Flow<List<Tarea>> {
        return tareaDao.obtenerTodasLasTareas().map { lista ->
            lista.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun guardarTarea(tarea: Tarea) {
        tareaDao.insertarTarea(tarea.toEntity())
    }

    override suspend fun cambiarEstadoTarea(id: String) {
        tareaDao.cambiarEstadoTarea(id)
    }

    override suspend fun eliminarTarea(idTarea: String) {
        tareaDao.eliminarTareaPorId(idTarea)
    }
}