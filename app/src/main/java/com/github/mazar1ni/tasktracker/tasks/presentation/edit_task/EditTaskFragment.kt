package com.github.mazar1ni.tasktracker.tasks.presentation.edit_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.databinding.FragmentEditTaskBinding
import com.github.mazar1ni.tasktracker.tasks.domain.states.EditTaskState
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

        binding.deleteTask.setOnClickListener {
            viewModel.deleteTask()
        }

        binding.cancelEdit.setOnClickListener {
            navigationUtil.popBackStack()
        }

        binding.updateTask.setOnClickListener {
            if (editTaskState == null || editTaskState !is EditTaskState.EditTaskStateSuccess)
                viewModel.saveEditTask()
        }

        viewModel.taskDomainModel.observe(viewLifecycleOwner) {
            binding.titleEditText.setText(it.title)
            binding.descriptionEditText.setText(it.description)

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
}