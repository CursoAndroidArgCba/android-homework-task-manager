package com.example.nuevotrabajo.domain.model

/**
 * CAPA: Domain : Es el nucleo ya que representa las reglas puras de la aplicacion,
 * las otras capas dependen de el y de esa forma es independiente de los demas.
 *
 * 1. ¿Qué representa la clase?: Es el modelo central.
 *
 * 2. ¿Por qué se utiliza este tipo de estructura?: Se utiliza un `data class` porque es la estructura
 * ideal para manejar datos puros. Evita escribir codigos repetitivos
 *
 * 3. Principio SOLID aplicado: Principio de Responsabilidad Única (SRP), ya que su única razón
 * de ser es modelar los datos del dominio sin mezclar lógica de negocio ni dependencias de elementos externos.
 */
data class Tarea(
    val id: String,
    val titulo: String,
    val descripcion: String?,
    val estaCompletada: Boolean
)