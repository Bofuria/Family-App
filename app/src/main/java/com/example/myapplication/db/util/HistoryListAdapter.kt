package com.example.myapplication.db.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.HistoryListItemLayoutBinding
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.ui.history.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
class HistoryListAdapter(private val viewModel: HistoryViewModel, private val context: Context) :
    ListAdapter<HistoryState, HistoryListAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryState>() {
            override fun areItemsTheSame(oldItem: HistoryState, newItem: HistoryState): Boolean {
                return oldItem.id == newItem.id // Adjust if you have a unique identifier
            }

            override fun areContentsTheSame(oldItem: HistoryState, newItem: HistoryState): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class HistoryViewHolder(private val binding: HistoryListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val historyItemText: TextView = binding.historyItemText
        private val dateAddedText: TextView = binding.dateAddedText

        fun bind(item: HistoryState) {
            historyItemText.text = item.item
            dateAddedText.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(item.dateAdded)

            binding.menuButton.setOnClickListener {
                val popupMenu = PopupMenu(binding.root.context, binding.menuButton)

                popupMenu.inflate(R.menu.edit_list_item_options)

                val menu = popupMenu.menu
                menu.findItem(R.id.edit_item).isVisible = false

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.delete_item -> {
                            // Implement the delete functionality
                            deleteAlert(item)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }
    private fun deleteAlert(item : HistoryState) {
        val builder = AlertDialog.Builder(context)

        with(builder) {
            setTitle("Delete ${item.item}")
            setMessage("Are you sure you want to delete '${item.item}'?")
            setPositiveButton("OK") { dialog,_ ->
                viewModel.deleteHistoryItem(item)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog,_ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }
}
