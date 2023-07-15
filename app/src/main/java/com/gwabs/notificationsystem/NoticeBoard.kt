package com.gwabs.notificationsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.notificationsystem.databinding.ActivityNoticeBoardBinding
import com.google.firebase.database.*

class NoticeBoard : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBoardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var noticeAdapter: NoticeAdapter
    private var noticeList = mutableListOf<Notice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_board)

        binding = ActivityNoticeBoardBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val view = binding.root
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setContentView(view)

        if (isAdmin(auth.currentUser?.email.toString())){
            binding.addNoticeFAB.visibility = View.VISIBLE
            binding.addNoticeFAB.setOnClickListener {
                val intent = Intent(this, PostNotice::class.java)
                startActivity(intent)
            }
        }

        // Fetch and update the notices from Firebase
        getAllNotices()

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






    private fun getAllNotices() {
       showLoadingDialog(this)
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("notices")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear existing notices list
                noticeList.clear()
                // Iterate through all the children nodes
                for (childSnapshot in dataSnapshot.children) {
                    // Get the notice object from each child
                    val notice = childSnapshot.getValue(Notice::class.java)
                    notice?.let {
                        // Add the notice to the list
                        noticeList.add(it)
                    }
                }
                closeLoadingDialog()

                val layoutManager = LinearLayoutManager(this@NoticeBoard)
                binding.noticeRecyclerView.layoutManager = layoutManager

                // Initialize the notice adapter
                noticeAdapter = NoticeAdapter(noticeList)
                binding.noticeRecyclerView.adapter = noticeAdapter
                noticeAdapter.notifyDataSetChanged()

                if (noticeList.isEmpty()) {
                    binding.noNoticeTextView.visibility = View.VISIBLE
                } else {
                    binding.noNoticeTextView.visibility = View.GONE
                }
                // Process the notices list
                // e.g., update UI or perform any desired operations
            }

            override fun onCancelled(databaseError: DatabaseError) {
              closeLoadingDialog()
                Toast.makeText(
                    this@NoticeBoard,
                    databaseError.message,
                    Toast.LENGTH_SHORT,
                ).show()
                // Handle the error case
                // e.g., display an error message
            }
        })
    }

}