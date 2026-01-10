package com.haris.expensetracker.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object ConfirmationDialogeHelper {

    fun showConfirmationDialog(
        context: Context,
        title: String = "Exit",
        message: String = "Do you really want to leave without saving?",
        onConfirm: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false) // Prevents clicking outside to dismiss
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { _, _ ->
                onConfirm()
            }
            .show()
    }
}