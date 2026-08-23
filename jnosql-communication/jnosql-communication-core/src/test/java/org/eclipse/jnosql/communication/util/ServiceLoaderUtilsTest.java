/*
 *
 *  Copyright (c) 2026 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 which accompanies this distribution is available at
 *   http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Mohan Lal
 *
 */
package org.eclipse.jnosql.communication.util;

import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceLoaderUtilsTest {

    private final ClassLoader originalClassLoader =
            Thread.currentThread().getContextClassLoader();

    @AfterEach
    void restoreContextClassLoader() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
    }

    @Test
    void shouldLoadServiceUsingContextClassLoader() {
        Optional<TypeReferenceReader> result =
                ServiceLoaderUtils.loadFirst(
                        TypeReferenceReader.class,
                        TypeReferenceReader.class);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldUseReferenceClassLoaderWhenContextClassLoaderDoesNotFindProvider() {
        Thread.currentThread().setContextClassLoader(
                new ClassLoader(null) {
                });

        Optional<TypeReferenceReader> result =
                ServiceLoaderUtils.loadFirst(
                        TypeReferenceReader.class,
                        TypeReferenceReader.class);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldLoadServiceWhenContextClassLoaderIsNull() {
        Thread.currentThread().setContextClassLoader(null);

        Optional<TypeReferenceReader> result =
                ServiceLoaderUtils.loadFirst(
                        TypeReferenceReader.class,
                        TypeReferenceReader.class);

        assertTrue(result.isPresent());
    }
}
