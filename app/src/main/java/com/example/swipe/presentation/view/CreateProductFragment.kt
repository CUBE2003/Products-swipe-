package com.example.swipe.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.swipe.R
import com.example.swipe.data.remote.model.ProductDetails
import com.example.swipe.databinding.FragmentCreateProductBinding
import com.example.swipe.presentation.MyViewModel
import com.example.swipe.utils.Resource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProductFragment : Fragment() {

    private val productViewModel: MyViewModel by viewModel()
    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageViewProduct.setImageURI(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSelectImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageLauncher.launch("image/*")
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageLauncher.launch("image/*")
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val productName = binding.editTextProductName.text.toString()
            val price = binding.editTextPrice.text.toString()
            val productType = binding.editTextProductType.text.toString()
            val tax = binding.editTextTax.text.toString()

            if (validateInput(productName, price, productType, tax)) {
                val productDetails = ProductDetails(
                    image = "", // Image will be handled separately
                    price = price,
                    product_name = productName,
                    product_type = productType,
                    tax = tax
                )
                productViewModel.createProduct(productDetails, selectedImageUri)
            } else {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            productViewModel.productCreationStatus.collect { resource ->
                when (resource) {
                    is Resource.Idle -> doNothing()
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> handleSuccess()
                    is Resource.Error -> handleError()
                }
            }
        }
    }

    private fun handleError() {
        doNothing()
        Toast.makeText(context, "Failed to create product:", Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess() {
        doNothing()
        Toast.makeText(context, "Product created successfully", Toast.LENGTH_SHORT).show()
        clearForm()
        findNavController().navigate(R.id.action_createProductFragment_to_productFragment)
    }

    private fun clearForm() {
        binding.editTextProductName.text?.clear()
        binding.editTextPrice.text?.clear()
        binding.editTextProductType.text?.clear()
        binding.editTextTax.text?.clear()
        binding.imageViewProduct.setImageURI(null)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonSubmit.isEnabled = false
    }

    private fun doNothing() {
        binding.progressBar.visibility = View.GONE
        binding.buttonSubmit.isEnabled = true
    }

    private fun validateInput(productName: String, price: String, productType: String, tax: String): Boolean {
        val isPriceValid = price.contains(".")
        val isTaxValid = tax.contains(".")
        val isProductNameNotEmpty = productName.isNotEmpty()
        val isProductTypeNotEmpty = productType.isNotEmpty()

        if (!isPriceValid) {
            Toast.makeText(context, "Price should end with a decimal", Toast.LENGTH_SHORT).show()
        }
        if (!isTaxValid) {
            Toast.makeText(context, "Tax should end with decimal", Toast.LENGTH_SHORT).show()
        }

        return isPriceValid && isTaxValid && isProductNameNotEmpty && isProductTypeNotEmpty
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
