package com.gwabs.notificationsystem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(private val notices: List<Notice>) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val currentNotice = notices[position]
        holder.bind(currentNotice)
    }

    override fun getItemCount(): Int {
        return notices.size
    }

    inner class NoticeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.noticeTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.noticeDescriptionTextView)
        private val dateAndTime: TextView = itemView.findViewById(R.id.noticeDateAndTimeTextView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(notice: Notice) {
            titleTextView.text = notice.title
            descriptionTextView.text = notice.description
            dateAndTime.text = notice.timestamp.toString()
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedNotice = notices[position]
                // Replace NoticeDetailsActivity::class.java with your actual notice details activity
                val intent = Intent(view.context, NoticeDetails::class.java)
                intent.putExtra("title", clickedNotice.title)
                intent.putExtra("dateAndTime", clickedNotice.timestamp)
                intent.putExtra("post", clickedNotice.description)
                view.context.startActivity(intent)
            }
        }
    }

}
