package com.github.mazar1ni.tasktracker.tasks.presentation.edit_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.core.util.Utils
import com.github.mazar1ni.tasktracker.databinding.FragmentEditTaskBinding
import com.github.mazar1ni.tasktracker.tasks.domain.states.EditTaskState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: EditTaskViewModel by viewModels()

    private val args: EditTaskFragmentArgs by navArgs()

    @Inject
    lateinit var navigationUtil: NavigationUtil

    private var editTaskState: EditTaskState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater)

        binding.titleEditText.addTextChangedListener(viewModel.titleWatcher)
        binding.descriptionEditText.addTextChangedListener(viewModel.descriptionWatcher)

        binding.datePicker.fragmentManager = parentFragmentManager
        binding.timePicker.fragmentManager = parentFragmentManager

        clearDateTextField()
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

        binding.deleteTask.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.message_dialog_delete_task))
                .setNegativeButton(resources.getString(R.string.cancel_button_name)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.delete_button_name)) { dialog, _ ->
                    viewModel.deleteTask()
                    dialog.dismiss()
                }
                .show()
        }

        binding.updateTask.setOnClickListener {
            if (editTaskState == null || editTaskState !is EditTaskState.EditTaskStateSuccess)
                viewModel.saveEditTask()
        }

        viewModel.taskDomainModel.observe(viewLifecycleOwner) {
            binding.titleEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description)

            it.dueDate?.let { date ->

                binding.datePicker.setDateTimeTextVisibility(
                    Utils.getStringOfDay(
                        date,
                        requireContext()
                    )
                )
                binding.datePicker.setDateTimeFieldVisibility(true)
                binding.timePicker.setDateTimeFieldVisibility(true)

                if (it.hasTime) {
                    val localDate = Utils.getLocalDateTimeFromEpoch(date)
                    setTime(localDate.hour, localDate.minute)
                    binding.timePicker.setClearIconVisibility(true)
                }
            }
        }

        viewModel.getTaskByUUID(args.taskId)

        viewModel.stateAction = { state ->
            editTaskState = state
            when (state) {
                EditTaskState.EditTaskStateSuccess -> {
                    navigationUtil.popBackStack()
                }
            }
        }

        return binding.root
    }

    private fun setTime(hour: Int?, minute: Int?) {
        if (hour == null || minute == null)
            return

        binding.timePicker.setDateTimeTextVisibility(Utils.getTimeFormat(hour, minute))
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