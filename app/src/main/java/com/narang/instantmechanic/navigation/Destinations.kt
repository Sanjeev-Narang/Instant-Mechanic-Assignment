package com.narang.instantmechanic.navigation

import androidx.fragment.app.Fragment
import com.narang.instantmechanic.ui.HomeFragment
import com.narang.instantmechanic.ui.MechanicDetailsFragment
import com.narang.instantmechanic.ui.ProfileFragment
import com.narang.instantmechanic.ui.RequestsFragment

object HomeDestination : Destination {
    override val tag: String = "HomeFragment"
    override fun newInstance(): Fragment = HomeFragment()
}

object RequestsDestination : Destination {
    override val tag: String = "RequestsFragment"
    override fun newInstance(): Fragment = RequestsFragment()
}

object ProfileDestination : Destination {
    override val tag: String = "ProfileFragment"
    override fun newInstance(): Fragment = ProfileFragment()
}

object MechanicDetailsDestination : Destination {
    override val tag: String = "MechanicDetailsFragment"
    override fun newInstance(): Fragment = MechanicDetailsFragment()
}
