package com.moa.app.data.remote.mapper

import com.moa.app.core.extensions.toHourMinute
import com.moa.app.core.model.onboarding.OnboardingStatus
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Profile
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.Time
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.core.model.setting.NotificationSetting
import com.moa.app.data.remote.model.response.NotificationSettingResponse
import com.moa.app.data.remote.model.response.PayrollResponse
import com.moa.app.data.remote.model.response.ProfileResponse
import com.moa.app.data.remote.model.response.StatusResponse
import com.moa.app.data.remote.model.response.TermResponse
import com.moa.app.data.remote.model.response.WorkPolicyResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun StatusResponse.toTermDomain(): OnboardingStatus = OnboardingStatus(
    profile = profile?.toTermDomain(),
    payroll = payroll?.toTermDomain(),
    workPolicy = workPolicy?.toTermDomain(),
    hasRequiredTermsAgreed = hasRequiredTermsAgreed,
)

fun ProfileResponse.toTermDomain(): Profile = Profile(
    nickname = nickname,
    companyName = workplace,
    paydayDay = paydayDay,
)

fun PayrollResponse.toTermDomain(): Payroll = Payroll(
    salaryType = Payroll.SalaryType.valueOf(salaryInputType),
    salary = salaryAmount.toString(),
)

fun WorkPolicyResponse.toTermDomain(): WorkPolicy {
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

fun List<TermResponse>.toTermDomain(): ImmutableList<Term> {
    val list = mutableListOf<Term>()
    list.add(Term.All(title = "전체 동의하기", checked = false))

    forEach { termResponse ->
        if (termResponse.required) {
            list.add(
                Term.Required(
                    code = termResponse.code,
                    title = "(필수) ${termResponse.title}",
                    url = termResponse.contentUrl,
                    checked = false,
                )
            )
        } else {
            list.add(
                Term.Optional(
                    code = termResponse.code,
                    title = "(선택) ${termResponse.title}",
                    url = termResponse.contentUrl,
                    checked = false,
                )
            )
        }
    }

    return list.sortedBy { it.order }.toImmutableList()
}

fun List<NotificationSettingResponse>.toNotificationSettingDomain(): ImmutableList<NotificationSetting> {
    val list = mutableListOf<NotificationSetting>()
    var category = ""

    forEach {
        if (it.category != category) {
            list.add(NotificationSetting.Title(it.category))
            category = it.category
        }
        list.add(
            NotificationSetting.Content(
                type = it.type,
                title = it.title,
                checked = it.checked,
            )
        )
    }

    return list.toImmutableList()
}