package com.example.myapplication.Screen.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AddContact(onSave: (Contact) -> Unit, onNavigateToList: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var hobbie by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Gradient background
    val purpleGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF2C4971), Color(0xFF91A9BE)),
        start = Offset(0f, 0f),
        end = Offset.Infinite
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

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") }
            )
            TextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Alias") }
            )
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
            TextField(
                value = hobbie,
                onValueChange = { hobbie = it },
                label = { Text("Hobbie") }
            )

            // Botón para guardar el contacto con un estilo moderno
            Button(
                onClick = {
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00BCD4), Color(0xFF8E24AA)),
                            startX = 0f,
                            endX = 1000f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text(
                    text = "Registrar Contacto",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Botón para navegar a la lista de contactos registrados con el mismo estilo
            Button(
                onClick = onNavigateToList,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00BCD4), Color(0xFF8E24AA)),
                            startX = 0f,
                            endX = 1000f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text(
                    text = "Ver Contactos Registrados",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Snackbar Host para mostrar el mensaje de confirmación
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

data class Contact(
    val nombre: String,
    val apellido: String,
    val alias: String,
    val telefono: String,
    val hobbie: String
)

@Composable
fun AddContactPreview() {
    AddContact(onSave = {}, onNavigateToList = {})
}
