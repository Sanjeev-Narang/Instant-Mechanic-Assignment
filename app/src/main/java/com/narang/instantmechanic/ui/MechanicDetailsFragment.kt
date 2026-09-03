package com.narang.instantmechanic.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.narang.instantmechanic.databinding.FragmentMechanicDetailsBinding
import com.narang.instantmechanic.navigation.Navigator
import com.narang.instantmechanic.navigation.RequestsDestination
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MechanicDetailsFragment : Fragment() {

    private var _binding: FragmentMechanicDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMechanicDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.btnRequestService.setOnClickListener {
            if (!isAdded || parentFragmentManager.isStateSaved) {
                Timber.w("MechanicDetailsFragment btnRequestService aborted: isAdded=%s isStateSaved=%s", isAdded, parentFragmentManager.isStateSaved)
                return@setOnClickListener
            }
            Navigator.navigate(parentFragmentManager, RequestsDestination)
        }
        binding.btnCallShop.setOnClickListener {
            try {
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:5551234567"))
                startActivity(callIntent)
            } catch (e: ActivityNotFoundException) {
                Timber.w(e, "MechanicDetailsFragment call intent FAILED - no dialer found")
            }
        }
        binding.btnDirections.setOnClickListener {
            try {
                val directionsIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=123+Mechanic+Lane,+Auto+City")
                )
                startActivity(directionsIntent)
            } catch (e: ActivityNotFoundException) {
                Timber.w(e, "MechanicDetailsFragment directions intent FAILED - no maps app found")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
