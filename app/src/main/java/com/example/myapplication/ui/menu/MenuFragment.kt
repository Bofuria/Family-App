package com.example.myapplication.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentMenuBinding
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.util.HistoryListAdapter
import com.example.myapplication.db.util.MealAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: MealAdapter
    private lateinit var mealsListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mealsListView = binding.mealsListView
        viewModel.loadMeals()

        adapter = MealAdapter(viewModel, requireContext(), mutableListOf())
        mealsListView.adapter = adapter

        viewModel.meals.observe(viewLifecycleOwner) { updatedList ->
            adapter.setItems(updatedList)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        binding.addButton.setOnClickListener {
            addAlert()
        }

        return root
    }

    private fun addAlert() {
        val builder = AlertDialog.Builder(requireContext())

        with(builder) {
            setTitle("Add new meal")
            val input = EditText(context)
            setView(input)
            setPositiveButton("OK") { dialog,_ ->
                val newName = input.text.toString()
                viewModel.addNewMeal(newName)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog,_ ->
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}