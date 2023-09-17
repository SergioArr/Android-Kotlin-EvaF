package com.sergio.evafinal.API

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Indicadores(
    val version: String?,
    val autor: String?,
    val fecha: String?,
    val uf: Indicador?,
    val ivp: Indicador?,
    val dolar: Indicador?,
    val dolar_intercambio: Indicador?,
    val euro: Indicador?,
    val ipc: Indicador?,
    val utm: Indicador?,
    val imacec: Indicador?,
    val tpm: Indicador?,
    val libra_cobre: Indicador?,
    val tasa_desempleo: Indicador?,
    val bitcoin: Indicador?
)

@JsonClass(generateAdapter = true)
data class Indicador(
    val codigo: String?,
    val nombre: String?,
    val unidad_medida: String?,
    val fecha: String?,
    val valor: Double?
)