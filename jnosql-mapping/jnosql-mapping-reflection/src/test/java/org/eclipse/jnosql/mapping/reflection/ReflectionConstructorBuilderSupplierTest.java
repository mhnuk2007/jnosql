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
 */
package org.eclipse.jnosql.mapping.reflection;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

class ReflectionConstructorBuilderSupplierTest {

    @Mock
    private ConstructorMetadata constructorMetadata;

    private ReflectionConstructorBuilderSupplier supplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        supplier = new ReflectionConstructorBuilderSupplier();
    }

    @Test
    void shouldApply() {
        // Mocking ConstructorBuilder
        ConstructorBuilder constructorBuilder = DefaultConstructorBuilder.of(constructorMetadata);
        when(constructorBuilder.toString()).thenReturn("MockedConstructorBuilder");

        // Applying the supplier
        ConstructorBuilder result = supplier.apply(constructorMetadata);

        Assertions.assertThat(result).isNotNull();
    }
}