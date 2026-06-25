package com.aumala.soundsafe.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aumala.soundsafe.data.local.dao.UsuarioDao
import com.aumala.soundsafe.data.local.entity.Usuario

/**
 * Punto de entrada único a la base de datos Room de SoundSafe.
 * Aquí se registran TODAS las entidades de la app (por ahora solo Usuario).
 */
@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        // @Volatile asegura que los cambios a esta variable sean visibles
        // inmediatamente para todos los hilos (importante si varias pantallas
        // intentan acceder a la base de datos al mismo tiempo).
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Si ya existe una instancia, la reutiliza — Room recomienda
            // tener UNA sola instancia de la base de datos en toda la app.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "soundsafe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}