/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License 2.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 *   and the Apache License v2.0 is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *   Maximillian Arruda
 */
package org.eclipse.jnosql.mapping.reflection;

import org.eclipse.jnosql.mapping.metadata.ArrayParameterMetaData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


interface DefaultArrayParameterMetaDataTest {

    ArrayParameterMetaData fieldMetadata();

    @Test
    default void shouldToString() {
        assertThat(fieldMetadata().toString()).isNotEmpty().isNotNull();
    }

    Class<?> expectedElementType();

    @Test
    default void shouldGetElementType() {
        assertThat(fieldMetadata().elementType()).isEqualTo(expectedElementType());
    }

}