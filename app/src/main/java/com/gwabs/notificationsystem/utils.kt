package com.gwabs.notificationsystem

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private lateinit var dialog: Dialog
fun checkEmaiandPassword(email: String, password: String): Boolean{
    return (!TextUtils.isEmpty(email) && password.length >= 4)
}

fun showLoadingDialog(context: Context){
    dialog = Dialog(context, R.style.loading_style)
    dialog.setContentView(R.layout.progress_dialog)
    val message: TextView = dialog.findViewById(R.id.loading_msg)
    message.text = "Please Wait"
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    if (!dialog.isShowing) {
        dialog.show()
    } else {
        closeLoadingDialog()
        dialog.show()
    }
}


fun closeLoadingDialog(){
    if (dialog.isShowing){
        dialog.dismiss()
    }
}
fun isAdmin(email: String):Boolean{
    return email == "admin@admin.com"
}
fun getDateAndTime(): String {
    val calendar = Calendar.getInstance()

// Define the desired date and time format
    val dateFormat = SimpleDateFormat("EEEE, MMM yyyy h:mma", Locale.getDefault())

// Format the current date and time

    return dateFormat.format(calendar.time)
}


