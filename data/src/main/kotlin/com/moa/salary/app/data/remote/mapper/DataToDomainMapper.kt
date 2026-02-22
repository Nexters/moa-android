package com.moa.salary.app.data.remote.mapper

import com.moa.salary.app.core.extensions.toHourMinute
import com.moa.salary.app.core.model.onboarding.OnboardingStatus
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.Profile
import com.moa.salary.app.core.model.onboarding.Term
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.core.model.setting.SettingTerm
import com.moa.salary.app.data.remote.model.response.NotificationSettingResponse
import com.moa.salary.app.data.remote.model.response.PayrollResponse
import com.moa.salary.app.data.remote.model.response.ProfileResponse
import com.moa.salary.app.data.remote.model.response.StatusResponse
import com.moa.salary.app.data.remote.model.response.TermResponse
import com.moa.salary.app.data.remote.model.response.WorkPolicyResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun StatusResponse.toDomain(): OnboardingStatus = OnboardingStatus(
    profile = profile?.toDomain(),
    payroll = payroll?.toDomain(),
    workPolicy = workPolicy?.toDomain(),
    hasRequiredTermsAgreed = hasRequiredTermsAgreed,
)

fun ProfileResponse.toDomain(): Profile = Profile(
    nickname = nickname,
    companyName = workplace,
    paydayDay = paydayDay,
)

fun PayrollResponse.toDomain(): Payroll = Payroll(
    salaryType = Payroll.SalaryType.valueOf(salaryInputType),
    salary = salaryAmount.toString(),
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

fun List<TermResponse>.toOnboardingTermDomain(): ImmutableList<Term> {
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

fun List<TermResponse>.toSettingTermDomain(): ImmutableList<SettingTerm> {
    return this.map {
        SettingTerm(
            title = it.title,
            url = it.contentUrl,
        )
    }.toImmutableList()
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