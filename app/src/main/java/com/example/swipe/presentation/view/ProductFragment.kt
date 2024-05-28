package com.example.swipe.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipe.databinding.FragmentProductBinding
import com.example.swipe.presentation.MyViewModel
import com.example.swipe.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductFragment : Fragment() {

    private val productViewModel: MyViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = productAdapter
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = productViewModel.products.value.data?.filter {
            it.product_name?.contains(query, ignoreCase = true) == true
        }
        productAdapter.submitList(filteredList)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            productViewModel.products.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
//                        binding.recyclerView.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        productAdapter.submitList(resource.data)
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.VISIBLE
                        binding.errorTextView.text = resource.message
                        binding.recyclerView.visibility = View.GONE
                    }

                    is Resource.Idle -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
