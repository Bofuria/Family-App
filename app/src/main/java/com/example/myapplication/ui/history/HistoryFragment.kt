package com.example.myapplication.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.example.myapplication.databinding.HistoryFragmentAddDialogBinding
import com.example.myapplication.databinding.HistoryListItemLayoutBinding
import com.example.myapplication.db.util.HistoryListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by activityViewModels()
    private lateinit var historyListView: RecyclerView

    private val menuHost: MenuHost by lazy {
        requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyListView = binding.historyListView

        val adapter = HistoryListAdapter(viewModel, requireContext())
        historyListView.adapter = adapter
        historyListView.layoutManager = LinearLayoutManager(context)

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.history_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_add -> {
                        viewModel.showDialog()
                    }
//                    android.R.id.home -> {
//                        findNavController().navigateUp()
//                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.historyList.observe(viewLifecycleOwner, Observer { updatedList ->
            Log.d("HistoryFragment", "Observer triggered with updatedList: $updatedList")
            adapter.submitList(updatedList)
        })

        viewModel.showDialogEvent.observe(viewLifecycleOwner) { dialog ->
            dialog?.show(parentFragmentManager, "HistoryDialogFragment")
        }

        viewModel.mealsList.observe(viewLifecycleOwner, { updatedList ->

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
