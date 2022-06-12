package com.github.mazar1ni.tasktracker.tasks.presentation.tasks_list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.core.util.Utils
import com.github.mazar1ni.tasktracker.databinding.TaskGroupViewBinding
import com.github.mazar1ni.tasktracker.databinding.TaskViewBinding
import com.github.mazar1ni.tasktracker.tasks.domain.models.TaskDomainModel

class TaskListAdapter(private val context: Context) :
    BaseExpandableListAdapter() {

    private val tasksDictionaryList = mutableMapOf<String, MutableList<TaskDomainModel>>(
        context.getString(R.string.overdue) to mutableListOf(),
        context.getString(R.string.today) to mutableListOf(),
        context.getString(R.string.tomorrow) to mutableListOf(),
        context.getString(R.string.this_week) to mutableListOf(),
        context.getString(R.string.next_week) to mutableListOf(),
        context.getString(R.string.this_month) to mutableListOf(),
        context.getString(R.string.next_month) to mutableListOf(),
        context.getString(R.string.later) to mutableListOf(),
        context.getString(R.string.finished) to mutableListOf()
    )

    private val filteredTasksDictionaryList = mutableMapOf<String, MutableList<TaskDomainModel>>()

    var clickAction: ((Int) -> Unit)? = null
    var doneAction: ((Int, Boolean) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun add(tasks: List<TaskDomainModel>) {

        tasksDictionaryList.forEach {
            it.value.clear()
        }

        tasks.forEach { task ->
            if (task.isCompleted) {
                tasksDictionaryList[context.getString(R.string.finished)]?.add(task)
            } else if (task.dueDate == null) {
                tasksDictionaryList[context.getString(R.string.no_date)]?.add(task)
            } else {
                task.dueDate?.let {
                    tasksDictionaryList[Utils.getStringOfDayFoTaskList(it, context)]?.add(task)
                }
            }
        }

        tasksDictionaryList.forEach {
            if (it.value.isNotEmpty())
                filteredTasksDictionaryList[it.key] = it.value
        }

        notifyDataSetChanged()
    }

    override fun getGroupCount() = filteredTasksDictionaryList.keys.count()

    override fun getChildrenCount(groupId: Int): Int {
        filteredTasksDictionaryList.keys.forEachIndexed { index, key ->
            if (index == groupId)
                return filteredTasksDictionaryList[key]?.size ?: 0
        }
        return 0
    }

    override fun getGroup(groupId: Int): Any {
        filteredTasksDictionaryList.keys.forEachIndexed { index, key ->
            if (index == groupId)
                return key
        }
        return ""
    }

    override fun getChild(groupId: Int, childId: Int): Any {
        filteredTasksDictionaryList.keys.forEachIndexed { index, key ->
            if (index == groupId)
                return filteredTasksDictionaryList[key]?.get(childId)?.title ?: ""
        }
        return 0
    }

    override fun getGroupId(groupId: Int): Long {
        return groupId.toLong()
    }

    override fun getChildId(groupId: Int, childId: Int): Long {
        return childId.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupId: Int,
        isExpanded: Boolean,
        view: View?,
        viewGroup: ViewGroup?
    ): View {
        val binding = TaskGroupViewBinding.inflate(LayoutInflater.from(context))

        filteredTasksDictionaryList.keys.forEachIndexed { index, key ->
            if (index == groupId) {
                binding.taskGroupTitle.text = key
                if (key == context.getString(R.string.overdue))
                    binding.taskGroupTitle.setTextColor(context.getColor(R.color.red))
            }
        }

        return binding.root
    }

    override fun getChildView(
        groupId: Int,
        childId: Int,
        isExpanded: Boolean,
        view: View?,
        viewGroup: ViewGroup?
    ): View {

        val binding = TaskViewBinding.inflate(LayoutInflater.from(context))

        filteredTasksDictionaryList.keys.forEachIndexed { index, s ->
            if (index == groupId) {
                val task = filteredTasksDictionaryList[s]?.get(childId)
                task?.let {

                    binding.title.text = task.title
                    if (task.description.isEmpty())
                        binding.description.visibility = View.GONE
                    else {
                        binding.description.text = task.description
                        binding.description.visibility = View.VISIBLE
                    }

                    if (task.dueDate == null)
                        binding.date.visibility = View.GONE
                    else {
                        binding.date.text = task.dueDate?.let { date ->

                            var formattedDate = Utils.getDateFormat(date)

                            if (task.hasTime) {
                                val localDate = Utils.getLocalDateTimeFromEpoch(date)
                                formattedDate += ", ${
                                    Utils.getTimeFormat(
                                        localDate.hour,
                                        localDate.minute
                                    )
                                }"
                            }

                            if (date < System.currentTimeMillis())
                                binding.date.setTextColor(context.getColor(R.color.red))
                            else
                                binding.date.setTextColor(context.getColor(R.color.black))

                            formattedDate
                        }
                        binding.date.visibility = View.VISIBLE
                    }

                    binding.completeButton.setOnClickListener {
                        // TODO: add sound and animation
                        if (task.id == null) {
                            // TODO: show internal error
                        } else {
                            doneAction?.invoke(task.id, binding.completeButton.isChecked)
                        }
                    }
                    binding.taskLayout.setOnClickListener {
                        if (task.id == null) {
                            // TODO: show internal error
                        } else {
                            clickAction?.invoke(task.id)
                        }
                    }

                    binding.completeButton.isChecked = task.isCompleted
                    if (task.isCompleted) {
                        if (!binding.title.paint.isStrikeThruText)
                            binding.title.paintFlags =
                                binding.title.paintFlags + Paint.STRIKE_THRU_TEXT_FLAG
                        if (!binding.description.paint.isStrikeThruText)
                            binding.description.paintFlags =
                                binding.description.paintFlags + Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        if (binding.title.paint.isStrikeThruText)
                            binding.title.paintFlags =
                                binding.title.paintFlags - Paint.STRIKE_THRU_TEXT_FLAG
                        if (binding.description.paint.isStrikeThruText)
                            binding.description.paintFlags =
                                binding.description.paintFlags - Paint.STRIKE_THRU_TEXT_FLAG
                    }

                }
            }
        }

        return binding.root
    }

    override fun isChildSelectable(groupId: Int, childId: Int): Boolean {
        return true
    }
}