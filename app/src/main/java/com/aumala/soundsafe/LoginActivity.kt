package com.aumala.soundsafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aumala.soundsafe.data.local.AppDatabase
import com.aumala.soundsafe.util.PasswordHasher
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutPassword: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        layoutEmail = findViewById(R.id.layoutEmail)
        layoutPassword = findViewById(R.id.layoutPassword)
        emailEditText = findViewById(R.id.editEmail)
        passwordEditText = findViewById(R.id.editPassword)

        val buttonLogin = findViewById<MaterialButton>(R.id.buttonLogin)
        val textGoToRegister = findViewById<android.widget.TextView>(R.id.textGoToRegister)

        buttonLogin.setOnClickListener {
            if (validateInputs()) {
                attemptLogin()
            }
        }

        textGoToRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    /**
     * Intenta autenticar al usuario contra Room.
     * Se ejecuta en una coroutine porque las consultas a Room son operaciones
     * de base de datos y NUNCA deben correr en el hilo principal (UI thread).
     */
    private fun attemptLogin() {
        val correo = emailEditText.text.toString().trim()
        val contrasenaPlano = passwordEditText.text.toString()

        lifecycleScope.launch {
            // 1. La contraseña ingresada se convierte en hash con la MISMA función
            //    que se usó al registrar al usuario.
            val hashIngresado = PasswordHasher.hash(contrasenaPlano)

            // 2. Se consulta Room comparando correo + hash en una sola operación.
            val dao = AppDatabase.getDatabase(applicationContext).usuarioDao()
            val usuario = dao.verificarCredenciales(correo, hashIngresado)

            // 3. Si la fila existe, las credenciales son correctas.
            if (usuario != null) {
                irAPantallaPrincipal()
            } else {
                // Mensaje genérico — no se revela si el problema fue el correo o la contraseña.
                layoutPassword.error = "Correo o contraseña incorrectos"
            }
        }
    }

    /**
     * Navega a la pantalla principal y limpia la pila de actividades,
     * para que el usuario no pueda regresar al login con el botón Atrás.
     */
    private fun irAPantallaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Valida formato antes de tocar la base de datos.
     * Orden: vacío -> formato -> longitud (igual que en la teoría revisada antes).
     */
    private fun validateInputs(): Boolean {
        var isValid = true

        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        layoutEmail.error = null
        layoutPassword.error = null

        if (email.isEmpty()) {
            layoutEmail.error = "El correo es obligatorio"
            isValid = false
        } else if (!isValidEmail(email)) {
            layoutEmail.error = "Ingresa un correo válido"
            isValid = false
        }

        if (password.isEmpty()) {
            layoutPassword.error = "La contraseña es obligatoria"
            isValid = false
        } else if (password.length < 6) {
            layoutPassword.error = "Debe tener al menos 6 caracteres"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return emailRegex.matches(email)
    }
}