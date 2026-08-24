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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {


    @Test
    void testIsBlankWithNull() {
        assertTrue(StringUtils.isBlank(null));
    }

    @Test
    void testIsBlankWithEmptyString() {
        assertTrue(StringUtils.isBlank(""));
    }

    @Test
    void testIsBlankWithSpaces() {
        assertTrue(StringUtils.isBlank("   "));
    }

    @Test
    void testIsBlankWithNonBlankString() {
        assertFalse(StringUtils.isBlank("Hello, world!"));
    }

    @Test
    void testIsNotBlankWithNull() {
        assertFalse(StringUtils.isNotBlank(null));
    }

    @Test
    void testIsNotBlankWithEmptyString() {
        assertFalse(StringUtils.isNotBlank(""));
    }

    @Test
    void testIsNotBlankWithSpaces() {
        assertFalse(StringUtils.isNotBlank("   "));
    }

    @Test
    void testIsNotBlankWithNonBlankString() {
        assertTrue(StringUtils.isNotBlank("Hello, world!"));
    }

}