package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mazar1ni.tasktracker.databinding.TaskViewBinding
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

class TaskListAdapter() :
    RecyclerView.Adapter<TaskListAdapter.CustomViewHolder>() {

    private val tasksList = mutableListOf<TaskDomainModel>()

    var clickAction: ((Int) -> Unit)? = null

    inner class CustomViewHolder(val binding: TaskViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val binding = TaskViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.apply {
            tasksList[position].let { task ->
                holder.binding.title.text = task.title
                holder.binding.description.text = task.description
                holder.binding.radioButton1.setOnClickListener {
                    // TODO: add sound and animation, also add cancel toast
                    tasksList.remove(task)
                    notifyItemChanged(position)
                }
                holder.binding.taskLayout.setOnClickListener {
                    if (task.id == null) {
                        // TODO: show internal error
                    } else {
                        clickAction?.invoke(task.id)
                    }
                }
            }
        }
    }

    override fun getItemCount() = tasksList.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(tasks: List<TaskDomainModel>) {
        tasksList.clear()
        tasksList.addAll(tasks)
        notifyDataSetChanged()
    }

}