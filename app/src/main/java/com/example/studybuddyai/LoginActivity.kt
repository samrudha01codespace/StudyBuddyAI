package com.example.studybuddyai

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studybuddyai.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var passwordEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("PrivateResource")
    private fun togglePasswordVisibility(passwordEditText: EditText, togglePassword: ImageView) {
        if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // Show password
            passwordEditText.transformationMethod = null
            togglePassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility)
        } else {
            // Hide password
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility_off)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val passwordEditText: EditText = findViewById(R.id.txtPassword)
        val togglePassword: ImageView = findViewById(R.id.togglePassword)
        progressBar = findViewById(R.id.progressBar)

        togglePassword.setOnClickListener {
            togglePasswordVisibility(passwordEditText, togglePassword)
        }

        binding.txtSignUpNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginNow.setOnClickListener {
            val email = binding.txtEmailAddress.text.toString()
            val pass = binding.txtPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                progressBar.visibility = VISIBLE

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {

                    if (it.isSuccessful) {
                        progressBar.visibility = INVISIBLE
                        // Inside your LoginActivity after successful login
                        saveLoginDetails(email , pass)
                        val intent = Intent(this, MainActivity::class.java)

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {

                        Toast.makeText(
                            this,
                            "Incorrect Fields or Check your Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    }

                }
            } else {
                Toast.makeText(this, "Empty Fields are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }

        retrieveLoginDetailsAndSignIn()
    }
    private fun saveLoginDetails(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun retrieveLoginDetailsAndSignIn() {
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        if (email!!.isNotEmpty() && password!!.isNotEmpty()) {

            progressBar.visibility= VISIBLE

            // Attempt to sign in the user using saved credentials
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        progressBar.visibility = INVISIBLE
                        // Inside your LoginActivity after successful login
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        // Clear saved credentials if login fails
                        clearSavedCredentials()
                    }
                }
        }
    }

    private fun clearSavedCredentials() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }

}