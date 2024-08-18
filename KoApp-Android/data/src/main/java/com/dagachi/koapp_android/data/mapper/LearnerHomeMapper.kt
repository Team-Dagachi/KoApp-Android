package com.dagachi.koapp_android.data.mapper

import com.dagachi.koapp_android.data.remote.model.learner.home.LearnerHomeResponse
import com.dagachi.koapp_android.domain.model.learner.home.AttendanceItem
import com.dagachi.koapp_android.domain.model.learner.home.LearnerHome
import com.dagachi.koapp_android.domain.model.learner.home.MissionItem
import com.dagachi.koapp_android.domain.utils.DateUtils
import java.time.format.DateTimeFormatter

/*
* 학습자의 홈 화면에서 사용하는 Mapper
* Data 모델 <-> Domain 모델 간의 데이터 변환을 담당
* */
object LearnerHomeMapper {
    // 요일 배열
    private val weeks = arrayListOf("일", "월", "화", "수", "목", "금", "토")

    // 홈 화면 조회(data -> domain)
    fun mapperToLearnerHome(learnerHomeResponse: LearnerHomeResponse): LearnerHome {
        // 미션 리스트
        val tempMissionList = ArrayList<MissionItem>()
        for (i in 0 until learnerHomeResponse.missionList.size) {
            val missionResponse = learnerHomeResponse.missionList[i]
            tempMissionList.add(
                MissionItem(
                    missionType = missionResponse.missionType,
                    missionName = missionResponse.missionName,
                    isCompletedMission = missionResponse.isCompletedMission,
                    missionDate = missionResponse.missionDate,
                    missionProgress = missionResponse.missionProgress
                )
            )
        }

        // 출석 여부 리스트
        val tempAttendanceList = ArrayList<AttendanceItem>()
        val weekStartDate = DateUtils.getWeekStartDate() // 일주일 시작 날짜

        for (i in 0 until learnerHomeResponse.attendanceList.size) {
            // 날짜
            val date = weekStartDate.plusDays(i.toLong())

            // 일주일 출석 아이템 추가
            tempAttendanceList.add(
                AttendanceItem(
                    day = weeks[i],
                    date = date.format(DateTimeFormatter.ofPattern("dd")),
                    isAttendance = learnerHomeResponse.attendanceList[i],
                    isToday = date.toLocalDate().isEqual(DateUtils.getTodayDate().toLocalDate())
                )
            )
        }

        return LearnerHome(
            userName = learnerHomeResponse.userName,
            todayWord = learnerHomeResponse.todayWord,
            attendanceList = tempAttendanceList,
            missionList = tempMissionList,
            programList = learnerHomeResponse.programList,
        )
    }
}