package com.narang.instantmechanic.navigation

import androidx.fragment.app.FragmentManager
import com.narang.instantmechanic.R
import timber.log.Timber

object Navigator {

    fun navigate(
        fragmentManager: FragmentManager,
        destination: Destination,
        addToBackStack: Boolean = true,
    ) {
        if (fragmentManager.isStateSaved) {
            Timber.w("navigate() aborted: FragmentManager state already saved for %s", destination.tag)
            return
        }
        fragmentManager.beginTransaction()
            .replace(
                R.id.nav_host_fragment_container,
                destination.newInstance(),
                destination.tag
            ).apply {
                if (addToBackStack) addToBackStack(destination.tag)
            }.commit()
    }

    fun navigate(
        destination: Destination,
        fragmentManager: FragmentManager,
        addToBackStack: Boolean = true,
    ) = navigate(fragmentManager, destination, addToBackStack)

    fun navigateToRoot(
        fragmentManager: FragmentManager,
        destination: Destination,
    ) {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        navigate(fragmentManager, destination, addToBackStack = false)
    }

    fun navigateToRoot(
        destination: Destination,
        fragmentManager: FragmentManager,
    ) = navigateToRoot(fragmentManager, destination)
}
