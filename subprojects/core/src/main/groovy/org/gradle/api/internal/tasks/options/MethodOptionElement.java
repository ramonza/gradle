/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.tasks.options;

import java.lang.reflect.Method;
import java.util.List;

public class MethodOptionElement extends AbstractOptionElement {

    private final Method method;
    private List<String> availableValues;
    private Class<?> optionType;

    public MethodOptionElement(Option option, Method method) {
        super(option.option(), option, method.getDeclaringClass());
        this.method = method;
        this.optionType = calculateOptionType();
        assertMethodTypeSupported(getOptionName(), method);
        assertValidOptionName();
    }

    private void assertValidOptionName() {
        if (getOptionName()== null || getOptionName().length() == 0) {
            throw new OptionValidationException(String.format("No option name set on '%s' in class '%s'.", getElementName(), getDeclaredClass().getName()));
        }
    }

    private Class<?> calculateOptionType() {
        if (method.getParameterTypes().length == 0) {
            //flag method
            return Void.TYPE;
        } else {
            return calculateOptionType(method.getParameterTypes()[0]);
        }
    }

    public Class<?> getDeclaredClass() {
        return method.getDeclaringClass();
    }

    public List<String> getAvailableValues() {
        //calculate list lazy to avoid overhead upfront
        if (availableValues == null) {
            availableValues = calculdateAvailableValues(optionType);
        }
        return availableValues;
    }

    public Class<?> getOptionType() {
        return optionType;
    }

    public String getElementName() {
        return method.getName();
    }

    public void apply(Object object, List<String> parameterValues) {
        if (parameterValues.size() == 0) {
            invokeMethod(object, method, true);
        } else if (parameterValues.size() > 1) {
            throw new IllegalArgumentException(String.format("Lists not supported for option."));
        } else {
            invokeMethod(object, method, getParameterObject(parameterValues.get(0)));
        }
    }

    private static void assertMethodTypeSupported(String optionName, Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 1) {
            throw new OptionValidationException(String.format("Option '%s' cannot be linked to methods with multiple parameters in class '%s#%s'.",
                    optionName, method.getDeclaringClass().getName(), method.getName()));
        }

        if (parameterTypes.length == 1) {
            final Class<?> parameterType = parameterTypes[0];
            if (!(parameterType == Boolean.class || parameterType == Boolean.TYPE)
                    && !parameterType.isAssignableFrom(String.class)
                    && !parameterType.isEnum()) {
                throw new OptionValidationException(String.format("Option '%s' cannot be casted to parameter type '%s' in class '%s'.",
                        optionName, parameterType.getName(), method.getDeclaringClass().getName()));
            }
        }
    }
}
