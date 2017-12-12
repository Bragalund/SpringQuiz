package no.group3.user.model.validation

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import kotlin.reflect.KClass

@NotNull
@Constraint(validatedBy = arrayOf(EmailValidator::class))
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.MustBeDocumented
annotation class CustomEmail(

        val groups: Array<KClass<*>> = arrayOf(),
        val payload: Array<KClass<out Payload>> = arrayOf()
)