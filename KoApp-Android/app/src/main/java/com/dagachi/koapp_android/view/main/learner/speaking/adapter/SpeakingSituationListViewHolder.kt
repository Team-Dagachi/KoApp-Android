package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.databinding.ItemSpeakingSituationBinding
import com.dagachi.koapp_android.view.main.learner.speaking.SpeakingSituation

class SpeakingSituationListViewHolder(val binding: ItemSpeakingSituationBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SpeakingSituation) {
        binding.tvItemSpeakingSituation.text = item.situationTip

        if (!item.situationKor.isNullOrEmpty()) {
            binding.tvItemSpeakingSituationKor.visibility = View.VISIBLE
            binding.tvItemSpeakingSituationKor.text = item.situationKor
        }
    }
}