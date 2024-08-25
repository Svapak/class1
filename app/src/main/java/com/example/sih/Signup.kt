package com.example.sih

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sih.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.intentSignIn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            pd = ProgressDialog(this)
            pd.setMessage("Creating your account...")
            pd.show()
            val email = binding.EmailEntryField.text.toString()
            val pass = binding.passwordEntryField.text.toString()
            val cpass = binding.confirmPasswordEntryField.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty()) {
                if (cpass == pass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            pd.dismiss()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            pd.dismiss()
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    pd.dismiss()
                    Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                pd.dismiss()
                Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}