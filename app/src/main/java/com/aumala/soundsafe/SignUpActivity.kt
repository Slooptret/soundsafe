package com.aumala.soundsafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aumala.soundsafe.data.local.AppDatabase
import com.aumala.soundsafe.data.local.entity.Usuario
import com.aumala.soundsafe.util.PasswordHasher
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var layoutName: TextInputLayout
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutPassword: TextInputLayout
    private lateinit var layoutConfirmPassword: TextInputLayout

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Referencias a los TextInputLayout (contenedores con el error visual)
        layoutName = findViewById(R.id.layoutName)
        layoutEmail = findViewById(R.id.layoutEmail)
        layoutPassword = findViewById(R.id.layoutPassword)
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword)

        // Referencias a los campos de texto reales
        nameEditText = findViewById(R.id.editName)
        emailEditText = findViewById(R.id.editEmail)
        passwordEditText = findViewById(R.id.editPassword)
        confirmPasswordEditText = findViewById(R.id.editConfirmPassword)

        val buttonSignUp = findViewById<MaterialButton>(R.id.buttonSignUp)
        val textGoToLogin = findViewById<android.widget.TextView>(R.id.textGoToLogin)

        // Conexión del botón con la validación + registro real en Room
        buttonSignUp.setOnClickListener {
            if (validateInputs()) {
                attemptRegister()
            }
        }

        // Navegación de regreso a LoginActivity
        textGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // cierra SignUpActivity para que no quede apilada en el historial
        }
    }

    /**
     * Registra al usuario en Room, solo si su correo no existe todavía.
     * Se ejecuta en una coroutine porque consultar e insertar en Room
     * son operaciones de base de datos y nunca deben correr en el hilo principal.
     */
    private fun attemptRegister() {
        val nombre = nameEditText.text.toString().trim()
        val correo = emailEditText.text.toString().trim()
        val contrasenaPlano = passwordEditText.text.toString()

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(applicationContext).usuarioDao()

            // 1. Verificar que el correo no esté registrado ya
            val usuarioExistente = dao.buscarPorCorreo(correo)
            if (usuarioExistente != null) {
                layoutEmail.error = "Este correo ya está registrado"
                return@launch
            }

            // 2. Transformar la contraseña en hash — nunca se guarda en texto plano
            val hash = PasswordHasher.hash(contrasenaPlano)

            // 3. Insertar el nuevo usuario en la tabla
            dao.insertarUsuario(
                Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasenaHash = hash
                )
            )

            // 4. Registro exitoso — navega a LoginActivity para que inicie sesión
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    /**
     * Valida los 4 campos del formulario de registro.
     * Orden: vacío -> formato -> longitud -> coincidencia entre campos.
     * Devuelve true solo si TODOS los campos son válidos.
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        // Limpiar errores previos antes de revalidar
        layoutName.error = null
        layoutEmail.error = null
        layoutPassword.error = null
        layoutConfirmPassword.error = null

        // 1. Nombre vacío o demasiado corto
        if (name.isEmpty()) {
            layoutName.error = "El nombre es obligatorio"
            isValid = false
        } else if (name.length < 2) {
            layoutName.error = "Ingresa tu nombre completo"
            isValid = false
        }

        // 2. Correo vacío o formato inválido
        if (email.isEmpty()) {
            layoutEmail.error = "El correo es obligatorio"
            isValid = false
        } else if (!isValidEmail(email)) {
            layoutEmail.error = "Ingresa un correo válido"
            isValid = false
        }

        // 3. Contraseña vacía o longitud insuficiente
        if (password.isEmpty()) {
            layoutPassword.error = "La contraseña es obligatoria"
            isValid = false
        } else if (password.length < 6) {
            layoutPassword.error = "Debe tener al menos 6 caracteres"
            isValid = false
        }

        // 4. Confirmación: solo se evalúa si la contraseña original ya es válida,
        // para no mostrar un error de "no coincide" cuando el problema real es otro
        if (password.isNotEmpty() && password.length >= 6) {
            if (confirmPassword.isEmpty()) {
                layoutConfirmPassword.error = "Confirma tu contraseña"
                isValid = false
            } else if (confirmPassword != password) {
                layoutConfirmPassword.error = "Las contraseñas no coinciden"
                isValid = false
            }
        }

        return isValid
    }

    /**
     * Verifica el formato del correo usando una expresión regular simple.
     * Cubre el patrón estándar: texto@dominio.extension
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return emailRegex.matches(email)
    }
}