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
package org.eclipse.jnosql.mapping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class DynamicQueryExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Test error message";
        DynamicQueryException exception = new DynamicQueryException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testEmptyMessage() {
        String emptyMessage = "";
        DynamicQueryException exception = new DynamicQueryException(emptyMessage);
        assertEquals(emptyMessage, exception.getMessage());
    }
}
