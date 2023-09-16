package com.sergio.evafinal.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LugarDao {
    @Query("SELECT * FROM lugar ORDER BY orden")
    fun findAll():List<Lugar>

    @Query("SELECT COUNT (*) FROM lugar")
    fun contar():Int

    @Insert
    fun insertLugar(lugar:Lugar):Long

    @Update
    fun updateLugar(lugar:Lugar)

    @Delete
    fun deleteLugar(lugar:Lugar)
}