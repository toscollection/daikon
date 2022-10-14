package org.talend.dsel.functions.custom;

import org.talend.daikon.pattern.character.CharPatternToRegex;
import org.talend.dsel.exception.FunctionException;
import org.talend.maplang.el.interpreter.api.ExprLangContext;
import org.talend.maplang.el.interpreter.api.ExprLangFunction;

import java.util.Arrays;
import java.util.Objects;

public class Complies implements ExprLangFunction {

    @Override
    public String getName() {
        return "complies";
    }

    @Override
    public Object call(ExprLangContext exprLangContext, Object... params) {
        long numberOfNonNullParams = Arrays.stream(params).filter(Objects::nonNull).count();

        if (numberOfNonNullParams != 2) {
            throw new FunctionException(
                    String.format("Wrong number of parameters (expected: 2, found: %d)", numberOfNonNullParams));
        }

        String value = params[0].toString();
        String pattern = params[1].toString();

        return value != null && pattern != null && value.matches(CharPatternToRegex.toRegex(pattern));
    }
}