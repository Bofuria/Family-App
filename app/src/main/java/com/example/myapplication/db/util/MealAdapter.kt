package com.example.myapplication.db.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.ui.menu.MenuFragment
import com.example.myapplication.ui.menu.MenuViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
class MealAdapter(private val viewModel: MenuViewModel, context: Context, private val items: MutableList<MealState>)
    : ArrayAdapter<MealState>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.meal_list_item_layout, parent, false)
        }

        val item = items[position]

        val meal = getItem(position)
        val mealName = itemView!!.findViewById<TextView>(R.id.meal_item_text)
        val menuButton: ImageView = itemView.findViewById(R.id.menu_button)

        mealName.text = item.name

        menuButton.setOnClickListener {
            val popupMenu = PopupMenu(context, menuButton)
            popupMenu.inflate(R.menu.edit_list_item_options)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_item -> {
                        meal?.let {
                            // Show a dialog or another way to get new name
                            editAlert(meal)
                        }
                        true
                    }
                    R.id.delete_item -> {
                        meal?.let {
                            deleteAlert(meal)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
        return itemView
    }

    private fun editAlert(item : MealState) {
        val builder = AlertDialog.Builder(context)

        with(builder) {
            setTitle("Rename ${item.name}")
            val input = EditText(context)
            setView(input)
            setPositiveButton("OK") { dialog,_ ->
                val newName = input.text.toString()
                viewModel.updateMeal(item.id, newName)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog,_ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun deleteAlert(item : MealState) {
        val builder = AlertDialog.Builder(context)

        with(builder) {
            setTitle("Delete ${item.name}")
            setMessage("Are you sure you want to delete '${item.name}'?")
            setPositiveButton("OK") { dialog,_ ->
                viewModel.deleteMeal(item)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog,_ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun setItems(newItems: List<MealState>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}