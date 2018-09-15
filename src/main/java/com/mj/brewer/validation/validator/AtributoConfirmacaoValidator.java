package com.mj.brewer.validation.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.beanutils.BeanUtils;

import com.mj.brewer.validation.AtributoConfirmacao;

public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object> {

	private String atributo;
	private String atributoConfirmacao;

	@Override
	public void initialize(AtributoConfirmacao atributoConfirmacao) {
		this.atributo = atributoConfirmacao.atributo();
		this.atributoConfirmacao = atributoConfirmacao.atributoConfirmacao();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		boolean valido = false;

		try {
			Object valorAtributo = BeanUtils.getProperty(value, atributo);
			Object valorAtributoConfirmacao = BeanUtils.getProperty(value, atributoConfirmacao);

			valido = isNull(valorAtributo, valorAtributoConfirmacao) || isEquals(valorAtributo, valorAtributoConfirmacao);

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}

		if (!valido) {
			context.disableDefaultConstraintViolation();
			
			String messageTemplate = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(messageTemplate);
			violationBuilder.addPropertyNode(atributoConfirmacao).addConstraintViolation();
		}

		return valido;
	}

	private boolean isEquals(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
	}

	private boolean isNull(Object valorAtributo, Object valorAtributoConfirmacao) {
		return valorAtributo == null && valorAtributoConfirmacao == null;
	}

}
