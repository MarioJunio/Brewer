package com.mj.brewer.controller.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mj.brewer.model.Venda;

@Component
public class VendaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Venda.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Venda venda = (Venda) target;

		// valida se foi informado cliente
		if (venda.getCliente() == null || venda.getCliente().getId() == null)
			errors.rejectValue("cliente", "", "O cliente deve ser informado");

		// valida se foi informado pelo menos 1 item na venda
		if (venda.getItens().isEmpty())
			errors.reject("", "A venda deve conter no mínimo 1 item adicionado");

		// valida se a hora da entrega foi preenchida e a data da entrega
		if (venda.getDataDaEntrega() == null && venda.getHoraDaEntrega() != null)
			errors.rejectValue("dataDaEntrega", "", "A data da entrega deve ser informada");

		// valida se o total da venda não é negativo
		if (venda.getValorTotal().compareTo(BigDecimal.ZERO) < 0)
			errors.reject("", "O total da venda não pode ser negativo");
	}

}
