package com.gwabs.notificationsystem

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.notificationsystem.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.emailEditText.text.toString())){
                binding.emailEditText.error = "Email is required "
            }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()){
                binding.emailEditText.error = "Invalid email"
            }else if (TextUtils.isEmpty(binding.passwordEditText.text.toString())){
                binding.passwordEditText.error = "Password is required"
            }else if (binding.passwordEditText.text.toString().length < 6 || binding.passwordEditText.text.toString().length > 8){
                binding.passwordEditText.error = "Password should be 6 to 8 char"
            }else{
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                signup(email,password)
            }
        }

    }

    private fun signup(email:String, password:String){
      showLoadingDialog(this)
        // email = emailEditText.text.toString().trim()
        // password = passwordEditText.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    closeLoadingDialog()
                    // Sign in success, update UI with the signed-in user's information
                   // Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                   startLoginActivity()
                } else {
                    closeLoadingDialog()
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed. try again",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}