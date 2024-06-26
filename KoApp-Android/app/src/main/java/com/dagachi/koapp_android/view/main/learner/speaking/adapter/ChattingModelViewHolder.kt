package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.R
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
                if (item.ttsCount == 2) {
                    dagachiTTS.textToSpeech(item.message)
                    binding.iBtnItemChattingModelSpeaker.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Gray_400))
                } else if (item.ttsCount < 2) {
                    dagachiTTS.textToSpeech(item.message)
                }
                item.ttsCount += 1
            }

            binding.iBtnItemChattingModelTranslate.setOnClickListener {
                if (item.isShowTranslation) {
                    item.isShowTranslation = false
                    binding.tvItemChattingModelBubble.text = item.message
                    binding.iBtnItemChattingModelTranslate.backgroundTintList = null
                } else {
                    item.isShowTranslation = true
                    binding.tvItemChattingModelBubble.text = item.translateMessage
                    binding.iBtnItemChattingModelTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Gray_400))
                }
            }

            binding.executePendingBindings()
        }
    }
}
