package com.dagachi.koapp_android.view.main.learner.speaking

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.ItemSpeakingSubjectBinding

/* 학습자의 말하기 연습 주제별 화면의 RV 어댑터 */
class SpeakingSubjectListViewHolder(val binding: ItemSpeakingSubjectBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SpeakingSubject) {
        if (item.isCheck) {
            binding.ivItemSpeakingSubjectCheck.setImageResource(R.drawable.ic_check_fill_32)
        }
        else {
            binding.ivItemSpeakingSubjectCheck.setImageResource(R.drawable.ic_check_fill_gray_28)
        }
        binding.tvItemSpeakingSubjectMain.text = item.subject

        if (!item.subjectKor.isNullOrEmpty()) {
            binding.tvItemSpeakingSubjectKor.visibility = View.VISIBLE
            binding.tvItemSpeakingSubjectKor.text = item.subjectKor
        }
    }
}