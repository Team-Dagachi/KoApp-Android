package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.databinding.ItemSpeakingSituationBinding
import com.dagachi.koapp_android.view.main.learner.speaking.SpeakingSituation

/* 학습자의 말하기 연습 상황별 화면의 RV 어댑터 */
class SpeakingSituationAdapter(private val situationList: ArrayList<SpeakingSituation>): RecyclerView.Adapter<SpeakingSituationListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpeakingSituationListViewHolder {
        val binding: ItemSpeakingSituationBinding = ItemSpeakingSituationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SpeakingSituationListViewHolder(binding)
    }

    override fun getItemCount(): Int = situationList.size

    override fun onBindViewHolder(holder: SpeakingSituationListViewHolder, position: Int) {
        holder.bind(situationList[position])
    }
}