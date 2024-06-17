package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
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
        // 사용자 라면
        if (item.role == ChatRole.USER) {
            binding.chatRoleModel = item

            // 기본 말풍선의 스피커 버튼 클릭 이벤트
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

            // 기본 말풍선의 번역 버튼 클릭 이벤트
            binding.iBtnItemChattingUserTranslate.setOnClickListener {
                if (item.isShowTranslation) {
                    item.isShowTranslation = false
                    binding.tvItemChattingUserBubble.text = item.message
                    binding.iBtnItemChattingUserTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_30))
                } else {
                    item.isShowTranslation = true
                    binding.tvItemChattingUserBubble.text = item.translateMessage
                    binding.iBtnItemChattingUserTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Main_70))
                }
            }

            // 자연스러운 문장이라면
            if (item.feedbackMessage.isNullOrEmpty()) {
                binding.ivItemChattingUserStatus.setImageResource(R.drawable.ic_check_fill_32)
            }
            else if (!item.feedbackMessage.isNullOrEmpty()) {
                binding.ivItemChattingUserStatus.setImageResource(R.drawable.ic_feedback_28)
            }

            // 피드백 말풍선의 스피커 버튼 클릭 이벤트
            binding.iBtnItemChattingUserFeebackSpeaker.setOnClickListener {
                if (item.feedbackTTSCount == 2) {
                    dagachiTTS.textToSpeech(item.feedbackReason.toString())
                    binding.iBtnItemChattingUserFeebackSpeaker.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Orange_Dark))
                } else if (item.feedbackTTSCount < 2) {
                    dagachiTTS.textToSpeech(item.feedbackReason.toString())
                }
                item.feedbackTTSCount += 1
            }

            // 피드백 말풍선의 번역 버튼 클릭 이벤트
            binding.iBtnItemChattingUserFeedbackTranslate.setOnClickListener {
                if (item.isShowTranslation) {
                    item.isShowTranslation = false
                    binding.tvItemChattingUserFeedbackSolution.text = item.feedbackReason
                    binding.iBtnItemChattingUserFeedbackTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Orange_Light))
                    binding.iBtnItemChattingUserFeedbackTranslate.imageTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Gray_600))
                } else {
                    item.isShowTranslation = true
                    binding.tvItemChattingUserFeedbackSolution.text = item.translatedFeedbackReason
                    binding.iBtnItemChattingUserFeedbackTranslate.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Orange_Dark))
                    binding.iBtnItemChattingUserFeedbackTranslate.imageTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.White))
                }
            }

            binding.executePendingBindings()
        }
    }
}
