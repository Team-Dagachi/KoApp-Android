package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.ItemChattingUserBinding
import com.dagachi.koapp_android.view.main.learner.speaking.tts.DagachiTTS

/* Gemini 챗봇과 대화하는 User의 RV 뷰 홀더 */
class ChattingUserViewHolder(val context: Context, val binding: ItemChattingUserBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val dagachiTTS = DagachiTTS.getInstance(context)
    fun bind(item: ChatMessage) {
        if (item.role == ChatRole.USER) {
            binding.chatRoleModel = item
            binding.iBtnItemChattingUserSpeaker.setOnClickListener {
                if (item.ttsCount == 2) {
                    dagachiTTS.textToSpeech(item.message)
                    binding.iBtnItemChattingUserSpeaker.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_70))
                } else if (item.ttsCount < 2) {
                    dagachiTTS.textToSpeech(item.message)
                }
                item.ttsCount += 1
            }
            binding.executePendingBindings()
        }
    }
}