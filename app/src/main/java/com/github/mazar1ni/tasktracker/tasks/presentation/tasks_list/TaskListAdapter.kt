package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.databinding.TaskViewBinding
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

class TaskListAdapter(private val context: Context) :
    RecyclerView.Adapter<TaskListAdapter.CustomViewHolder>() {

    private val tasksList = mutableListOf<TaskDomainModel>()

    var clickAction: ((Int) -> Unit)? = null
    var doneAction: ((Int, Boolean) -> Unit)? = null

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
                holder.binding.completeButton.setOnClickListener {
                    // TODO: add sound and animation
                    if (task.id == null) {
                        // TODO: show internal error
                    } else {
                        doneAction?.invoke(task.id, holder.binding.completeButton.isChecked)
                    }
                }
                holder.binding.taskLayout.setOnClickListener {
                    if (task.id == null) {
                        // TODO: show internal error
                    } else {
                        clickAction?.invoke(task.id)
                    }
                }

                holder.binding.completeButton.isChecked = task.isCompleted
                if (task.isCompleted) {
                    if (!holder.binding.title.paint.isStrikeThruText)
                        holder.binding.title.paintFlags =
                            holder.binding.title.paintFlags + Paint.STRIKE_THRU_TEXT_FLAG
                    if (!holder.binding.description.paint.isStrikeThruText)
                        holder.binding.description.paintFlags =
                            holder.binding.description.paintFlags + Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    if (holder.binding.title.paint.isStrikeThruText)
                        holder.binding.title.paintFlags =
                            holder.binding.title.paintFlags - Paint.STRIKE_THRU_TEXT_FLAG
                    if (holder.binding.description.paint.isStrikeThruText)
                        holder.binding.description.paintFlags =
                            holder.binding.description.paintFlags - Paint.STRIKE_THRU_TEXT_FLAG
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