package com.lasrosas.iot.core.flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@ExtendWith(SpringExtension.class)
public class SpringExpressionTest {

	@Test
	public void testDownlinkExpression() {
		var imessage = MessageBuilder
				.withPayload("Hello World")
				.setHeader(LasRosasHeaders.THING_NATURAL_ID, "123b123b123b123b123")
				.build();

		ExpressionParser parser = new SpelExpressionParser();

		Expression exp = parser.parseExpression("'application/1/device/' + headers['ThingNaturalId'] + '/tx'"); 
		var result = exp.getValue(imessage, String.class);
		assertEquals("application/1/device/123b123b123b123b123/tx", result);
	}
}
