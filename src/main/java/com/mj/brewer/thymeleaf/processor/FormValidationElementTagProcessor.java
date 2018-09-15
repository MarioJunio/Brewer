package com.mj.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class FormValidationElementTagProcessor extends AbstractElementTagProcessor {

	private static final String ELEMENT_NAME = "validation";
	private static final int PRECENDE = 1000;

	public FormValidationElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, ELEMENT_NAME, true, null, false, PRECENDE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {

		IModelFactory factory = context.getModelFactory();
		IModel model = factory.createModel();

		model.add(factory.createStandaloneElementTag("th:block", "th:replace",
				"layout/fragments/cadastro-mensagens-validacao"));

		structureHandler.replaceWith(model, true);
	}

}
