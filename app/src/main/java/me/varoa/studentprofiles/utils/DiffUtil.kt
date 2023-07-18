package me.varoa.studentprofiles.utils

import androidx.recyclerview.widget.DiffUtil
import me.varoa.studentprofiles.core.domain.model.StudentMinified

val STUDENT_COMPARATOR = object : DiffUtil.ItemCallback<StudentMinified>() {
    override fun areItemsTheSame(
        oldItem: StudentMinified,
        newItem: StudentMinified,
    ): Boolean = oldItem.devName == newItem.devName

    override fun areContentsTheSame(
        oldItem: StudentMinified,
        newItem: StudentMinified,
    ): Boolean = oldItem == newItem
}

