package com.example.base_datos.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.base_datos.Model.User
import com.example.base_datos.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserApp(userRepository: UserRepository) {
    // Variables para almacenar los datos de entrada del usuario
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }

    // Inicializa el scope de corutinas para manejar operaciones asíncronas
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFF0F4FF)) // Establece un fondo suave gris claro
            .padding(top = 32.dp) // Espacio adicional en la parte superior
    ) {
        // Título de la aplicación
        Text(
            text = "Gestión de Usuarios",
            style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF3F51B5)), // Título en color azul oscuro
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de entrada para nombre, apellido y edad
        InputField(value = nombre, label = "Nombre") { nombre = it }
        InputField(value = apellido, label = "Apellido") { apellido = it }
        InputField(value = edad, label = "Edad (0-105)", keyboardType = KeyboardType.Number) { edad = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para registrar un nuevo usuario
        CustomButton(text = "Registrar") {
            val edadInt = edad.toIntOrNull() // Intenta convertir la edad a un entero
            if (nombre.isNotBlank() && apellido.isNotBlank() && edadInt != null && edadInt in 0..105) {
                // Verifica que los campos no estén vacíos y que la edad sea válida
                val user = User(
                    nombre = nombre,
                    apellido = apellido,
                    edad = edadInt
                )
                scope.launch {
                    withContext(Dispatchers.IO) {
                        userRepository.insert(user) // Inserta el usuario en la base de datos
                    }
                    Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show() // Muestra mensaje de éxito
                    // Limpia los campos de entrada después de registrar
                    nombre = ""
                    apellido = ""
                    edad = ""
                }
            } else {
                // Si hay campos vacíos o la edad es inválida, muestra un mensaje de error
                Toast.makeText(context, "Por favor, complete todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el ID del usuario a eliminar o actualizar
        InputField(value = idInput, label = "ID (Eliminar o Actualizar)", keyboardType = KeyboardType.Number) { idInput = it }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para eliminar usuario
        CustomButton(text = "Eliminar") {
            val id = idInput.toIntOrNull() // Convierte el ID a un entero
            if (id != null) {
                scope.launch {
                    val deletedCount: Int
                    withContext(Dispatchers.IO) {
                        deletedCount = userRepository.deleteById(id) // Elimina el usuario por ID
                    }

                    if (deletedCount > 0) {
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                        // Actualiza la lista de usuarios después de eliminar
                        users = withContext(Dispatchers.IO) {
                            userRepository.getAllUser() // Obtiene todos los usuarios de la base de datos
                        }
                    } else {
                        Toast.makeText(context, "Usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Si el ID es inválido, muestra un mensaje de error
                Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para actualizar usuario
        CustomButton(text = "Actualizar") {
            val id = idInput.toIntOrNull() // Convierte el ID a un entero
            val edadInt = edad.toIntOrNull() // Intenta convertir la edad a un entero
            if (id != null && edadInt != null && edadInt in 0..105) {
                // Verifica que el ID y la edad sean válidos
                val updatedUser = User(
                    id = id,
                    nombre = nombre,
                    apellido = apellido,
                    edad = edadInt
                )
                scope.launch {
                    withContext(Dispatchers.IO) {
                        userRepository.update(updatedUser) // Actualiza el usuario en la base de datos
                    }
                    Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()

                    // Actualiza la lista después de la actualización
                    users = withContext(Dispatchers.IO) {
                        userRepository.getAllUser() // Obtiene todos los usuarios actualizados de la base de datos
                    }
                }
            } else {
                // Si el ID o la edad son inválidos, muestra un mensaje de error
                Toast.makeText(context, "ID o edad inválidos. La edad debe ser un número entre 0 y 105.", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para listar todos los usuarios
        CustomButton(text = "Listar Usuarios") {
            scope.launch {
                users = withContext(Dispatchers.IO) {
                    userRepository.getAllUser() // Obtiene todos los usuarios de la base de datos
                }
                Toast.makeText(context, "Usuarios listados", Toast.LENGTH_SHORT).show() // Muestra mensaje de éxito
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra la lista de usuarios usando LazyColumn para permitir el desplazamiento
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(users) { user ->
                UserCard(user = user) // Muestra cada usuario en una tarjeta
                Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre tarjetas
            }
        }
    }
}

// Composable para el campo de entrada
@Composable
fun InputField(value: String, label: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color(0xFF3F51B5)) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType), // Establece el tipo de teclado
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // Fondo blanco para el TextField
            .padding(8.dp) // Espaciado interno
    )
}

// Composable para botones personalizados
@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

// Composable para mostrar cada usuario en una tarjeta
@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFE3F2FD))
        ) {
            Text(text = "ID: ${user.id}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Text(text = "Nombre: ${user.nombre}", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(text = "Apellido: ${user.apellido}", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(text = "Edad: ${user.edad}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        }
    }
}