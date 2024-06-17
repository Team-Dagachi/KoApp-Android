package com.dagachi.koapp_android.view.main.learner.speaking.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dagachi.koapp_android.data.remote.model.ChatMessage
import com.dagachi.koapp_android.data.remote.model.ChatRole
import com.dagachi.koapp_android.databinding.ItemChattingHintBinding
import com.dagachi.koapp_android.databinding.ItemChattingModelBinding
import com.dagachi.koapp_android.databinding.ItemChattingUserBinding
import com.dagachi.koapp_android.widget.ApplicationClass.Companion.applicationContext

/* Gemini 챗봇 RV 어댑터 */
class ChattingAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val messages = mutableListOf<ChatMessage>()

    companion object {
        const val VIEW_TYPE_MODEL = 0 // 모델
        const val VIEW_TYPE_USER = 1 // 사용자
        const val VIEW_TYPE_HINT = 2 // 힌트
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
            VIEW_TYPE_HINT -> {
                ChattingHintViewHolder(applicationContext(), ItemChattingHintBinding.inflate(inflater, parent, false))
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
            VIEW_TYPE_HINT -> {
                val hintViewHolder = holder as ChattingHintViewHolder
                hintViewHolder.bind(message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].role) {
            ChatRole.MODEL -> VIEW_TYPE_MODEL
            ChatRole.USER -> VIEW_TYPE_USER
            ChatRole.HINT -> VIEW_TYPE_HINT
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

    // 힌트 상태 전환
    @SuppressLint("NotifyDataSetChanged")
    fun changeHintView(isShowHint: Boolean, message: ChatMessage?) {
        // 힌트가 보이는 상태라면
        if (isShowHint) {
            val hintMessage = messages.find { it.role == ChatRole.HINT }
            if (hintMessage != null) {
                messages.remove(hintMessage) // 리스트에서 삭제
            }
            notifyDataSetChanged()
        }
        // 힌트가 안 보이는 상태라면
        else {
            messages.add(message!!) // 리스트에 추가
            notifyDataSetChanged()
        }
    }

    // 리스트에 힌트가 있는지에 대한 여부 반환
    fun getHintInList(): Boolean {
        val hintMessage = messages.find { it.role == ChatRole.HINT }
        return hintMessage != null
    }
}