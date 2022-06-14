package com.fabianshallari.pixabay.extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

inline fun <reified T : Adapter<out RecyclerView.ViewHolder>> requireAdapter(recyclerView: RecyclerView): T {
    val adapter = recyclerView.adapter
    if (adapter is T) {
        return adapter
    } else {
        throw IllegalArgumentException("Required value was not of type ${T::class}")
    }
}