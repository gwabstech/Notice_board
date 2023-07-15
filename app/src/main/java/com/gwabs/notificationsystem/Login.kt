package com.gwabs.notificationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.notificationsystem.databinding.ActivityMainBinding



// ...


// Initialize Firebase Auth
class Login : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root



        setContentView(view)
        auth = FirebaseAuth.getInstance()


        binding.signupTextView.setOnClickListener {
            startSignupActivity()
        }
        binding.loginButton.setOnClickListener {
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
                login(email,password)
            }
        }

    }

    private fun startSignupActivity() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
    }

    private fun startDashboard() {
        val intent = Intent(this, NoticeBoard::class.java)
        startActivity(intent)
        finish()
    }

    fun login(email:String,password:String){

       showLoadingDialog(this)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   closeLoadingDialog()
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                   // val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    startDashboard()
                } else {
                    // If sign in fails, display a message to the user.
                  // Log.w(TAG, "signInWithEmail:failure", task.exception)
                    closeLoadingDialog()
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startDashboard()
        }
    }

}