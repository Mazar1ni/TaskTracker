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

        binding.datePicker.fragmentManager = parentFragmentManager
        binding.timePicker.fragmentManager = parentFragmentManager

        clearTimeTextField()

        binding.datePicker.selectionDateTime = viewModel.dueDate
        binding.datePicker.datePickerPositiveAction = {
            binding.timePicker.setDateTimeFieldVisibility(true)
            binding.datePicker.setClearIconVisibility(true)

            viewModel.dueDate = it

            binding.datePicker.setDateTimeTextVisibility(Utils.getStringOfDay(it, requireContext()))
        }
        binding.datePicker.clearButtonClickedAction = {
            clearDateTextField()
            clearTimeTextField()
        }

        binding.timePicker.selectionHourTime = viewModel.dueHour
        binding.timePicker.selectionMinuteTime = viewModel.dueMinute
        binding.timePicker.timePickerPositiveAction = { hour, minute ->
            binding.timePicker.setClearIconVisibility(true)

            viewModel.dueHour = hour
            viewModel.dueMinute = minute

            binding.timePicker.setDateTimeTextVisibility(Utils.getTimeFormat(hour, minute))
        }
        binding.timePicker.clearButtonClickedAction = {
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
        binding.timePicker.setDateTimeFieldVisibility(false)
        binding.timePicker.setClearIconVisibility(false)
        binding.timePicker.setDateTimeTextVisibility(null)
        viewModel.dueHour = null
        viewModel.dueMinute = null
    }

    private fun clearDateTextField() {
        binding.datePicker.setClearIconVisibility(false)
        binding.datePicker.setDateTimeTextVisibility(null)
        viewModel.dueDate = null
    }
}