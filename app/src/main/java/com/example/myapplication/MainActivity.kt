package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.Screen.nav.AddContact
import com.example.myapplication.Screen.nav.NavigationExample
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Actividad principal de la aplicación.
 *
 * Esta actividad establece el contenido de la interfaz de usuario y habilita el diseño edge-to-edge.
 */
class MainActivity : ComponentActivity() {
    /**
     * Método llamado al crear la actividad.
     *
     * Aquí se habilita el diseño edge-to-edge y se establece el contenido de la interfaz de usuario.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el diseño edge-to-edge
        setContent {
            MyApplicationTheme {
                NavigationExample() // Reemplazamos el contenido por la navegación
            }
        }
    }
}

/**
 * Composable que representa la pantalla principal de la aplicación.
 *
 * Este componente muestra el contenido de navegación en una superficie con el tema de la aplicación.
 */
@Composable
@Preview(showBackground = true)
fun MainScreen() {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavigationExample() // Mostramos el componente de navegación en la pantalla principal
        }
    }
}
