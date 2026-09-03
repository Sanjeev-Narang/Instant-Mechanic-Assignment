package com.narang.instantmechanic.navigation

import androidx.fragment.app.Fragment

interface Destination {
    val tag: String
    fun newInstance(): Fragment
}
