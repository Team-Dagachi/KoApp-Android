package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.databinding.ItemSpeakingSubjectBinding
import com.dagachi.koapp_android.view.main.learner.speaking.SpeakingSubject

/* 학습자의 말하기 연습 주제별 화면의 RV 어댑터 */
class SpeakingSubjectAdapter(private val subjectList: ArrayList<SpeakingSubject>): RecyclerView.Adapter<SpeakingSubjectListViewHolder>() {

    var onSubjectClickListener: ((Int) -> Unit)? = null // 주제 박스 클릭 이벤트

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpeakingSubjectListViewHolder {
        val binding: ItemSpeakingSubjectBinding = ItemSpeakingSubjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SpeakingSubjectListViewHolder(binding)
    }

    override fun getItemCount(): Int = subjectList.size

    override fun onBindViewHolder(holder: SpeakingSubjectListViewHolder, position: Int) {
        holder.bind(subjectList[position])

        // 주제 박스 클릭 이벤트
        holder.binding.cLayoutItemSpeakingSubject.setOnClickListener {
            onSubjectClickListener?.invoke(position)
        }
    }
}