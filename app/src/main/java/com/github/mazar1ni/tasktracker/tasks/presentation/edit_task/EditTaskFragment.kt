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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
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

                setTime(timePicker.hour, timePicker.minute)
            }
            timePicker.show(parentFragmentManager, null)
        }

        binding.timeClearIcon.setOnClickListener {
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
                binding.dueDateEditText.setText(Utils.getStringOfDay(date, requireContext()))
                binding.dateClearIcon.visibility = View.VISIBLE
                binding.dueTimeField.visibility = View.VISIBLE
                binding.timeClearIcon.visibility = View.VISIBLE

                if (it.hasTime) {
                    val localDate = Utils.getLocalDateTimeFromEpoch(date)
                    setTime(localDate.hour, localDate.minute)
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

        binding.dueTimeEditText.setText(
            Utils.getTimeFormat(hour, minute)
        )
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