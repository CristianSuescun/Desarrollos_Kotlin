package com.example.myapplication.Screen.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
    // Establecer el fondo de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6CD1FF)) // Fondo azul claro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Lista de Contactos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contactList) { contact ->
                    ContactCard(contact, onEditContact, onDeleteContact)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GradientButton(
                text = "Volver al Formulario",
                onClick = onBackToForm,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}

@Composable
fun GradientButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF64B5F6), Color(0xFF42A5F5)),
                        start = Offset(0f, 0f),
                        end = Offset(0f, 100f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Text(text = text, color = Color.White)
        }
    }
}

@Composable
fun ContactCard(contact: Contact, onEditContact: (Contact) -> Unit, onDeleteContact: (Contact) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Nombre: ${contact.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Apellido: ${contact.apellido}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Alias: ${contact.alias}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Teléfono: ${contact.telefono}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Hobbie: ${contact.hobbie}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            GradientButton(
                text = "Editar",
                onClick = { onEditContact(contact) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            GradientButton(
                text = "Eliminar",
                onClick = { onDeleteContact(contact) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditContact(contact: Contact, onSave: (Contact) -> Unit, onNavigateBack: () -> Unit) {
    var nombre by remember { mutableStateOf(contact.nombre) }
    var apellido by remember { mutableStateOf(contact.apellido) }
    var alias by remember { mutableStateOf(contact.alias) }
    var telefono by remember { mutableStateOf(contact.telefono) }
    var hobbie by remember { mutableStateOf(contact.hobbie) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF48A6C2)),
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
            Text(text = "Editar Contacto", style = MaterialTheme.typography.titleLarge)

            TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            TextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
            TextField(value = alias, onValueChange = { alias = it }, label = { Text("Alias") })
            TextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            TextField(value = hobbie, onValueChange = { hobbie = it }, label = { Text("Hobbie") })

            GradientButton(
                text = "Guardar Cambios",
                onClick = {
                    val updatedContact = contact.copy(nombre = nombre, apellido = apellido, alias = alias, telefono = telefono, hobbie = hobbie)
                    onSave(updatedContact)
                    coroutineScope.launch { snackbarHostState.showSnackbar("Contacto guardado con éxito.") }
                    onNavigateBack()
                }
            )

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}