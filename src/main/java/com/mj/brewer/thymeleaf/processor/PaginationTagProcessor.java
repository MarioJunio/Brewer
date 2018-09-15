package com.mj.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class PaginationTagProcessor extends AbstractElementTagProcessor {

	private static final String ELEMENT_NAME = "pagination";
	private static final int PRECEDENCE = 1000;

	public PaginationTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, ELEMENT_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {

		IModelFactory factory = context.getModelFactory();
		IModel model = factory.createModel();

		IAttribute paginaAttr = tag.getAttribute("pagina");

		model.add(factory.createStandaloneElementTag("th:block", "th:replace",
				String.format("layout/fragments/pagination :: pagination (%s)", paginaAttr.getValue())));
		
		structureHandler.replaceWith(model, true);
	}

}
