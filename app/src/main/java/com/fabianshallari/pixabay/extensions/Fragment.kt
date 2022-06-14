package com.fabianshallari.pixabay.extensions

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.fabianshallari.pixabay.binding.ViewBindingLazy

fun <T : ViewBinding> Fragment.viewBinding(
    bindingProducer: (LayoutInflater) -> T
): Lazy<T> = ViewBindingLazy(
    ownerProducer = { viewLifecycleOwner },
    inflaterProducer = { layoutInflater },
    bindingProducer = bindingProducer
)
