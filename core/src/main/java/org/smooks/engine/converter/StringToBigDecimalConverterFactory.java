/*-
 * ========================LICENSE_START=================================
 * Core
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.engine.converter;

import org.smooks.api.converter.TypeConverter;
import org.smooks.api.converter.TypeConverterDescriptor;
import org.smooks.api.converter.TypeConverterException;
import org.smooks.api.converter.TypeConverterFactory;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

/**
 * {@link BigDecimal} Decoder.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@Resource(name = "BigDecimal")
public class StringToBigDecimalConverterFactory implements TypeConverterFactory<String, BigDecimal> {

    @Override
    public TypeConverter<String, BigDecimal> createTypeConverter() {
        return new NumberTypeConverter<String, BigDecimal>() {
            @Override
            protected BigDecimal doConvert(String value) {
                if (numberFormat != null) {
                    try {
                        Number number = numberFormat.parse(value.trim());

                        if(number instanceof BigDecimal) {
                            return (BigDecimal) number;
                        } else if(number instanceof BigInteger) {
                            return new BigDecimal((BigInteger) number);
                        }

                        return BigDecimal.valueOf(number.doubleValue());
                    } catch (ParseException e) {
                        throw new TypeConverterException("Failed to decode BigDecimal value '" + value + "' using NumberFormat instance " + numberFormat + ".", e);
                    }
                } else {
                    try {
                        return new BigDecimal(value.trim());
                    } catch (NumberFormatException e) {
                        throw new TypeConverterException("Failed to decode BigDecimal value '" + value + "'.", e);
                    }
                }
            }
        };
    }

    @Override
    public TypeConverterDescriptor<Class<String>, Class<BigDecimal>> getTypeConverterDescriptor() {
        return new DefaultTypeConverterDescriptor<>(String.class, BigDecimal.class);
    }
}
