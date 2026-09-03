package com.narang.instantmechanic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.narang.instantmechanic.domain.Result
import com.narang.instantmechanic.domain.Mechanic
import com.narang.instantmechanic.databinding.FragmentHomeBinding
import com.narang.instantmechanic.navigation.MechanicDetailsDestination
import com.narang.instantmechanic.navigation.Navigator
import com.narang.instantmechanic.navigation.RequestsDestination
import com.narang.instantmechanic.ui.adapter.MechanicAdapter
import com.narang.instantmechanic.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mechanicAdapter: MechanicAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mechanicAdapter = MechanicAdapter(
            onMechanicClicked = { mechanic -> onMechanicClicked(mechanic) },
            onRequestClicked = { mechanic -> onRequestClicked(mechanic) }
        )

        binding.rvMechanics.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMechanics.adapter = mechanicAdapter
        binding.rvMechanics.isNestedScrollingEnabled = false

        observeViewModel()

        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.btnRetry.setOnClickListener { viewModel.retry() }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    showError(false, null)
                    mechanicAdapter.submitList(result.data)
                    if (result.data.isEmpty()) {
                        showEmpty(true)
                    } else {
                        showEmpty(false)
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(true, result.message)
                    Timber.e("HomeFragment error: %s", result.message)
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG)
                        .setAction("Retry") { viewModel.retry() }
                        .show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.swipeRefresh.isRefreshing = isLoading
        if (isLoading) binding.tvError.visibility = View.GONE
    }

    private fun showError(show: Boolean, message: String?) {
        binding.tvError.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRetry.visibility = if (show) View.VISIBLE else View.GONE
        if (message != null) binding.tvError.text = message
    }

    private fun showEmpty(show: Boolean) {
        binding.tvEmpty.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun onMechanicClicked(mechanic: Mechanic) {
        if (!isAdded || parentFragmentManager.isStateSaved) {
            Timber.w("HomeFragment onMechanicClicked() aborted: isAdded=%s isStateSaved=%s", isAdded, parentFragmentManager.isStateSaved)
            return
        }
        Navigator.navigate(parentFragmentManager, MechanicDetailsDestination)
    }

    private fun onRequestClicked(mechanic: Mechanic) {
        if (!isAdded || parentFragmentManager.isStateSaved) {
            Timber.w("HomeFragment onRequestClicked() aborted: isAdded=%s isStateSaved=%s", isAdded, parentFragmentManager.isStateSaved)
            return
        }
        Navigator.navigate(parentFragmentManager, RequestsDestination)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // demoMechanics moved to FakeMechanicRepository - kept here only for preview fallback
    companion object {
        @Deprecated("Use FakeMechanicRepository demo data")
        private val demoMechanics = listOf(
            Mechanic(1, "Swift Fix Auto", "4.9", "Brooklyn, NY • 1.2 km", "Open"),
            Mechanic(2, "Metro Motor Works", "4.8", "Queens, NY • 2.7 km", "Open"),
            Mechanic(3, "Reliable Auto Care", "4.7", "Manhattan, NY • 3.5 km", "Closes 6 PM"),
        )
    }
}
