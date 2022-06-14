package com.fabianshallari.pixabay.binding

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.fabianshallari.pixabay.extensions.onDestroy

class ViewBindingLazy<T : ViewBinding>(
    private val bindingProducer: (LayoutInflater) -> T,
    private val ownerProducer: () -> LifecycleOwner,
    private val inflaterProducer: () -> LayoutInflater
) : Lazy<T> {

    private var cached: T? = null

    override val value: T
        get() = cached ?: run {
            val lifecycleOwner: LifecycleOwner = ownerProducer()
            lifecycleOwner.onDestroy {
                cached = null
            }

            val layoutInflater: LayoutInflater = inflaterProducer()
            val binding: T = bindingProducer(layoutInflater)

            cached = binding
            return binding
        }

    override fun isInitialized(): Boolean = cached != null
}
