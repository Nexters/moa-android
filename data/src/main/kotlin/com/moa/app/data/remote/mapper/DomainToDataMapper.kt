package com.moa.app.data.remote.mapper

import com.moa.app.core.extensions.makeTimeString
import com.moa.app.core.model.onboarding.Payroll
import com.moa.app.core.model.onboarding.Term
import com.moa.app.core.model.onboarding.WorkPolicy
import com.moa.app.data.remote.model.request.AgreementRequest
import com.moa.app.data.remote.model.request.AgreementsRequest
import com.moa.app.data.remote.model.request.PayrollRequest
import com.moa.app.data.remote.model.request.WorkPolicyRequest
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