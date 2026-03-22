package com.moa.salary.app.data.remote.mapper

import com.moa.salary.app.core.extensions.convertMinutesToRoundedHours
import com.moa.salary.app.core.extensions.toHourMinute
import com.moa.salary.app.core.extensions.toHourMinuteOrNull
import com.moa.salary.app.core.extensions.toLocalDate
import com.moa.salary.app.core.model.calendar.Calendar
import com.moa.salary.app.core.model.calendar.CalendarStatus
import com.moa.salary.app.core.model.calendar.Event
import com.moa.salary.app.core.model.calendar.MonthlyInfo
import com.moa.salary.app.core.model.calendar.Schedule
import com.moa.salary.app.core.model.onboarding.OnboardingStatus
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.Profile
import com.moa.salary.app.core.model.onboarding.Term
import com.moa.salary.app.core.model.onboarding.Time
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.core.model.setting.SettingTerm
import com.moa.salary.app.core.model.work.Home
import com.moa.salary.app.core.model.work.MonthlyWorkSummary
import com.moa.salary.app.core.model.work.Workday
import com.moa.salary.app.core.model.work.WorkdayItem
import com.moa.salary.app.core.model.work.WorkdayType
import com.moa.salary.app.data.remote.model.response.CalendarResponse
import com.moa.salary.app.data.remote.model.response.EarningsResponse
import com.moa.salary.app.data.remote.model.response.HomeResponse
import com.moa.salary.app.data.remote.model.response.NotificationSettingResponse
import com.moa.salary.app.data.remote.model.response.PayrollResponse
import com.moa.salary.app.data.remote.model.response.ProfileResponse
import com.moa.salary.app.data.remote.model.response.ScheduleResponse
import com.moa.salary.app.data.remote.model.response.StatusResponse
import com.moa.salary.app.data.remote.model.response.TermResponse
import com.moa.salary.app.data.remote.model.response.WorkPolicyResponse
import com.moa.salary.app.data.remote.model.response.WorkdayItemResponse
import com.moa.salary.app.data.remote.model.response.WorkdayResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

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

fun HomeResponse.toDomain(): Home {
    val clockIn = clockInTime?.toHourMinuteOrNull()
    val clockOut = clockOutTime?.toHourMinuteOrNull()

    return Home(
        workplace = workplace,
        workedEarnings = workedEarnings,
        standardSalary = standardSalary,
        dailyPay = dailyPay,
        type = type.toWorkdayType(),
        startHour = clockIn?.first ?: 9,
        startMinute = clockIn?.second ?: 0,
        endHour = clockOut?.first ?: 18,
        endMinute = clockOut?.second ?: 0,
    )
}

fun WorkdayResponse.toDomain(): Workday {
    val clockIn = clockInTime?.toHourMinuteOrNull()
    val clockOut = clockOutTime?.toHourMinuteOrNull()

    return Workday(
        date = date,
        type = type.toWorkdayType(),
        dailyPay = dailyPay,
        startHour = clockIn?.first,
        startMinute = clockIn?.second,
        endHour = clockOut?.first,
        endMinute = clockOut?.second,
    )
}

fun WorkdayItemResponse.toDomain(): WorkdayItem = WorkdayItem(
    date = date,
    type = type.toWorkdayType(),
)

fun String.toWorkdayType(): WorkdayType = when (this) {
    "WORK" -> WorkdayType.WORK
    "VACATION" -> WorkdayType.VACATION
    else -> WorkdayType.NONE
}

fun EarningsResponse.toDomain(): MonthlyWorkSummary = MonthlyWorkSummary(
    workedMinutes = workedMinutes,
    standardMinutes = standardMinutes,
    workedEarnings = workedEarnings,
    standardSalary = standardSalary,
)

fun CalendarResponse.toDomain(): Calendar {
    return Calendar(
        monthlyInfo = earnings.toADomain(),
        schedules = schedules.associate { it.date.toLocalDate() to it.toDomain() }.toImmutableMap(),
        joinedAt = joinedAt.toLocalDate(),
    )
}

fun EarningsResponse.toADomain(): MonthlyInfo = MonthlyInfo(
    accumulatedWorkTime = workedMinutes.convertMinutesToRoundedHours().toString(),
    totalWorkTime = standardMinutes.convertMinutesToRoundedHours().toString(),
    accumulatedPay = (workedEarnings / 10000).toString(),
    totalPay = (standardSalary / 10000).toString(),
)

fun ScheduleResponse.toDomain(): Schedule = Schedule(
    type = type.toWorkdayType(),
    status = status.toCalendarStatus(),
    events = events.map { it.toEvent() }.toImmutableList(),
    dailyPay = dailyPay,
    workTime = "${clockInTime}~${clockOutTime}"
)

fun String.toCalendarStatus(): CalendarStatus = when (this) {
    "SCHEDULED" -> CalendarStatus.SCHEDULED
    "COMPLETED" -> CalendarStatus.COMPLETED
    else -> CalendarStatus.NONE
}

fun String.toEvent(): Event = when (this) {
    "PAYDAY" -> Event.PAYDAY
    else -> Event.NONE
}