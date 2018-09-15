package com.mj.brewer.thymeleaf.processor;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class MenuAttributeTag extends AbstractAttributeTagProcessor {

	private static final String ATTRIBUTE_NAME = "menu";
	private static final int PRECEDENCE = 1000;

	public MenuAttributeTag(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, ATTRIBUTE_NAME, true, PRECEDENCE, true);
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue,
			IElementTagStructureHandler structureHandler) {

		IEngineConfiguration configuration = context.getConfiguration();
		IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
		IStandardExpression expression = parser.parseExpression(context, attributeValue);
		
		// valor no atributo menu da tag
		String menu = (String) expression.execute(context);

		// URI atual
		String requestURI = ((IWebContext) context).getRequest().getRequestURI();
		
		// se a URI atual iniciar com o URI do menu selecionado, ative esse link
		if (requestURI.matches(menu)) {
			String classes = tag.getAttributeValue("class");
			structureHandler.setAttribute("class", classes + " is-active");
		}

	}

}
