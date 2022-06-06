package com.github.mazar1ni.tasktracker.core.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.github.mazar1ni.tasktracker.databinding.ProgressDialogBinding

object DialogUtil {

    fun showProgressDialog(context: Context): Dialog {
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        return Dialog(context).apply {
            setCancelable(false)
            setContentView(binding.root)
            show()
        }
    }

}