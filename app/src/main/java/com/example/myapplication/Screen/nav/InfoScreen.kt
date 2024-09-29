package com.example.myapplication.Screen.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.* // Para remember, mutableStateOf, etc.
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch

/**
 * Pantalla que muestra una lista de contactos.
 *
 * @param contactList Lista de contactos a mostrar.
 * @param onBackToForm Función que se ejecuta al presionar el botón para volver al formulario.
 * @param onEditContact Función que se ejecuta al editar un contacto.
 * @param onDeleteContact Función que se ejecuta al eliminar un contacto.
 */
@Composable
fun InfoScreen(contactList: List<Contact>, onBackToForm: () -> Unit, onEditContact: (Contact) -> Unit, onDeleteContact: (Contact) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el tamaño disponible
            .padding(top = 48.dp), // Agrega un margen superior
        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
        verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado vertical entre elementos
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho
                .weight(1f), // Permite que la columna ocupe el espacio restante
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado vertical entre los elementos de la lista
        ) {
            // Itera sobre la lista de contactos y crea una tarjeta para cada uno
            items(contactList.size) { index ->
                val contact = contactList[index]
                ContactCard(contact, onEditContact, onDeleteContact)
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaciador para separar el botón

        // Botón para volver al formulario
        Button(onClick = onBackToForm) {
            Text("Volver al Formulario") // Texto del botón
        }
    }
}

/**
 * Tarjeta que muestra la información de un contacto y opciones para editar o eliminar.
 *
 * @param contact Contacto a mostrar.
 * @param onEditContact Función que se ejecuta al editar el contacto.
 * @param onDeleteContact Función que se ejecuta al eliminar el contacto.
 */
@Composable
fun ContactCard(contact: Contact, onEditContact: (Contact) -> Unit, onDeleteContact: (Contact) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho
            .padding(8.dp) // Margen alrededor de la tarjeta
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${contact.nombre}") // Muestra el nombre del contacto
            Text(text = "Apellido: ${contact.apellido}") // Muestra el apellido del contacto
            Text(text = "Alias: ${contact.alias}") // Muestra el alias del contacto
            Text(text = "Teléfono: ${contact.telefono}") // Muestra el teléfono del contacto
            Text(text = "Hobbie: ${contact.hobbie}") // Muestra el hobbie del contacto

            // Botón para editar el contacto
            Button(onClick = { onEditContact(contact) }) {
                Text("Editar") // Texto del botón de editar
            }

            // Botón para eliminar el contacto
            Button(onClick = { onDeleteContact(contact) }) {
                Text("Eliminar") // Texto del botón de eliminar
            }
        }
    }
}

/**
 * Pantalla para editar la información de un contacto.
 *
 * @param contact Contacto a editar.
 * @param onSave Función que se ejecuta al guardar los cambios.
 * @param onNavigateBack Función que se ejecuta al navegar de regreso.
 */
@Composable
fun EditContact(contact: Contact, onSave: (Contact) -> Unit, onNavigateBack: () -> Unit) {
    // Estado mutable para cada campo del contacto
    var nombre by remember { mutableStateOf(contact.nombre) }
    var apellido by remember { mutableStateOf(contact.apellido) }
    var alias by remember { mutableStateOf(contact.alias) }
    var telefono by remember { mutableStateOf(contact.telefono) }
    var hobbie by remember { mutableStateOf(contact.hobbie) }

    // Estado para mostrar mensajes de Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Diseño de la interfaz de usuario
    Box(
        modifier = Modifier.fillMaxSize(), // Ocupa comleto el tamaño disponible
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Margen alrededor de la columna
                .fillMaxWidth(0.85f) // Ancho limitado al 85%
                .background(Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con esquinas redondeadas
                .padding(16.dp), // Margen interno
            horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado vertical entre elementos
        ) {
            Text(text = "Editar Contacto", style = MaterialTheme.typography.displaySmall) // Título

            // Campos de texto para editar los atributos del contacto
            TextField(
                value = nombre,
                onValueChange = { newValue -> nombre = newValue }, // Actualiza el nombre
                label = { Text("Nombre") } // Etiqueta del campo
            )
            TextField(
                value = apellido,
                onValueChange = { newValue -> apellido = newValue }, // Actualiza el apellido
                label = { Text("Apellido") } // Etiqueta del campo
            )
            TextField(
                value = alias,
                onValueChange = { newValue -> alias = newValue }, // Actualiza el alias
                label = { Text("Alias") } // Etiqueta del campo
            )
            TextField(
                value = telefono,
                onValueChange = { newValue -> telefono = newValue }, // Actualiza el teléfono
                label = { Text("Teléfono") }, // Etiqueta del campo
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Tipo de teclado numérico
            )
            TextField(
                value = hobbie,
                onValueChange = { newValue -> hobbie = newValue }, // Actualiza el hobbie
                label = { Text("Hobbie") } // Etiqueta del campo
            )

            // Botón para guardar los cambios
            Button(onClick = {
                // Crea un nuevo contacto con los valores actualizados
                val updatedContact = contact.copy(
                    nombre = nombre,
                    apellido = apellido,
                    alias = alias,
                    telefono = telefono,
                    hobbie = hobbie
                )
                onSave(updatedContact) // Guarda los cambios

                // Muestra un mensaje de confirmación
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Contacto guardado con éxito.")
                }

                onNavigateBack()  // Navegar de regreso después de guardar
            }) {
                Text("Guardar Cambios") // Texto del botón de guardar
            }

            // Snackbar para mostrar mensajes temporales
            SnackbarHost(hostState = snackbarHostState) // Muestra el snackbar para mensajes
        }
    }
}
