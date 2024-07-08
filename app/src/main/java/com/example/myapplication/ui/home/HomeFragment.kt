package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.shuffleButton.setOnClickListener {
            viewModel.shuffleList()
            Snackbar.make(root, getString(R.string.list_shuffled), Snackbar.LENGTH_SHORT).show()
        }

        binding.randomButton.setOnClickListener {
            viewModel.getNextWord()
        }

        binding.addToListButton.setOnClickListener {
            viewModel.saveWordToHistory()
        }

        viewModel.currentDish.observe(viewLifecycleOwner) { dish ->
            binding.dishText.text = dish
        }
        return root
    }

    override fun onPause() {
        super.onPause()
        viewModel.viewModelScope.launch {
            viewModel.saveList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.viewModelScope.launch {
            viewModel.saveList()
        }
    }
}