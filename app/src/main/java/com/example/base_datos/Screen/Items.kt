package com.example.base_datos.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var idDelete by remember { mutableStateOf("") }
    var idUpdate by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Campos de entrada para nombre, apellido y edad
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text(text = "Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text(text = "Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para registrar un usuario
        Button(onClick = {
            val user = User(
                nombre = nombre,
                apellido = apellido,
                edad = edad.toIntOrNull() ?: 0
            )
            scope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.insert(user)
                }
                Toast.makeText(context, "Usuario registrado", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para listar usuarios
        Button(onClick = {
            scope.launch {
                users = withContext(Dispatchers.IO) {
                    userRepository.getAllUser()
                }
            }
        }) {
            Text(text = "Listar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar lista de usuarios
        Column {
            users.forEach { user ->
                Text("${user.id} ${user.nombre} ${user.apellido}, Edad: ${user.edad}")
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el ID del usuario a eliminar
        TextField(
            value = idDelete,
            onValueChange = { idDelete = it },
            label = { Text(text = "ID a eliminar") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para eliminar usuario
        Button(onClick = {
            val id = idDelete.toIntOrNull()
            if (id != null) {
                scope.launch {
                    val deletedCount: Int
                    withContext(Dispatchers.IO) {
                        deletedCount = userRepository.deleteById(id)
                    }

                    if (deletedCount > 0) {
                        Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                        // Actualizar la lista después de eliminar
                        users = withContext(Dispatchers.IO) {
                            userRepository.getAllUser()
                        }
                    } else {
                        Toast.makeText(context, "Usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Eliminar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el ID del usuario a actualizar
        TextField(
            value = idUpdate,
            onValueChange = { idUpdate = it },
            label = { Text(text = "ID a actualizar") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para actualizar usuario
        Button(onClick = {
            val id = idUpdate.toIntOrNull()
            if (id != null) {
                val updatedUser = User(
                    id = id,
                    nombre = nombre,
                    apellido = apellido,
                    edad = edad.toIntOrNull() ?: 0
                )
                scope.launch {
                    withContext(Dispatchers.IO) {
                        userRepository.update(updatedUser)
                    }
                    Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()

                    // Actualizar la lista después de actualizar
                    users = withContext(Dispatchers.IO) {
                        userRepository.getAllUser()
                    }
                }
            } else {
                Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Actualizar")
        }
    }
}
