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
}