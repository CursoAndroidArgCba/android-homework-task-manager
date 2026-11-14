package com.example.nuevotrabajo.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nuevotrabajo.domain.model.Tarea

/**
- CAPA: Presentation: se construye un puente entre los datos y lo que vera el usuario
- mediante la viewmodel y el jetpack compose
 *
- ¿Qué representa?: Representa la capa visual y de interacción con el usuario, es lo que el usuario
- ve o toca en la pantalla.
 *
- 2. ¿Por qué se utiliza esta estructura?: La UI solo se encarga de observar el StateFlow del ViewModel
- y emitir eventos cuando el usuario interactúa.
 *
- 3. Principio SOLID aplicado: Principio de Responsabilidad Única (SRP). ya que la única misión
- de esta pantalla es encargarse del diseño visual y de la interfaz de usuario.
 */
@Composable
fun PantallaTareas(
    tareas: List<Tarea>,
    onAgregarTarea: (String, String?) -> Unit,
    onCambiarEstado: (String) -> Unit,
    onEliminarTarea: (String) -> Unit // 1. Nuevo evento expuesto hacia el ViewModel
) {
    var textoTitulo by remember { mutableStateOf("") }
    var textoDescripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(tareas) { tarea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
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
                        
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = tarea.estaCompletada,
                                onCheckedChange = { onCambiarEstado(tarea.id) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onEliminarTarea(tarea.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(text = "Eliminar", color = MaterialTheme.colorScheme.onError)
                            }
                        }
                    }
                }
            }
        }
    }
}