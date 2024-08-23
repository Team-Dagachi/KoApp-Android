package com.dagachi.koapp_android.view.learner.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.dagachi.koapp_android.R
import com.dagachi.koapp_android.databinding.FragmentLearnerHomeBinding
import com.dagachi.koapp_android.base.BaseFragment
import com.dagachi.koapp_android.domain.model.learner.home.AttendanceItem
import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome
import com.dagachi.koapp_android.domain.utils.DateUtils
import com.dagachi.koapp_android.viewmodel.learner.home.LearnerHomeViewModel
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

/* 학습자의 홈 화면 */
@AndroidEntryPoint
class LearnerHomeFragment: BaseFragment<FragmentLearnerHomeBinding>(FragmentLearnerHomeBinding::inflate) {
    private lateinit var callback: OnBackPressedCallback // 뒤로가기 콜백
    private val homeViewModel: LearnerHomeViewModel by viewModels() // 뷰모델

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

    override fun initCreateView() {
        // 뷰모델 연결
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initViewCreated() {
        // 바텀 네비게이션 띄우기
        mainActivity?.hideLearnerBottomNav(false)

        // RV 설정
        setupRecyclerViews()

        // 데이터 관찰 설정
        //setupObservers()

        // 데이터 로드
        //homeViewModel.getLearnerHome()

        // 이번주 출석률 아이템 중앙 정렬
        FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_AROUND // 축 기준 정렬 방향
        }.let {
            binding.rvLearnerHomeAttendance.layoutManager = it
            binding.rvLearnerHomeAttendance.adapter = attendanceAdapter
        }
    }

    override fun initAfterBinding() {
        // API 연결전 사용할 더미 데이터
        val weekDayList: Array<String> = resources.getStringArray(R.array.week_array)
        val weekStartDate = DateUtils.getWeekStartDate() // 일주일 시작 날짜
        for (i in 0 until 7) {
            val date = weekStartDate.plusDays(i.toLong()) // 날짜

            attendanceAdapter?.addAttendanceItem(
                AttendanceItem(
                    day = weekDayList[i],
                    date = date.format(DateTimeFormatter.ofPattern("dd")),
                    isAttendance = false,
                    isToday = date.toLocalDate().isEqual(DateUtils.getTodayDate().toLocalDate()),
                )
            )
        }
    }

    // RV 연결
    private fun setupRecyclerViews() {
        // 주간 출석률 어댑터
        attendanceAdapter = AttendanceAdapter(requireContext())
        binding.rvLearnerHomeAttendance.adapter = attendanceAdapter

        //binding.rvLearnerHomeMission.adapter = missionAdapter
        //binding.rvLearnerHomeProgram.adapter = programAdapter
    }

    // 데이터 받기
    private fun setupObservers() {
        homeViewModel.learnerHomeData.observe(viewLifecycleOwner) { learnerHome ->
            updateUI(learnerHome)
        }
    }

    // UI 업데이트
    @SuppressLint("SetTextI18n")
    private fun updateUI(learnerHome: LearnerHome) {
        // 이름
        binding.tvLearnerHomeName.text = "${learnerHome.userName}님,"

        // RecyclerView 어댑터 설정
        attendanceAdapter?.submitList(learnerHome.attendanceList)
    }
}