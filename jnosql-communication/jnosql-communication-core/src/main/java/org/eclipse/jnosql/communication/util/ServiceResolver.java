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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves {@link ServiceLoader}-discovered service implementations, trying
 * the thread context class loader first and falling back to the class
 * loader of a reference class when they differ. This supports environments
 * where the context class loader and the class loader that loaded the API
 * classes are different (for example, a Quarkus/Vert.x event-loop thread
 * whose context class loader does not see the application's
 * {@code META-INF/services} entries).
 *
 * <p>Results are cached per {@code (service, contextClassLoader,
 * referenceClassLoader)} combination, so the underlying {@link ServiceLoader}
 * scan runs at most once for a given combination rather than on every call.</p>
 *
 * <p><strong>Known limitation:</strong> the cache holds a strong reference to
 * every {@code ClassLoader} it has ever seen, for the lifetime of the JVM.
 * This is safe for class loaders that live for the lifetime of the
 * application (the common case), but is not yet safe for environments that
 * create and discard class loaders repeatedly at runtime — for example, a
 * hot-redeploy application server or an OSGi bundle that is installed and
 * uninstalled repeatedly. Supporting that scenario would require a bounded
 * or weak-referencing cache, which this class does not yet implement.</p>
 *
 * <p>Not yet migrated to this utility: any call site whose
 * {@code ServiceLoader.load(...)} call must execute from within the
 * caller's own module under the Java Platform Module System — for example,
 * a named module resolving a service owned by a sibling module it cannot
 * declare {@code uses} for without an upward dependency. Migrating such a
 * call site here produces a {@link java.util.ServiceConfigurationError} at
 * runtime, because the module system attributes the {@code ServiceLoader}
 * call to this class's own module, not the original caller's.</p>
 */
public final class ServiceResolver {

    private static final Map<CacheKey, Optional<?>> FIRST_CACHE =
            new ConcurrentHashMap<>();

    private static final Map<CacheKey, List<?>> ALL_CACHE =
            new ConcurrentHashMap<>();

    private ServiceResolver() {
    }

    /**
     * Loads the first available implementation of the given service.
     *
     * @param service the service type
     * @param referenceClass a class whose class loader is used as a fallback
     * @param <T> the service type
     * @return the first available service implementation, or an empty optional
     * @throws NullPointerException if {@code service} or {@code referenceClass}
     *         is {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> loadFirst(
            Class<T> service,
            Class<?> referenceClass) {

        Objects.requireNonNull(service, "service is required");
        Objects.requireNonNull(referenceClass, "referenceClass is required");

        CacheKey key = CacheKey.of(service, referenceClass);
        return (Optional<T>) FIRST_CACHE.computeIfAbsent(
                key, ServiceResolver::resolveFirst);
    }

    /**
     * Loads every available implementation of the given service.
     *
     * <p>Providers are returned from the thread context class loader if any
     * are found there; otherwise, providers from the reference class's
     * class loader are returned. The two sets of providers are not merged,
     * consistent with {@link #loadFirst}'s fallback semantics.</p>
     *
     * @param service the service type
     * @param referenceClass a class whose class loader is used as a fallback
     * @param <T> the service type
     * @return every available service implementation found through the
     *         first class loader that discovers any; empty if neither does
     * @throws NullPointerException if {@code service} or {@code referenceClass}
     *         is {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadAll(
            Class<T> service,
            Class<?> referenceClass) {

        Objects.requireNonNull(service, "service is required");
        Objects.requireNonNull(referenceClass, "referenceClass is required");

        CacheKey key = CacheKey.of(service, referenceClass);
        return (List<T>) ALL_CACHE.computeIfAbsent(
                key, ServiceResolver::resolveAll);
    }

    /**
     * Clears all cached resolution results. Intended for test use, to keep
     * cache state isolated between test cases that reuse the same
     * {@code (service, referenceClass)} pair with different thread context
     * class loaders.
     */
    static void clearCaches() {
        FIRST_CACHE.clear();
        ALL_CACHE.clear();
    }

    private static <T> Optional<T> resolveFirst(CacheKey key) {
        @SuppressWarnings("unchecked")
        Class<T> service = (Class<T>) key.service;

        Optional<T> provider =
                ServiceLoader.load(service, key.contextClassLoader)
                        .findFirst();

        if (provider.isPresent()) {
            return provider;
        }

        if (!Objects.equals(
                key.referenceClassLoader, key.contextClassLoader)) {
            return ServiceLoader.load(service, key.referenceClassLoader)
                    .findFirst();
        }

        return Optional.empty();
    }

    private static <T> List<T> resolveAll(CacheKey key) {
        @SuppressWarnings("unchecked")
        Class<T> service = (Class<T>) key.service;

        List<T> viaContext = ServiceLoader.load(service, key.contextClassLoader)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        if (!viaContext.isEmpty()) {
            return viaContext;
        }

        if (!Objects.equals(
                key.referenceClassLoader, key.contextClassLoader)) {
            return ServiceLoader.load(service, key.referenceClassLoader)
                    .stream()
                    .map(ServiceLoader.Provider::get)
                    .toList();
        }

        return List.of();
    }

    /**
     * Cache key combining the requested service type with both class
     * loaders involved in resolution, so that the same service requested
     * through different class loaders is cached independently rather than
     * sharing a stale result.
     */
    private static final class CacheKey {

        private final Class<?> service;
        private final ClassLoader contextClassLoader;
        private final ClassLoader referenceClassLoader;

        private CacheKey(Class<?> service,
                         ClassLoader contextClassLoader,
                         ClassLoader referenceClassLoader) {
            this.service = service;
            this.contextClassLoader = contextClassLoader;
            this.referenceClassLoader = referenceClassLoader;
        }

        static CacheKey of(Class<?> service, Class<?> referenceClass) {
            return new CacheKey(
                    service,
                    Thread.currentThread().getContextClassLoader(),
                    referenceClass.getClassLoader());
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CacheKey that)) {
                return false;
            }
            return service.equals(that.service)
                    && Objects.equals(
                    contextClassLoader, that.contextClassLoader)
                    && Objects.equals(
                    referenceClassLoader, that.referenceClassLoader);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    service, contextClassLoader, referenceClassLoader);
        }
    }
}
