package com.googlecode.jutils.csvexporter.util;

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
     * @param object the object
     * @param stringExpression the string expression
     * @return the object
     */
    public static Object evaluate(Object object, String stringExpression) {
        Object returnValue = null;
        if (object != null && !StringUtil.isBlank(stringExpression)) {
            final StandardEvaluationContext context = new StandardEvaluationContext();
            context.setRootObject(object);

            final ExpressionParser parser = new SpelExpressionParser();
            final Expression expression = parser.parseExpression(stringExpression);

            returnValue = expression.getValue(context);
        }
        return returnValue;
    }
}
