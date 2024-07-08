package com.example.myapplication.db.util

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.HistoryFragmentAddDialogBinding
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.repository.MealRepository
import com.example.myapplication.ui.history.HistoryViewModel
import com.example.myapplication.ui.menu.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class HistoryDialogFragment(
    private val mealList: List<MealState>
) : DialogFragment() {

    private var _binding: HistoryFragmentAddDialogBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by activityViewModels()
    private var selectedDate: Date? = null
    private var selectedMeal: MealState? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealSpinner: Spinner = binding.mealSpinner
        val dateButton = binding.dateButton
        val saveButton = binding.saveButton

        val mealNames = mealList.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mealSpinner.adapter = adapter

        // TODO: can be moved to separate class and assigned with onItemSelectedListener
        mealSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedMeal = mealList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedMeal = null
            }
        }

        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time
                    dateButton.text =
                        selectedDate?.let { it1 ->
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it1)
                        }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        saveButton.setOnClickListener {
            if (selectedDate != null && selectedMeal != null) {
                val historyItem = HistoryState(item = selectedMeal!!.name, dateAdded = selectedDate!!)
                historyViewModel.saveHistoryItem(historyItem)
                dismiss()
                Toast.makeText(context, "Meal saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please select a meal and a date", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
