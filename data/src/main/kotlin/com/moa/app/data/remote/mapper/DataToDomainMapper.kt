package com.moa.app.data.remote.mapper

import com.moa.app.core.extensions.toHourMinute
import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.model.PayrollResponse
import com.moa.app.data.remote.model.StatusResponse
import com.moa.app.data.remote.model.WorkPolicyResponse
import kotlinx.collections.immutable.toImmutableList

fun StatusResponse.toDomain(): OnboardingStatus = OnboardingStatus(
    nickName = profile?.nickname,
    payroll = payroll?.toDomain(),
    workPolicy = workPolicy?.toDomain(),
    hasRequiredTermsAgreed = hasRequiredTermsAgreed,
)

fun PayrollResponse.toDomain(): Payroll = Payroll(
    salaryType = Payroll.SalaryType.valueOf(salaryInputType),
    salary = salaryAmount.toString(),
    paydayDay = paydayDay,
)

fun WorkPolicyResponse.toDomain(): WorkPolicy {
    val (startHour, startMinute) = clockInTime.toHourMinute()
    val (endHour, endMinute) = clockOutTime.toHourMinute()

    return WorkPolicy(
        workScheduleDays = workdays.map { WorkPolicy.WorkScheduleDay.valueOf(it) }
            .toImmutableList(),
        time = Time(
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
        )
    )
}