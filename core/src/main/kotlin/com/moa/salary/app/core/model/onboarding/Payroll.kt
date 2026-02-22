package com.moa.salary.app.core.model.onboarding

data class Payroll(
    val salaryType: SalaryType,
    val salary: String,
) {
    enum class SalaryType(val title: String) {
        ANNUAL("연봉"),
        MONTHLY("월급")
    }
}
