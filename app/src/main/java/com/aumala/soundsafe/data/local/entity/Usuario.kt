package com.aumala.soundsafe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa la tabla "usuario" en la base de datos local.
 * Cada propiedad se convierte en una columna; la clase entera se convierte en la tabla.
 */
@Entity(tableName = "usuario")
data class Usuario(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val correo: String,

    // Nunca se guarda la contraseña en texto plano — este campo almacena el HASH,
    // no la contraseña que el usuario escribió.
    val contrasenaHash: String
)