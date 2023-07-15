package com.gwabs.notificationsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gwabs.notificationsystem.databinding.ActivityNoticeDetailsBinding

class NoticeDetails : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val title = intent.getStringExtra("title")
        val dateAndTime = intent.getStringExtra("dateAndTime")
        val description = intent.getStringExtra("post")

        // Set the data to the corresponding views
        binding.noticeTitleTextView.text = title
        binding.noticeDateAndTimeTextView.text = dateAndTime
        binding.noticeDescriptionTextView.text = description

        // Set up the toolbar
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title

        // Set the click listener on the back arrow button
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Close the activity when the back arrow button is clicked
        return true
    }
}
