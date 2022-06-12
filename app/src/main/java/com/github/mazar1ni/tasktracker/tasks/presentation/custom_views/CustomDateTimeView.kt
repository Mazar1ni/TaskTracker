package com.github.mazar1ni.tasktracker.tasks.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.databinding.CustomDateTimeViewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker


class CustomDateTimeView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var selectionDateTime: Long? = null

    var selectionHourTime: Int? = null
    var selectionMinuteTime: Int? = null

    var datePickerPositiveAction: ((Long) -> Unit)? = null
    var timePickerPositiveAction: ((Int, Int) -> Unit)? = null
    var clearButtonClickedAction: (() -> Unit)? = null

    var fragmentManager: FragmentManager? = null

    private var titleText: String?
    private val binding: CustomDateTimeViewBinding

    init {
        gravity = HORIZONTAL

        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.TaskTracker)

        val dateTimeHint = attributeSet.getString(R.styleable.TaskTracker_dateTimeHint)
        titleText = attributeSet.getString(R.styleable.TaskTracker_dateTimeTitle)
        val dateTimeType = attributeSet.getInteger(R.styleable.TaskTracker_dateTimeType, 0)

        binding = CustomDateTimeViewBinding.inflate(LayoutInflater.from(context), this)

        binding.dateTimeField.hint = dateTimeHint

        binding.dateTimeEditText.setOnClickListener {
            when (dateTimeType) {
                0 -> createDatePicker()
                1 -> createTimePicker()
            }
        }

        binding.clearIcon.setOnClickListener {
            clearButtonClickedAction?.invoke()
        }
    }

    fun setDateTimeFieldVisibility(isVisible: Boolean) {
        binding.dateTimeField.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setDateTimeTextVisibility(text: String?) {
        binding.dateTimeEditText.setText(text)
    }

    fun setClearIconVisibility(isVisible: Boolean) {
        binding.clearIcon.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun createDatePicker() =
        fragmentManager?.let {
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(titleText)
                .setSelection(selectionDateTime ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build().run {
                    addOnPositiveButtonClickListener {
                        selectionDateTime = it
                        datePickerPositiveAction?.invoke(it)
                    }
                    show(it, null)
                }
        }

    private fun createTimePicker() =
        fragmentManager?.let {
            MaterialTimePicker.Builder()
                .setTitleText(titleText)
                .setHour(selectionHourTime ?: 0)
                .setMinute(selectionMinuteTime ?: 0)
                .build().run {
                    addOnPositiveButtonClickListener {
                        selectionHourTime = hour
                        selectionMinuteTime = minute
                        timePickerPositiveAction?.invoke(hour, minute)
                    }
                    show(it, null)
                }
        }

}