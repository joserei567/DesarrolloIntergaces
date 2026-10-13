package com.example.desarrollointergaces

import org.junit.Assert.*
import org.junit.Test

class CalculadoraTest {
    @Test
    fun operacionesBasicas() {
        assertEquals(12.0, calcular(7.0, 5.0, "+")!!, 0.001)
        assertEquals(-2.0, calcular(3.0, 5.0, "-")!!, 0.001)
        assertEquals(15.0, calcular(3.0, 5.0, "×")!!, 0.001)
        assertEquals(2.5, calcular(5.0, 2.0, "÷")!!, 0.001)
    }

    @Test
    fun divisionEntreCero() {
        assertNull(calcular(5.0, 0.0, "÷"))
    }

    @Test
    fun formatoDelResultado() {
        assertEquals("12", mostrarNumero(12.0))
        assertEquals("2.5", mostrarNumero(2.5))
        assertEquals("-2", mostrarNumero(-2.0))
    }
}
