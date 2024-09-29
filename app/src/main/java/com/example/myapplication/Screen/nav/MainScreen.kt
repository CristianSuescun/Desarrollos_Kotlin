package com.example.myapplication.Screen.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Composable que permite agregar un nuevo contacto.
 *
 * @param onSave Función que se llama para guardar el contacto.
 * @param onNavigateToList Función que se llama para navegar a la lista de contactos.
 */
@Composable
fun AddContact(onSave: (Contact) -> Unit, onNavigateToList: () -> Unit) {
    // Estado para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hobbie by remember { mutableStateOf("") }

    // Estado para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Fondo con gradiente
    val purpleGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF2C4971), Color(0xFF91A9BE)),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset.Infinite
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Añadir Contacto",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo para el nombre
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            // Campo para el apellido
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") }
            )
            // Campo para el alias
            TextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Alias") }
            )
            // Campo para el teléfono (solo números)
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number  // Mostrar teclado numérico
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            // Campo para el hobbie
            TextField(
                value = hobbie,
                onValueChange = { hobbie = it },
                label = { Text("Hobbie") }
            )

            // Botón para guardar el contacto
            Button(onClick = {
                if (nombre.isNotBlank() && telefono.isNotBlank()) {
                    val newContact = Contact(nombre, apellido, alias, telefono, hobbie)
                    onSave(newContact)

                    // Limpiar los campos del formulario después de guardar
                    nombre = ""
                    apellido = ""
                    alias = ""
                    telefono = ""
                    hobbie = ""

                    // Mostrar mensaje de confirmación usando Snackbar
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Contacto registrado con éxito")
                    }
                } else {
                    // Mostrar un mensaje de error si los campos requeridos están vacíos
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("El nombre y teléfono son obligatorios")
                    }
                }
            }) {
                Text("Registrar Contacto")
            }

            // Botón para navegar a la lista de contactos registrados
            Button(onClick = onNavigateToList) {
                Text("Ver Contactos Registrados")
            }

            // Snackbar Host para mostrar el mensaje de confirmación
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

/**
 * Clase de datos que representa un contacto.
 *
 * @param nombre Nombre del contacto.
 * @param apellido Apellido del contacto.
 * @param alias Alias del contacto.
 * @param telefono Número de teléfono del contacto.
 * @param hobbie Hobbie del contacto.
 */
data class Contact(
    val nombre: String,
    val apellido: String,
    val alias: String,
    val telefono: String,
    val hobbie: String
)

/**
 * Composable que sirve como vista previa para el formulario de agregar contacto.
 */
@Composable
fun AddContactPreview() {
    AddContact(onSave = {}, onNavigateToList = {})
}
