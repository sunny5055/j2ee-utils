package com.googlecode.jutils.spring;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.googlecode.jutils.StringUtil;

/**
 * The Class ExpressionUtil.
 */
public class ExpressionUtil {

	/**
	 * The Constructor.
	 */
	private ExpressionUtil() {
		super();
	}

	/**
	 * Evaluate.
	 * 
	 * @param stringExpression
	 *            the string expression
	 * @return the object
	 */
	public static Object evaluate(String stringExpression) {
		return evaluate(null, stringExpression);
	}

	/**
	 * Evaluate.
	 * 
	 * @param object
	 *            the object
	 * @param stringExpression
	 *            the string expression
	 * @return the object
	 */
	public static Object evaluate(Object object, String stringExpression) {
		Object returnValue = null;
		if (!StringUtil.isBlank(stringExpression)) {
			final ExpressionParser parser = new SpelExpressionParser();
			final Expression expression = parser.parseExpression(stringExpression);

			if (object != null) {
				final StandardEvaluationContext context = new StandardEvaluationContext(object);
				returnValue = expression.getValue(context);
			} else {
				returnValue = expression.getValue();
			}

		}
		return returnValue;
	}
}