package com.example.myapplication.Screen.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen.nav.AddContact
import com.example.myapplication.Screen.nav.InfoScreen

/**
 * NavigationExample es una función composable que configura la navegación
 * entre diferentes pantallas en la aplicación usando Jetpack Compose.
 * Esta función inicializa el controlador de navegación y define las rutas
 * de navegación y la lógica asociada a cada pantalla.
 */
@Composable
fun NavigationExample() {
    // Inicializa el controlador de navegación
    val navController = rememberNavController()

    // Crea y recuerda una lista mutable de contactos
    val contactList = remember { mutableStateListOf<Contact>() }

    // Define el host de navegación y el destino inicial
    NavHost(navController = navController, startDestination = "add_contact") {
        // Composable para añadir un nuevo contacto
        composable("add_contact") {
            AddContact(
                // Función que se ejecuta al guardar un nuevo contacto
                onSave = { newContact ->
                    contactList.add(newContact) // Añade el nuevo contacto a la lista
                },
                // Función para navegar a la pantalla de información
                onNavigateToList = {
                    navController.navigate("info_screen")
                }
            )
        }

        // Composable para mostrar la lista de contactos
        composable("info_screen") {
            InfoScreen(
                contactList = contactList, // Pasa la lista de contactos a la pantalla
                // Función para regresar al formulario de añadir contacto
                onBackToForm = {
                    navController.popBackStack() // Regresa a la pantalla anterior
                },
                // Función para editar un contacto
                onEditContact = { contact ->
                    navController.navigate("edit_contact/${contact.nombre}/${contact.apellido}/${contact.alias}/${contact.telefono}/${contact.hobbie}")
                },
                // Función para eliminar un contacto
                onDeleteContact = { contact ->
                    contactList.remove(contact) // Elimina el contacto de la lista
                }
            )
        }

        // Composable para editar un contacto existente
        composable("edit_contact/{nombre}/{apellido}/{alias}/{telefono}/{hobbie}") { backStackEntry ->
            // Obtiene los parámetros de la ruta
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val apellido = backStackEntry.arguments?.getString("apellido") ?: ""
            val alias = backStackEntry.arguments?.getString("alias") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            val hobbie = backStackEntry.arguments?.getString("hobbie") ?: ""

            // Llama a la función EditContact con los datos del contacto
            EditContact(
                contact = Contact(nombre, apellido, alias, telefono, hobbie),
                // Función que se ejecuta al guardar los cambios en el contacto
                onSave = { updatedContact ->
                    val index = contactList.indexOfFirst { it.telefono == updatedContact.telefono }
                    if (index != -1) {
                        contactList[index] = updatedContact // Actualiza el contacto en la lista
                    }
                },
                // Función para regresar a la pantalla anterior
                onNavigateBack = {
                    navController.popBackStack() // Regresa a la pantalla anterior
                }
            )
        }
    }
}
