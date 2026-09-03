package com.narang.instantmechanic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.narang.instantmechanic.databinding.FragmentServiceRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestsFragment : Fragment() {

    private var _binding: FragmentServiceRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentServiceRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.tvAppName.updatePadding(top = systemBars.top)
            insets
        }

        val services = listOf(
            "Engine Diagnostics",
            "Oil Change",
            "Brake Service",
            "Tire Rotation",
            "Battery Replacement",
        )
        val serviceAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            services
        )
        binding.actvServiceType.setAdapter(serviceAdapter)

        binding.etCustomerName.setText("John Doe")
        binding.etPhoneNumber.setText("(555) 123-4567")
        binding.etVehicleId.setText("ABC-1234")
        binding.actvServiceType.setText(services.first(), false)
        binding.etProblemDescription.setText(
            "The check-engine light is on and the car is making a rattling sound."
        )

        binding.btnSubmitRequest.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Demo request submitted successfully",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
