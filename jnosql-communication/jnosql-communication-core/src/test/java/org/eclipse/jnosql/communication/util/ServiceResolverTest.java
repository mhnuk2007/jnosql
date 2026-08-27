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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ServiceResolverTest {

    private final ClassLoader originalClassLoader =
            Thread.currentThread().getContextClassLoader();

    @AfterEach
    void restoreContextClassLoaderAndClearCache() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        ServiceResolver.clearCaches();
    }

    @Nested
    @DisplayName("When the loading is performed")
    class WhenTheLoading {

        @Test
        @DisplayName("Should return a provider when available through the context class loader")
        void shouldLoadUsingContextClassLoader() {
            // Given: the context class loader is explicitly set to the loader
            // that already provides TypeReferenceReader
            var service = TypeReferenceReader.class;
            var referenceClass = TypeReferenceReader.class;
            Thread.currentThread().setContextClassLoader(
                    TypeReferenceReader.class.getClassLoader());

            // When
            Optional<TypeReferenceReader> result =
                    ServiceResolver.loadFirst(service, referenceClass);

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
                    ServiceResolver.loadFirst(service, referenceClass);

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
                    ServiceResolver.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("Should return empty when no provider is found through either class loader")
        void shouldReturnEmptyWhenNoProviderExists() {
            // Given: a class loader that defines its own copy of Marker and
            // deliberately reports no META-INF/services resources, so
            // ServiceLoader cannot discover a provider through it
            var emptyClassLoader = new EmptyServicesClassLoader();
            Class<?> referenceClass = emptyClassLoader.loadMarker();
            Thread.currentThread().setContextClassLoader(emptyClassLoader);
            var service = TypeReferenceReader.class;

            // When
            Optional<TypeReferenceReader> result =
                    ServiceResolver.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty when the context and reference class loaders are equal")
        void shouldReturnEmptyWhenContextAndReferenceClassLoadersAreTheSame() {
            // Given: the same empty-services class loader instance is used
            // both as the thread's context class loader and as the loader
            // that defines the reference class, guaranteeing
            // referenceClassLoader.equals(contextClassLoader) by construction
            var emptyClassLoader = new EmptyServicesClassLoader();
            Class<?> referenceClass = emptyClassLoader.loadMarker();
            Thread.currentThread().setContextClassLoader(emptyClassLoader);
            var service = TypeReferenceReader.class;

            // When
            Optional<TypeReferenceReader> result =
                    ServiceResolver.loadFirst(service, referenceClass);

            // Then
            assertThat(result).isEmpty();
        }
    }

    /**
     * A class loader that defines its own copy of {@link Marker} directly
     * (rather than delegating to its parent), and reports no resources for
     * any {@code META-INF/services} provider-configuration file. Used to
     * deterministically force the "no provider found" outcome of
     * {@link ServiceResolver#loadFirst}, independent of whatever
     * providers happen to be visible through ambient JVM or module-path
     * class loaders in a given environment.
     */
    private static final class EmptyServicesClassLoader extends ClassLoader {

        EmptyServicesClassLoader() {
            super(null);
        }

        Class<?> loadMarker() {
            try {
                String path = Marker.class.getName().replace('.', '/') + ".class";
                byte[] bytes;
                try (var in = EmptyServicesClassLoader.class.getClassLoader()
                        .getResourceAsStream(path)) {
                    bytes = Objects.requireNonNull(in, "Marker class bytes not found")
                            .readAllBytes();
                }
                return defineClass(Marker.class.getName(), bytes, 0, bytes.length);
            } catch (java.io.IOException e) {
                throw new IllegalStateException("Unable to define Marker for test", e);
            }
        }

        @Override
        protected java.util.Enumeration<java.net.URL> findResources(String name) {
            return java.util.Collections.emptyEnumeration();
        }

        @Override
        protected java.net.URL findResource(String name) {
            return null;
        }
    }

    /**
     * A no-op class redefined by {@link EmptyServicesClassLoader} so that
     * its {@code getClassLoader()} reports that isolated loader rather than
     * this test module's own loader.
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
                    .isThrownBy(() -> ServiceResolver.loadFirst(service, referenceClass))
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
                    .isThrownBy(() -> ServiceResolver.loadFirst(service, referenceClass))
                    .withMessage("referenceClass is required");
        }

        @Test
        @DisplayName("Should reject a null service type for loadAll")
        void shouldRejectNullServiceForLoadAll() {
            // Given
            Class<TypeReferenceReader> service = null;
            var referenceClass = TypeReferenceReader.class;

            // When / Then
            assertThatNullPointerException()
                    .isThrownBy(() -> ServiceResolver.loadAll(service, referenceClass))
                    .withMessage("service is required");
        }

        @Test
        @DisplayName("Should reject a null reference class for loadAll")
        void shouldRejectNullReferenceClassForLoadAll() {
            // Given
            var service = TypeReferenceReader.class;
            Class<?> referenceClass = null;

            // When / Then
            assertThatNullPointerException()
                    .isThrownBy(() -> ServiceResolver.loadAll(service, referenceClass))
                    .withMessage("referenceClass is required");
        }
    }

    @Nested
    @DisplayName("When loading every provider")
    class WhenLoadingEveryProvider {

        @Test
        @DisplayName("Should return every provider found through the context class loader")
        void shouldLoadAllUsingContextClassLoader() {
            // Given
            var service = TypeReferenceReader.class;
            var referenceClass = TypeReferenceReader.class;
            Thread.currentThread().setContextClassLoader(
                    TypeReferenceReader.class.getClassLoader());

            // When
            var result = ServiceResolver.loadAll(service, referenceClass);

            // Then
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should return an empty list when no provider is found through either class loader")
        void shouldReturnEmptyListWhenNoProviderExists() {
            // Given
            var emptyClassLoader = new EmptyServicesClassLoader();
            Class<?> referenceClass = emptyClassLoader.loadMarker();
            Thread.currentThread().setContextClassLoader(emptyClassLoader);
            var service = TypeReferenceReader.class;

            // When
            var result = ServiceResolver.loadAll(service, referenceClass);

            // Then
            assertThat(result).isEmpty();
        }
    }

}