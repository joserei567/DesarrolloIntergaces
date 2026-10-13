package com.example.desarrollointergaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.desarrollointergaces.ui.theme.DesarrolloIntergacesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesarrolloIntergacesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { margen ->
                    Calculadora(Modifier.padding(margen))
                }
            }
        }
    }
}

// Devuelve null cuando la operación no se puede realizar.
fun calcular(primero: Double, segundo: Double, operacion: String): Double? {
    val resultado = when (operacion) {
        "+" -> primero + segundo
        "-" -> primero - segundo
        "×" -> primero * segundo
        "÷" -> if (segundo == 0.0) return null else primero / segundo
        else -> segundo
    }
    return if (resultado.isFinite()) resultado else null
}

fun mostrarNumero(numero: Double): String {
    return java.math.BigDecimal.valueOf(numero).stripTrailingZeros().toPlainString()
}

@Composable
fun Calculadora(modifier: Modifier = Modifier) {
    // Estos valores se conservan al girar la pantalla.
    var entrada by rememberSaveable { mutableStateOf("0") }
    var primerNumero by rememberSaveable { mutableStateOf(0.0) }
    var operacion by rememberSaveable { mutableStateOf("") }
    var nuevaEntrada by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf("") }

    // Mientras se escribe el segundo número, se muestra el resultado provisional.
    val resultado = if (operacion.isNotEmpty() && !nuevaEntrada) {
        calcular(primerNumero, entrada.toDouble(), operacion)
    } else {
        entrada.toDouble()
    }
    val textoResultado = when {
        error.isNotEmpty() -> error
        resultado == null -> if (operacion == "÷" && entrada.toDouble() == 0.0) {
            "No se puede dividir entre cero"
        } else {
            "Resultado demasiado grande"
        }
        else -> mostrarNumero(resultado)
    }

    val filas = listOf(
        listOf("C", "Borrar", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Calculadora", fontSize = 28.sp)
        Spacer(Modifier.weight(1f))
        Text(
            text = if (operacion.isEmpty()) "" else "${mostrarNumero(primerNumero)} $operacion",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 22.sp
        )
        Text(entrada, Modifier.fillMaxWidth(), textAlign = TextAlign.End, fontSize = 36.sp)
        Text(
            text = "Resultado: $textoResultado",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 20.sp,
            color = if (resultado == null || error.isNotEmpty()) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        Spacer(Modifier.height(12.dp))

        for (fila in filas) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (boton in fila) {
                    Button(
                        modifier = Modifier.weight(1f).height(58.dp),
                        onClick = {
                            when (boton) {
                                "C" -> {
                                    entrada = "0"
                                    primerNumero = 0.0
                                    operacion = ""
                                    nuevaEntrada = false
                                    error = ""
                                }
                                "Borrar" -> {
                                    if (!nuevaEntrada) {
                                        entrada = entrada.dropLast(1)
                                        if (entrada.isEmpty() || entrada == "-") entrada = "0"
                                        error = ""
                                    }
                                }
                                "+", "-", "×", "÷" -> {
                                    if (resultado != null && error.isEmpty()) {
                                        primerNumero = resultado
                                        entrada = mostrarNumero(resultado)
                                        operacion = boton
                                        nuevaEntrada = true
                                    }
                                }
                                "=" -> {
                                    if (operacion.isNotEmpty() && !nuevaEntrada) {
                                        if (resultado == null) {
                                            error = textoResultado
                                        } else {
                                            entrada = mostrarNumero(resultado)
                                            operacion = ""
                                            nuevaEntrada = true
                                        }
                                    }
                                }
                                else -> {
                                    // Después de igual o de una operación comienza otro número.
                                    if (nuevaEntrada || error.isNotEmpty()) {
                                        entrada = "0"
                                        nuevaEntrada = false
                                        if (error.isNotEmpty()) operacion = ""
                                        error = ""
                                    }
                                    if (boton == ".") {
                                        if (!entrada.contains(".")) entrada += "."
                                    } else if (entrada.length < 15) {
                                        entrada = if (entrada == "0") boton else entrada + boton
                                    }
                                }
                            }
                        }
                    ) {
                        Text(boton, fontSize = if (boton == "Borrar") 16.sp else 24.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    DesarrolloIntergacesTheme {
        Calculadora()
    }
}
