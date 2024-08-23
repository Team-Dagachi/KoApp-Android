package com.dagachi.koapp_android.view.learner.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dagachi.koapp_android.databinding.ItemLearnerAttendanceBinding
import com.dagachi.koapp_android.domain.model.learner.home.AttendanceItem

/* 홈 화면의 주간 이번주 출석률 RV 어댑터 */
class AttendanceAdapter(val context: Context) : ListAdapter<AttendanceItem, AttendanceViewHolder>(AttendanceDiffCallback()) {
    private var attendanceList = ArrayList<AttendanceItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding: ItemLearnerAttendanceBinding = ItemLearnerAttendanceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AttendanceViewHolder(context, binding)
    }

    override fun getItemCount(): Int = attendanceList.size

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(attendanceList[position])
    }

    class AttendanceDiffCallback : DiffUtil.ItemCallback<AttendanceItem>() {
        override fun areItemsTheSame(oldItem: AttendanceItem, newItem: AttendanceItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: AttendanceItem, newItem: AttendanceItem): Boolean {
            return oldItem == newItem
        }
    }

    // 아이템 추가
    fun addAttendanceItem(item: AttendanceItem) {
        attendanceList.add(item)
        notifyItemInserted(attendanceList.size - 1)
    }
}