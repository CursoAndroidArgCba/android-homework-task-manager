package com.example.nuevotrabajo.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nuevotrabajo.domain.Tarea

@Composable
fun PantallaTareas(
    tareas: List<Tarea>,
    onAgregarTarea: (String, String?) -> Unit,
    onCambiarEstado: (String) -> Unit,
    onEliminarTarea: (String) -> Unit,
    onEditarTarea: (String, String, String?) -> Unit // 1. Nuevo evento: (id, nuevoTitulo, nuevaDescripcion)
) {
    var textoTitulo by remember { mutableStateOf("") }
    var textoDescripcion by remember { mutableStateOf("") }

    // Estados para controlar qué tarea se está editando actualmente
    var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }
    var tituloEditado by remember { mutableStateOf("") }
    var descripcionEditada by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = textoTitulo,
            onValueChange = { textoTitulo = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = textoDescripcion,
            onValueChange = { textoDescripcion = it },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (textoTitulo.isNotBlank()) {
                    onAgregarTarea(textoTitulo, textoDescripcion)
                    textoTitulo = ""
                    textoDescripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Tarea")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tareas) { tarea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
                            tarea.descripcion?.let {
                                Text(text = it, style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (tarea.estaCompletada) "Estado: Completada" else "Estado: Pendiente",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tarea.estaCompletada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = tarea.estaCompletada,
                                onCheckedChange = { onCambiarEstado(tarea.id) }
                            )

                            // 2. Nuevo botón "Editar" agregado sin quitar nada anterior
                            TextButton(
                                onClick = {
                                    tareaAEditar = tarea
                                    tituloEditado = tarea.titulo
                                    descripcionEditada = tarea.descripcion ?: ""
                                }
                            ) {
                                Text(text = "Editar", color = MaterialTheme.colorScheme.primary)
                            }

                            TextButton(onClick = { onEliminarTarea(tarea.id) }) {
                                Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    // 3. Diálogo emergente para modificar la tarea seleccionada
    tareaAEditar?.let { tarea ->
        AlertDialog(
            onDismissRequest = { tareaAEditar = null },
            title = { Text("Modificar Tarea") },
            text = {
                Column {
                    OutlinedTextField(
                        value = tituloEditado,
                        onValueChange = { tituloEditado = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descripcionEditada,
                        onValueChange = { descripcionEditada = it },
                        label = { Text("Descripción (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tituloEditado.isNotBlank()) {
                            onEditarTarea(
                                tarea.id,
                                tituloEditado,
                                descripcionEditada.ifBlank { null }
                            )
                            tareaAEditar = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { tareaAEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}