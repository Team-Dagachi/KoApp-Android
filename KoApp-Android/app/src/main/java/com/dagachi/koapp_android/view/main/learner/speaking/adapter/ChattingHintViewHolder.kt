package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.ItemChattingHintBinding
import com.dagachi.koapp_android.view.main.learner.speaking.tts.DagachiTTS

/* Gemini 챗봇의 힌트의 RV 뷰 홀더 */
class ChattingHintViewHolder(val context: Context, val binding: ItemChattingHintBinding):
    RecyclerView.ViewHolder(binding.root) {

    private val dagachiTTS = DagachiTTS.getInstance(context)

    fun bind(item: ChatMessage) {
        if (item.role == ChatRole.HINT) {
            binding.chatRoleModel = item

            // 스피커 버튼 클릭 이벤트
            binding.iBtnItemChattingHintSpeaker.setOnClickListener {
                if (item.ttsCount == 2) {
                    dagachiTTS.textToSpeech(item.translatedHint.toString())
                    binding.iBtnItemChattingHintSpeaker.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_70))
                } else if (item.ttsCount < 2) {
                    dagachiTTS.textToSpeech(item.translatedHint.toString())
                }
                item.hintTTSCount += 1
            }

            // 번역 버튼 클릭 이벤트
            binding.iBtnItemChattingHintTranslate.setOnClickListener {
                if (item.isShowTranslation) {
                    item.isShowTranslation = false
                    binding.tvItemChattingHintBubble.text = item.hintMessage
                    binding.iBtnItemChattingHintTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_30))
                } else {
                    item.isShowTranslation = true
                    binding.tvItemChattingHintBubble.text = item.translatedHint
                    binding.iBtnItemChattingHintTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_70))
                }
            }

            binding.executePendingBindings()
        }
    }
}