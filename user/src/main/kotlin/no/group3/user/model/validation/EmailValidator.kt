package no.group3.user.model.validation


import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class EmailValidator : ConstraintValidator<CustomEmail, String> {
    override fun isValid(p0: String, p1: ConstraintValidatorContext?): Boolean {
        return EmailChecker.isValidEmail(p0)
    }

    override fun initialize(p0: CustomEmail?) {}
}