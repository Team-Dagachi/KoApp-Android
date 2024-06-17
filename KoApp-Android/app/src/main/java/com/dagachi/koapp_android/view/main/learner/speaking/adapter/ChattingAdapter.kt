package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.ItemChattingModelBinding
import com.dagachi.koapp_android.databinding.ItemChattingUserBinding
import com.dagachi.koapp_android.widget.ApplicationClass.Companion.applicationContext

/* Gemini 챗봇 RV 어댑터 */
class ChattingAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    companion object {
        const val VIEW_TYPE_MODEL = 0 // 모델
        const val VIEW_TYPE_USER = 1 // 사용자
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MODEL -> {
                ChattingModelViewHolder(applicationContext(), ItemChattingModelBinding.inflate(inflater, parent, false))
            }
            VIEW_TYPE_USER -> {
                ChattingUserViewHolder(applicationContext(), ItemChattingUserBinding.inflate(inflater, parent, false))
            }
            else -> throw IllegalArgumentException("Chatting Bot ViewHolder 생성 실패")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MODEL -> {
                val modelViewHolder = holder as ChattingModelViewHolder
                modelViewHolder.bind(message)
            }
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as ChattingUserViewHolder
                userViewHolder.bind(message)

                // 사용자의 말풍선을 클릭했을 경우
                userViewHolder.binding.tvItemChattingUserBubble.setOnClickListener {
                    // 피드백이 있는 경우
                    if (!message.feedbackMessage.isNullOrEmpty()) {
                        // 피드백 박스가 보인다면
                        if (userViewHolder.binding.cLayoutItemChattingUserFeedback.visibility == View.VISIBLE) {
                            userViewHolder.binding.cLayoutItemChattingUserFeedback.visibility = View.GONE
                            userViewHolder.binding.lLayoutItemChattingUserBtn.visibility = View.VISIBLE
                        }
                        // 피드백 박스가 안 보인다면
                        else {
                            userViewHolder.binding.cLayoutItemChattingUserFeedback.visibility = View.VISIBLE
                            userViewHolder.binding.lLayoutItemChattingUserBtn.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].role) {
            ChatRole.MODEL -> VIEW_TYPE_MODEL
            ChatRole.USER -> VIEW_TYPE_USER
            else -> -1
        }
    }

    override fun getItemCount(): Int = messages.size

    // 메시지 추가
    @SuppressLint("NotifyDataSetChanged")
    fun setMessage(message: ChatMessage?) {
        this.messages.apply {
            //clear()
            if (message != null) {
                // 메시지가 일치하는지 확인
                val messageItem: ChatMessage? = messages.findLast { it.message == message.message }
                if (messageItem != null) {
                    // 값 교체
                    messageItem.feedbackMessage = message.feedbackMessage
                    messageItem.feedbackReason = message.feedbackReason
                    messageItem.translatedFeedbackReason = message.translatedFeedbackReason
                }
                else {
                    add(message)
                }
            }
        }
        notifyDataSetChanged()
    }

    // 챗봇 초기화
    fun resetChatting() {
        notifyItemRangeRemoved(0, messages.size)
        messages.clear()
    }
}