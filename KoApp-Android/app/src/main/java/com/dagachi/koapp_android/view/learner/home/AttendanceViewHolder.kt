package com.dagachi.koapp_android.view.learner.home

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.ItemLearnerAttendanceBinding
import com.dagachi.koapp_android.domain.model.learner.home.AttendanceItem

/* 홈 화면의 주간 이번주 출석률 RV 뷰 홀더 */
class AttendanceViewHolder(val context: Context, val binding: ItemLearnerAttendanceBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AttendanceItem) {
        // 날짜 0 제거 및 공백 설정
        val tempDate: String =
            if (item.date[0].toString() == "0") {
                "  " + item.date[1].toString() + "  "
            } else {
                " " + item.date + " "
            }

        // 요일
        binding.tvItemLearnerAttendanceDay.text = item.day

        // 날짜
        binding.tvItemLearnerAttendanceDate.text = tempDate

        // 출석한 날짜라면
        if (item.isAttendance) {
            binding.tvItemLearnerAttendanceDate.visibility = View.GONE
            binding.ivItemLearnerAttendanceCheck.visibility = View.VISIBLE
        }
        // 안 출석한 날짜라면
        else {
            binding.tvItemLearnerAttendanceDate.visibility = View.VISIBLE
            binding.ivItemLearnerAttendanceCheck.visibility = View.GONE
        }

        // 오늘 날짜라면
        if (item.isToday) {
            binding.lLayoutItemLearnerAttendance.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.Main_70)
                )
            binding.tvItemLearnerAttendanceDay.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.White)))
            binding.ivItemLearnerAttendanceCheck.setImageResource(R.drawable.ic_check_fill_main20_32)
        }
    }
}
