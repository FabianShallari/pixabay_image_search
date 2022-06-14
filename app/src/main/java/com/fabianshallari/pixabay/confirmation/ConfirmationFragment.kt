package com.fabianshallari.pixabay.confirmation

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.fabianshallari.pixabay.R
import com.fabianshallari.pixabay.navigation.Routes

class ConfirmationFragment : DialogFragment() {

    companion object {
        const val CONFIRMATION_REQUEST = "confirmation_request"
        const val CONFIRMATION_RESPONSE = "confirmation_response"
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setFragmentResult(CONFIRMATION_REQUEST, createConfirmationResponse(false))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog
            .Builder(requireActivity())
            .setTitle(getString(R.string.confirm_navigation))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(R.string.yes) { _, _ ->
                setFragmentResult(CONFIRMATION_REQUEST, createConfirmationResponse(true))
                dismiss()
            }
            .setCancelable(true)
            .setNegativeButton(R.string.no) { _, _ ->
                setFragmentResult(CONFIRMATION_REQUEST, createConfirmationResponse(false))
                dismiss()
            }
            .create()

    }

    private fun createConfirmationResponse(response: Boolean): Bundle =
        bundleOf(CONFIRMATION_RESPONSE to response)
}