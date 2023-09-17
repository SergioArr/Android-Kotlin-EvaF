package com.sergio.evafinal.API

import retrofit2.http.GET

interface DivisaService {
        @GET("/api/")
        suspend fun obtenerIndicadores(): Indicadores


    }