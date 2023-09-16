package com.sergio.evafinal.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Lugar::class], version = 1 )
abstract class DBHelper: RoomDatabase()  {
    abstract fun lugarDao(): LugarDao

    companion object {
        @Volatile
        private var BASE_DATOS: DBHelper? = null
        fun getInstance (contexto: Context): DBHelper {
            return BASE_DATOS ?: synchronized(this) {
                Room. databaseBuilder(
                    contexto.applicationContext,
                    DBHelper::class.java,
                    "lugares.bd")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }

    }
}
