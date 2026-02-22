package com.moa.app.domain.usecase

import java.time.LocalTime
import javax.inject.Inject

/**
 * 근무 시간과 일급을 기준으로 누적 금액을 계산하는 UseCase
 *
 * 사용처:
 * - 근무전/근무중/근무후 화면에서 출퇴근 시간 변동 시 누적 금액 즉각 반영
 * - 위젯에서 실시간 누적 금액 표시
 */
class CalculateAccumulatedSalaryUseCase @Inject constructor() {

    /**
     * 현재 시간 기준 누적 금액을 계산합니다.
     *
     * @param startHour 출근 시간 (시)
     * @param startMinute 출근 시간 (분)
     * @param endHour 퇴근 시간 (시)
     * @param endMinute 퇴근 시간 (분)
     * @param dailyPay 일급
     * @param currentTime 현재 시간 (기본값: LocalTime.now())
     * @return 현재까지의 누적 금액
     */
    operator fun invoke(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
        currentTime: LocalTime = LocalTime.now(),
    ): Long {
        if (dailyPay <= 0) return 0L

        val startTimeSeconds = startHour * 3600 + startMinute * 60
        val endTimeSeconds = endHour * 3600 + endMinute * 60
        val currentTimeSeconds = currentTime.hour * 3600 + currentTime.minute * 60 + currentTime.second

        // 총 근무 시간 (초)
        val totalWorkSeconds = endTimeSeconds - startTimeSeconds
        if (totalWorkSeconds <= 0) return 0L

        // 현재까지 근무 시간 (초) - 출근 전이면 0, 퇴근 후면 전체 근무시간
        val workedSeconds = when {
            currentTimeSeconds < startTimeSeconds -> 0
            currentTimeSeconds > endTimeSeconds -> totalWorkSeconds
            else -> currentTimeSeconds - startTimeSeconds
        }

        // 초당 급여 계산
        val salaryPerSecond = dailyPay.toDouble() / totalWorkSeconds

        return (workedSeconds * salaryPerSecond).toLong()
    }

    /**
     * 초당 급여를 계산합니다.
     *
     * @param startHour 출근 시간 (시)
     * @param startMinute 출근 시간 (분)
     * @param endHour 퇴근 시간 (시)
     * @param endMinute 퇴근 시간 (분)
     * @param dailyPay 일급
     * @return 초당 급여
     */
    fun calculateSalaryPerSecond(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
    ): Double {
        if (dailyPay <= 0) return 0.0

        val startTimeSeconds = startHour * 3600 + startMinute * 60
        val endTimeSeconds = endHour * 3600 + endMinute * 60
        val totalWorkSeconds = endTimeSeconds - startTimeSeconds

        return if (totalWorkSeconds > 0) {
            dailyPay.toDouble() / totalWorkSeconds
        } else {
            0.0
        }
    }

    /**
     * 특정 근무 시간(초)에 대한 급여를 계산합니다.
     *
     * @param workedSeconds 근무한 시간 (초)
     * @param startHour 출근 시간 (시)
     * @param startMinute 출근 시간 (분)
     * @param endHour 퇴근 시간 (시)
     * @param endMinute 퇴근 시간 (분)
     * @param dailyPay 일급
     * @return 해당 근무 시간에 대한 급여
     */
    fun calculateSalaryForWorkedTime(
        workedSeconds: Int,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
    ): Long {
        val salaryPerSecond = calculateSalaryPerSecond(
            startHour, startMinute, endHour, endMinute, dailyPay
        )
        return (workedSeconds * salaryPerSecond).toLong()
    }

    /**
     * 월 누적금액을 계산합니다. (기존 누적월급 + 당일 누적일급)
     *
     * - 근무전: workedEarnings (아직 오늘 일 안 함)
     * - 근무중: workedEarnings + 현재까지 누적일급
     * - 퇴근후: workedEarnings + dailyPay (하루치 전부)
     *
     * @param workedEarnings API에서 받은 누적월급
     * @param startHour 출근 시간 (시)
     * @param startMinute 출근 시간 (분)
     * @param endHour 퇴근 시간 (시)
     * @param endMinute 퇴근 시간 (분)
     * @param dailyPay 일급
     * @param currentTime 현재 시간 (기본값: LocalTime.now())
     * @return 월 누적금액 (누적월급 + 당일 누적일급)
     */
    fun calculateMonthlyAccumulatedSalary(
        workedEarnings: Long,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        dailyPay: Long,
        currentTime: LocalTime = LocalTime.now(),
    ): Long {
        val todayEarnings = invoke(
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            dailyPay = dailyPay,
            currentTime = currentTime,
        )
        return workedEarnings + todayEarnings
    }
}