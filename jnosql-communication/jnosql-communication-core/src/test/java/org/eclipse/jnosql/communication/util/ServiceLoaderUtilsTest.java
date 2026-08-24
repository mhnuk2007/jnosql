/*
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ServiceLoaderUtilsTest {

    private final ClassLoader originalClassLoader =
            Thread.currentThread().getContextClassLoader();

    @AfterEach
    void restoreContextClassLoader() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
    }

    @Nested
    @DisplayName("When the loading is performed")
    class WhenTheLoading {

        @Test
        @DisplayName("Should return a provider when available through the context class loader")
        void shouldLoadUsingContextClassLoader() {
            // Given
            var service = TypeReferenceReader.class;
            var referenceClass = TypeReferenceReader.class;
            Thread.currentThread().setContextClassLoader(
                    TypeReferenceReader.class.getClassLoader());

            // When
            Optional<TypeReferenceReader> result =
                    ServiceLoaderUtils.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("Should fall back to the reference class loader when the context class loader cannot find a provider")
        void shouldFallBackToReferenceClassLoaderWhenContextCannotFindProvider() {
            // Given
            var service = TypeReferenceReader.class;
            var referenceClass = TypeReferenceReader.class;
            Thread.currentThread().setContextClassLoader(new ClassLoader(null) {
            });

            // When
            Optional<TypeReferenceReader> result =
                    ServiceLoaderUtils.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("Should fall back to the reference class loader when the context class loader is null")
        void shouldFallBackToReferenceClassLoaderWhenContextClassLoaderIsNull() {
            // Given
            var service = TypeReferenceReader.class;
            var referenceClass = TypeReferenceReader.class;
            Thread.currentThread().setContextClassLoader(null);

            // When
            Optional<TypeReferenceReader> result =
                    ServiceLoaderUtils.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("Should return empty when no provider is found through either class loader")
        void shouldReturnEmptyWhenNoProviderExists() {
            // Given
            var emptyClassLoader = new EmptyServicesClassLoader();
            Class<?> referenceClass = emptyClassLoader.loadMarker();
            Thread.currentThread().setContextClassLoader(emptyClassLoader);
            var service = TypeReferenceReader.class;

            // When
            Optional<TypeReferenceReader> result =
                    ServiceLoaderUtils.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty when the context and reference class loaders are equal")
        void shouldReturnEmptyWhenContextAndReferenceClassLoadersAreTheSame() {
            // Given
            var emptyClassLoader = new EmptyServicesClassLoader();
            Class<?> referenceClass = emptyClassLoader.loadMarker();
            Thread.currentThread().setContextClassLoader(emptyClassLoader);
            var service = TypeReferenceReader.class;

            // When
            Optional<TypeReferenceReader> result =
                    ServiceLoaderUtils.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isEmpty();
        }
    }

    /**
     * A class loader that defines its own copy of {@link Marker} directly
     * rather than delegating to its parent, and reports no resources for any
     * {@code META-INF/services} provider-configuration file. This allows the
     * "no provider found" outcome to be tested deterministically.
     */
    private static final class EmptyServicesClassLoader extends ClassLoader {

        EmptyServicesClassLoader() {
            super(null);
        }

        Class<?> loadMarker() {
            try {
                String path = Marker.class.getName().replace('.', '/') + ".class";

                try (var inputStream =
                             EmptyServicesClassLoader.class.getClassLoader()
                                     .getResourceAsStream(path)) {
                    byte[] bytes = Objects.requireNonNull(
                            inputStream,
                            "Marker class bytes not found"
                    ).readAllBytes();

                    return defineClass(
                            Marker.class.getName(),
                            bytes,
                            0,
                            bytes.length
                    );
                }
            } catch (java.io.IOException e) {
                throw new IllegalStateException(
                        "Unable to define Marker for test",
                        e
                );
            }
        }

        @Override
        protected Enumeration<URL> findResources(String name) {
            return Collections.emptyEnumeration();
        }

        @Override
        protected URL findResource(String name) {
            return null;
        }
    }

    /**
     * A no-op class redefined by {@link EmptyServicesClassLoader} so that its
     * {@code getClassLoader()} reports the isolated class loader rather than
     * the test module's class loader.
     */
    private static final class Marker {
    }

    @Nested
    @DisplayName("When the validation is performed")
    class WhenTheValidation {

        @Test
        @DisplayName("Should reject a null service type")
        void shouldRejectNullService() {
            // Given
            Class<TypeReferenceReader> service = null;
            var referenceClass = TypeReferenceReader.class;

            // When / Then
            assertThatNullPointerException()
                    .isThrownBy(() ->
                            ServiceLoaderUtils.loadFirst(service, referenceClass))
                    .withMessage("service is required");
        }

        @Test
        @DisplayName("Should reject a null reference class")
        void shouldRejectNullReferenceClass() {
            // Given
            var service = TypeReferenceReader.class;
            Class<?> referenceClass = null;

            // When / Then
            assertThatNullPointerException()
                    .isThrownBy(() ->
                            ServiceLoaderUtils.loadFirst(service, referenceClass))
                    .withMessage("referenceClass is required");
        }
    }
}