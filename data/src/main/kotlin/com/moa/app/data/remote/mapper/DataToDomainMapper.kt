package com.moa.app.data.remote.mapper

import com.moa.app.core.extensions.toHourMinute
import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.model.response.PayrollResponse
import com.moa.app.data.remote.model.response.StatusResponse
import com.moa.app.data.remote.model.response.TermResponse
import com.moa.app.data.remote.model.response.WorkPolicyResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

fun List<TermResponse>.toDomain(): ImmutableList<Term> {
    val list = mutableListOf<Term>()
    list.add(Term.All(title = "전체 동의하기", checked = false))

    forEach { termResponse ->
        if (termResponse.required) {
            list.add(
                Term.Required(
                    title = "(필수) ${termResponse.title}",
                    url = termResponse.contentUrl,
                    checked = false,
                )
            )
        } else {
            list.add(
                Term.Optional(
                    title = "(선택) ${termResponse.title}",
                    url = termResponse.contentUrl,
                    checked = false,
                )
            )
        }
    }

    return list.sortedBy { it.order }.toImmutableList()
}