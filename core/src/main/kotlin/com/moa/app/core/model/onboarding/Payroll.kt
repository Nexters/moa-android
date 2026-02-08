package com.moa.app.core.model.onboarding

data class Payroll(
    val salaryType: SalaryType,
    val salary: String,
    val paydayDay: Int = 25,
) {
    enum class SalaryType(val title: String) {
        ANNUAL("연봉"),
        MONTHLY("월급")
    }
}
