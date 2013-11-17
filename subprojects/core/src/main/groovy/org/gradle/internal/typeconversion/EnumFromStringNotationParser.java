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

package org.gradle.internal.typeconversion;

import org.gradle.api.specs.Spec;
import org.gradle.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EnumFromStringNotationParser<T extends Enum<T>> extends TypedNotationParser<CharSequence, T> {
    private final Class<T> enumType;

    public EnumFromStringNotationParser(Class<T> enumType){
        super(CharSequence.class);
        this.enumType = enumType;
    }

    @Override
    protected T parseType(CharSequence notation) {
        final String enumString = notation.toString();
        List<T> enumConstants = Arrays.asList(enumType.getEnumConstants());
        T match = CollectionUtils.findFirst(enumConstants, new Spec<T>() {
            public boolean isSatisfiedBy(T enumValue) {
                return enumValue.name().equalsIgnoreCase(enumString);
            }
        });
        if (match == null) {
            throw new TypeConversionException(
                    String.format("Cannot coerce string value '%s' to an enum value of type '%s' (valid case insensitive values: %s)",
                            enumString, enumType.getName(), CollectionUtils.toStringList(Arrays.asList(enumType.getEnumConstants()))
                    )
            );
        } else {
            return match;
        }
    }

    public void describe(Collection<String> candidateFormats) {
        candidateFormats.add(String.format("Strings, valid case insensitive values: %s", CollectionUtils.toStringList(Arrays.asList(enumType.getEnumConstants()))));
    }
}