package com.fabianshallari.pixabay.extensions

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.onDestroy(block: (LifecycleOwner) -> Unit) {
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                lifecycle.removeObserver(this)
                block(owner)
            }
        }
    )
}
