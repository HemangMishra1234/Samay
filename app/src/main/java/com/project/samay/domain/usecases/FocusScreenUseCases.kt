package com.project.samay.domain.usecases

import android.content.Context
import com.project.samay.data.model.TaskEntity

class FocusScreenUseCases(
    private val taskScreenUseCases: TaskScreenUseCases
) {
    val allTasks = taskScreenUseCases.allTasks

    suspend fun useTime(
        context: Context,
        time: Int,
        taskEntity: TaskEntity,
        start: Long,
        end: Long,
        target: Long
    ){
        taskScreenUseCases.useTimeInTask(
            context,
            time,
            taskEntity,
            start,
            end,
            target
        )
    }
}