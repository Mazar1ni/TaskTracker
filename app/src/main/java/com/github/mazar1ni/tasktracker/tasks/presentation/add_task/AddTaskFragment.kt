package com.github.mazar1ni.tasktracker.tasks.presentation.add_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.core.util.Utils
import com.github.mazar1ni.tasktracker.databinding.FragmentAddTaskBinding
import com.github.mazar1ni.tasktracker.tasks.domain.states.AddTaskState
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val viewModel: AddTaskViewModel by viewModels()

    @Inject
    lateinit var navigationUtil: NavigationUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater)

        binding.titleEditText.addTextChangedListener {
            clearErrorFields()
            viewModel.title = it.toString()
        }

        binding.descriptionEditText.addTextChangedListener {
            viewModel.description = it.toString()
        }

        binding.dueDateEditText.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_date)
                    .setSelection(viewModel.dueDate ?: MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                binding.dueTimeField.visibility = View.VISIBLE
                binding.dateClearIcon.visibility = View.VISIBLE

                viewModel.dueDate = it
                binding.dueDateEditText.setText(Utils.getStringOfDay(it, requireContext()))
            }
            datePicker.show(parentFragmentManager, null)
        }

        binding.dateClearIcon.setOnClickListener {
            clearDateTextField()
            clearTimeTextField()
        }

        binding.dueTimeEditText.setOnClickListener {
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTitleText(R.string.select_time)
                    .setHour(viewModel.dueHour ?: 0)
                    .setMinute(viewModel.dueMinute ?: 0)
                    .build()
            timePicker.addOnPositiveButtonClickListener {
                binding.timeClearIcon.visibility = View.VISIBLE

                viewModel.dueHour = timePicker.hour
                viewModel.dueMinute = timePicker.minute

                binding.dueTimeEditText.setText(
                    Utils.getTimeFormat(timePicker.hour, timePicker.minute)
                )
            }
            timePicker.show(parentFragmentManager, null)
        }

        binding.timeClearIcon.setOnClickListener {
            clearTimeTextField()
        }

        binding.createTask.setOnClickListener {
            viewModel.createTask()
        }

        viewModel.stateAction = { state ->
            clearErrorFields()

            when (state) {
                AddTaskState.AddTaskStateInProgress -> {
                    // TODO: add animation
                }
                AddTaskState.AddTaskStateEmptyTitle -> {
                    binding.titleField.error =
                        requireContext().getString(R.string.title_empty_error)
                }
                AddTaskState.AddTaskStateSuccess -> {
                    navigationUtil.popBackStack()
                }
            }
        }

        return binding.root
    }

    private fun clearErrorFields() {
        binding.titleField.error = null
    }

    private fun clearTimeTextField() {
        binding.dueTimeField.visibility = View.GONE
        binding.timeClearIcon.visibility = View.GONE
        binding.dueTimeEditText.text = null
        viewModel.dueHour = null
        viewModel.dueMinute = null
    }

    private fun clearDateTextField() {
        binding.dueDateEditText.text = null
        binding.dateClearIcon.visibility = View.GONE
        viewModel.dueDate = null
    }
}