package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.ItemChattingModelBinding
import com.dagachi.koapp_android.view.main.learner.speaking.tts.DagachiTTS

/* Gemini 챗봇과 대화하는 Model의 RV 뷰 홀더 */
class ChattingModelViewHolder(val context: Context, val binding: ItemChattingModelBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val dagachiTTS = DagachiTTS.getInstance(context)
    fun bind(item: ChatMessage) {
        if (item.role == ChatRole.MODEL) {
            binding.chatRoleModel = item
            binding.iBtnItemChattingModelSpeaker.setOnClickListener {
                dagachiTTS.textToSpeech(item.message)
            }
            binding.executePendingBindings()
        }
    }
}