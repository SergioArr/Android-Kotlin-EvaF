package com.sergio.evafinal.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lugar(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var lugarNombre: String,
    var imagenUrl: String,
    var latitud: Double,
    var longitud: Double,
    var orden: Int,
    var costoAlojamiento: Double?,
    var costoTraslados: Double?,
    var comentarios: String?
)
