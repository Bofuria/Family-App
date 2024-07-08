package com.example.myapplication.db.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

class CustomToast {
    fun showToast(context: Context, message: String, toastType: ToastType) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflater.inflate(R.layout.custom_toast, null)

        val text = toastView.findViewById<TextView>(R.id.toast_text)
        text.text = message

        val container = toastView.findViewById<LinearLayout>(R.id.custom_toast_container)
        container.setBackgroundResource(toastType.color)

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = toastView
        toast.show()
    }
}