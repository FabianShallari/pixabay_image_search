package com.fabianshallari.pixabay.extensions

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun EditText.textChanges(): Flow<String> = callbackFlow {
    val listener = doAfterTextChanged { text -> trySend(text?.toString().orEmpty()) }
    awaitClose { removeTextChangedListener(listener) }
}