/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 *  and the Apache License v2.0 is available at https://www.apache.org/licenses/LICENSE-2.0.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.method;

import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class DeleteMethodProviderTest {

    @Test
    void shouldCreate() {
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        DeleteQuery query = DeleteMethodProvider.INSTANCE.apply(method, "Person");
        Assertions.assertNotNull(query);
        Assertions.assertEquals("Person", query.entity());
    }

    interface PersonRepository{
        void deleteByAge(Integer age);
    }
}