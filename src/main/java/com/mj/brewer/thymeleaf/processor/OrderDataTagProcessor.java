package com.mj.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class OrderDataTagProcessor extends AbstractElementTagProcessor {

	private static final String ELEMENT_NAME = "order-data";
	private static final int PRECEDENCE = 1000;

	public OrderDataTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, ELEMENT_NAME, true, null, false, PRECEDENCE);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();

		IAttribute pageAttr = tag.getAttribute("page");
		IAttribute propertyAttr = tag.getAttribute("property");
		IAttribute textAttr = tag.getAttribute("text");

		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace",
				String.format("layout/fragments/data-order.html :: order(%s, %s, %s)", pageAttr.getValue(), propertyAttr.getValue(),
						textAttr.getValue())));
		structureHandler.replaceWith(model, true);
	}

}
