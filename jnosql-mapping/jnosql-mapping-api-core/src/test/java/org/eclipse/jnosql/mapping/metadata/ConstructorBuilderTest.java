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
package org.eclipse.jnosql.mapping.metadata;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ConstructorBuilderTest {

    @Test
    void shouldReturnValidConstructorBuilder() {
        ConstructorMetadata constructorMetadata = mock(ConstructorMetadata.class);
        ConstructorBuilder builder = ConstructorBuilder.of(constructorMetadata);
        SoftAssertions.assertSoftly(soft -> soft.assertThat(builder).isNotNull());
    }

}