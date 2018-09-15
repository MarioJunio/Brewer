package com.mj.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class MessageElementTagProcessor extends AbstractElementTagProcessor {

	private static final String ELEMENT_NAME = "message";
	private static final int PRECEDENSE = 1000;

	public MessageElementTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, ELEMENT_NAME, true, null, false, PRECEDENSE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();

		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace", "layout/fragments/cadastro-mensagem-sucesso"));
		structureHandler.replaceWith(model, true);
	}

}
