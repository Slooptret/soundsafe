package com.aumala.soundsafe.util

import java.security.MessageDigest

/**
 * Utilidad simple de hashing para contraseñas.
 *
 * NOTA ACADÉMICA: SHA-256 es un hash criptográfico válido para fines de aprendizaje,
 * pero en un proyecto de producción real se recomienda BCrypt o Argon2, que además
 * de hashear, agregan "salt" automático y son deliberadamente lentos para dificultar
 * ataques de fuerza bruta. Para el MVP de SoundSafe, SHA-256 es suficiente y más simple
 * de implementar sin dependencias externas.
 */
object PasswordHasher {

    fun hash(contrasenaPlano: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(contrasenaPlano.toByteArray(Charsets.UTF_8))
        // Convierte los bytes del hash a una cadena hexadecimal legible
        return bytes.joinToString("") { "%02x".format(it) }
    }
}