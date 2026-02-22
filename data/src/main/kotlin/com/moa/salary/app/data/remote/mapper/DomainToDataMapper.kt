package com.moa.salary.app.data.remote.mapper

import com.moa.salary.app.core.extensions.makeTimeString
import com.moa.salary.app.core.model.onboarding.Payroll
import com.moa.salary.app.core.model.onboarding.Term
import com.moa.salary.app.core.model.onboarding.WorkPolicy
import com.moa.salary.app.core.model.setting.NotificationSetting
import com.moa.salary.app.data.remote.model.request.AgreementRequest
import com.moa.salary.app.data.remote.model.request.AgreementsRequest
import com.moa.salary.app.data.remote.model.request.NotificationSettingRequest
import com.moa.salary.app.data.remote.model.request.PayrollRequest
import com.moa.salary.app.data.remote.model.request.WorkPolicyRequest
import kotlinx.collections.immutable.ImmutableList

fun Payroll.toData(): PayrollRequest = PayrollRequest(
    salaryInputType = salaryType.name,
    salaryAmount = salary.toLong(),
)

fun WorkPolicy.toData(): WorkPolicyRequest = WorkPolicyRequest(
    workdays = workScheduleDays.map { it.name },
    clockInTime = makeTimeString(
        time.startHour,
        time.startMinute
    ),
    clockOutTime = makeTimeString(
        time.endHour,
        time.endMinute
    ),
)

fun ImmutableList<Term>.toData(): AgreementsRequest = AgreementsRequest(
    agreements = this
        .filter { it !is Term.All }
        .map {
            AgreementRequest(
                code = it.code,
                agreed = it.checked,
            )
        }
)

fun NotificationSetting.Content.toData(): NotificationSettingRequest = NotificationSettingRequest(
    type = type,
    checked = checked,
)