package com.mj.brewer.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;

import com.mj.brewer.validation.validator.AtributoConfirmacaoValidator;

@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { AtributoConfirmacaoValidator.class })
public @interface AtributoConfirmacao {

	@OverridesAttribute(constraint = AtributoConfirmacao.class, name = "message")
	String message() default "Os atributos não conferem";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String atributo();

	String atributoConfirmacao();

}
