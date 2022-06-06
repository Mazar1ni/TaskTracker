package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.core.util.NavigationUtil
import com.github.mazar1ni.tasktracker.databinding.FragmentTasksBinding
import com.github.mazar1ni.tasktracker.tasks.domain.states.TasksState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by viewModels()

    @Inject
    lateinit var navigationUtil: NavigationUtil

    private val taskListAdapter by lazy { TaskListAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater)

        binding.tasksList.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = taskListAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            navigationUtil.navigate(TasksFragmentDirections.actionNavigationTasksToFragmentAddTask())
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.updateListTasks(true)
        }

        taskListAdapter.clickAction = { taskId ->
            navigationUtil.navigate(
                TasksFragmentDirections.actionNavigationTasksToFragmentEditTask(
                    taskId
                )
            )
        }

        taskListAdapter.doneAction = { taskId, isChecked ->
            viewModel.completeTask(taskId, isChecked)
        }

        viewModel.stateAction = { state ->
            when (state) {
                TasksState.TasksInProgress -> {
                    binding.refreshLayout.isRefreshing = true
                }
                TasksState.TasksListEmpty -> {
                    binding.refreshLayout.isRefreshing = false
                    taskListAdapter.add(listOf())
                    // TODO: show icon that no tasks today
                }
                TasksState.TasksNetworkConnectionError -> {
                    binding.refreshLayout.isRefreshing = false
                    view?.let {
                        Snackbar.make(it, R.string.no_network_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                TasksState.TasksInternalError -> {
                    binding.refreshLayout.isRefreshing = false
                    view?.let {
                        Snackbar.make(it, R.string.internal_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is TasksState.TasksSuccess -> {
                    binding.refreshLayout.isRefreshing = false
                    state.tasks?.let {
                        taskListAdapter.add(it.sortedBy { task -> task.isCompleted })
                    }
                }
                TasksState.TasksCompletedSuccess -> {
                    binding.refreshLayout.isRefreshing = false
                    viewModel.updateListTasks()
                }
            }
        }

        viewModel.updateListTasks()

        return binding.root
    }
}