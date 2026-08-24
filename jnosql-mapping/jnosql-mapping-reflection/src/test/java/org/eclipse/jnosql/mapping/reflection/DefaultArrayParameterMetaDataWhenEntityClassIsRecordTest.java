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
 *   Maximillian Arruda
 */
package org.eclipse.jnosql.mapping.reflection;

import org.eclipse.jnosql.mapping.metadata.ArrayParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.entities.constructor.Game;
import org.eclipse.jnosql.mapping.reflection.entities.constructor.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultArrayParameterMetaDataWhenEntityClassIsRecordTest implements DefaultArrayParameterMetaDataTest {

    @Override
    public ArrayParameterMetaData fieldMetadata() {
        ClassConverter converter = new ReflectionClassConverter();
        EntityMetadata entityMetadata = converter.apply(Player.class);
        var constructor = entityMetadata.constructor();
        return (ArrayParameterMetaData)
                constructor.parameters().stream().filter(p -> p.name().equals("games"))
                        .findFirst().orElseThrow();
    }

    @Override
    public Class<?> expectedElementType() {
        return Game.class;
    }

    @Test
    void shouldIsEmbeddable() {
        assertThat(fieldMetadata().isEmbeddable()).isTrue();
    }

}