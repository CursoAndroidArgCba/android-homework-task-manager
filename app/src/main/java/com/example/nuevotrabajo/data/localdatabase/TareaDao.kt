package com.example.nuevotrabajo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * CAPA: Data (DAO - Data Access Object)
 * Interfaz que contiene los métodos para acceder a los datos almacenados en SQLite.
 */
@Dao
interface TareaDao {

    @Query("SELECT * FROM tareas")
    fun obtenerTodasLasTareas(): Flow<List<TareaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: TareaEntity)

    @Query("UPDATE tareas SET estaCompletada = NOT estaCompletada WHERE id = :id")
    suspend fun cambiarEstadoTarea(id: String)
}