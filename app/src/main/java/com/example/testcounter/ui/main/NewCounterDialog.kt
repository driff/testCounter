package com.example.testcounter.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.testcounter.MainActivity
import com.example.testcounter.R
import kotlinx.android.synthetic.main.single_input_layout.view.*
import javax.inject.Inject

class NewCounterDialog: DialogFragment() {

    val TAG = this.javaClass.canonicalName

    @Inject lateinit var clickListener: IDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val input = requireActivity().layoutInflater.inflate(R.layout.single_input_layout, null)
            val builder = AlertDialog.Builder(it)
            builder.setView(input)
                .setTitle(R.string.new_counter_title)
                .setNegativeButton(R.string.new_counter_negative_action) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.new_counter_positive_action) { dialog, _ ->
                    Log.d(TAG, "Positive Button Clicked")
                    clickListener.onDialogPositiveClick(input.txtNewTitle.text.toString())
                    dialog.dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as MainActivity).activityComponent.inject(this)
    }
}