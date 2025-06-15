package com.example.proyectolista.model

import java.io.Serializable

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagen: String
) : Serializable