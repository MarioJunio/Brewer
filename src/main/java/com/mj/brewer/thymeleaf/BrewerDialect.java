package com.mj.brewer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.mj.brewer.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.mj.brewer.thymeleaf.processor.FormValidationElementTagProcessor;
import com.mj.brewer.thymeleaf.processor.MenuAttributeTag;
import com.mj.brewer.thymeleaf.processor.MessageElementTagProcessor;
import com.mj.brewer.thymeleaf.processor.OrderDataTagProcessor;
import com.mj.brewer.thymeleaf.processor.PaginationTagProcessor;

public class BrewerDialect extends AbstractProcessorDialect {

	public BrewerDialect() {
		super("Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {

		Set<IProcessor> processors = new HashSet<>();
		processors.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processors.add(new FormValidationElementTagProcessor(dialectPrefix));
		processors.add(new MessageElementTagProcessor(dialectPrefix));
		processors.add(new OrderDataTagProcessor(dialectPrefix));
		processors.add(new PaginationTagProcessor(dialectPrefix));
		processors.add(new MenuAttributeTag(dialectPrefix));

		return processors;
	}

}
