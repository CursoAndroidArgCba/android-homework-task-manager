package com.example.nuevotrabajo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nuevotrabajo.domain.Tarea

@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val descripcion: String?,
    val estaCompletada: Boolean
)

// Funciones de extensión para mapear entre la Entidad de Room y la Entidad del Dominio
fun TareaEntity.toDomain(): Tarea {
    return Tarea(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        estaCompletada = estaCompletada
    )
}

fun Tarea.toEntity(): TareaEntity {
    return TareaEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        estaCompletada = estaCompletada
    )
}