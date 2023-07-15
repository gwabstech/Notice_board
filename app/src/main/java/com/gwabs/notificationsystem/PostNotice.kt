package com.gwabs.notificationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.notificationsystem.databinding.ActivityPostNoticeBinding
import com.google.firebase.database.FirebaseDatabase

class PostNotice : AppCompatActivity() {
    private lateinit var binding: ActivityPostNoticeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostNoticeBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()


        val view = binding.root
        setContentView(view)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Post Notice"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.submitBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val post = binding.descriptionEditText.text.toString()
            postNotice(title,post)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun postNotice(title:String,post:String){
        if (TextUtils.isEmpty(title)){
            binding.titleEditText.error = " Title cant be empty"
        }else if (TextUtils.isEmpty(post)){
            binding.descriptionEditText.error = "Post is required"
        }else{
            val notice = Notice(title, post, getDateAndTime())
            sendPost(notice)
        }
    }

    private fun sendPost(notice: Notice){
        showLoadingDialog(this)


        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("notices")

        // Generate a unique key for the notice
        val noticeKey = reference.push().key

        // Set the notice object under the generated key
        reference.child(noticeKey!!).setValue(notice)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Post sent successfully
                    closeLoadingDialog()
                    Toast.makeText(this,"Posted Successfully",Toast.LENGTH_SHORT).show()
                    finish()
                    // Handle success case here
                } else {
                    // Failed to send post
                    closeLoadingDialog()
                    Toast.makeText(this,"error ${task.exception.toString()}",Toast.LENGTH_SHORT).show()
                    // Handle failure case here
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Close the activity when the back arrow button is clicked
        return true
    }
}