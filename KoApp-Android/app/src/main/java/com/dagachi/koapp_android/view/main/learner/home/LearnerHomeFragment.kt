package com.dagachi.koapp_android.view.main.learner.home

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentLearnerHomeBinding
import com.dagachi.koapp_android.view.base.BaseFragment
import com.dagachi.koapp_android.viewmodel.main.learner.home.LearnerHomeViewModel
import com.dagachi.koapp_android.widget.utils.DateUtils
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/* 학습자의 홈 화면 */
class LearnerHomeFragment: BaseFragment<FragmentLearnerHomeBinding>(FragmentLearnerHomeBinding::inflate) {
    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백
    private val homeViewModel: LearnerHomeViewModel by viewModels()

    private var attendanceAdapter: AttendanceAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 핸드폰 뒤로가기 이벤트
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.getBackPressedEvent()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove() // 콜백 제거
    }

    override fun initViewCreated() {
        // 바텀 네비게이션 띄우기
        mainActivity?.hideLearnerBottomNav(false)

        // 이번주 출석률
        attendanceAdapter = AttendanceAdapter(requireContext())
        binding.rvLearnerHomeAttendance.adapter = attendanceAdapter

        // 이번주 출석률 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvLearnerHomeAttendance.layoutManager = it
            binding.rvLearnerHomeAttendance.adapter = attendanceAdapter
        }
    }

    override fun initAfterBinding() {
        // 이번주 출석률 설정
        setAttendanceDate()
    }

    // 날짜 설정 함수
    private fun setAttendanceDate() {
        // 날짜 포맷
        val fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateFormat = DateTimeFormatter.ofPattern("dd")

        // 오늘 날짜
        val today: ZonedDateTime = DateUtils.getTodayDate()
        val todayToString = today.format(fullDateFormat)

        // 일주일의 시작 날짜
        val startDate: LocalDateTime = DateUtils.getWeekStartDate()

        // 요일 배열
        val weekDayList: Array<String> = resources.getStringArray(R.array.week_array)

        for (i in 0..6) {
            var isToday = false
            val fullDate = startDate.plusDays(i.toLong()).format(fullDateFormat)
            val date = startDate.plusDays(i.toLong()).format(dateFormat)

            // 오늘 날짜라면 true
            if (todayToString == fullDate) {
                isToday = true
            }

            // 값 넣기
            attendanceAdapter?.addAttendanceItem(
                AttendanceItem(
                    day = weekDayList[i],
                    date = date,
                    fullDate = fullDate,
                    isAttendance = false,
                    isToday = isToday
                )
            )
        }
    }
}