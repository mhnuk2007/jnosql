/*
 *
 *  Copyright (c) 2026 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License 2.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 *   and the Apache License v2.0 is available at
 *   https://www.apache.org/licenses/LICENSE-2.0.
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
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * A lazily initialized handle to service providers of a given type.
 *
 * <p>The thread context class loader (TCCL) is tried first. When it cannot
 * discover a provider, the class loader of the reference class is used as a
 * fallback. This supports environments where the context class loader and
 * the class loader that loaded the API classes are different, such as a
 * Quarkus/Vert.x event-loop thread whose context class loader does not see
 * the application's {@code META-INF/services} entries.</p>
 *
 * <p>An instance lazily selects and retains a {@link ServiceLoader}. The
 * selected loader is reused by subsequent calls to {@link #first()} and
 * {@link #all()}, allowing {@code ServiceLoader} to maintain its own provider
 * cache. This class does not maintain a global cache of providers or class
 * loaders; the lifecycle of a {@code ServiceDiscovery} instance belongs to
 * its caller.</p>
 *
 * <p>If a provider configuration is discoverable through the TCCL but that
 * provider later fails to load or instantiate, the resulting
 * {@link java.util.ServiceConfigurationError} is allowed to propagate. The
 * reference-class-loader fallback is intended to address class-loader
 * visibility, not to hide a broken provider that is already discoverable.</p>
 *
 * <p>This abstraction is not suitable for service lookups that must execute
 * from within a caller's own named JPMS module. Such call sites may need to
 * invoke {@link ServiceLoader} directly so that the caller module's
 * {@code uses} declaration is honored.</p>
 *
 * @param <T> the service type
 */
public final class ServiceDiscovery<T> {

    private final Class<T> service;
    private final Class<?> referenceClass;

    private ServiceLoader<T> serviceLoader;

    private ServiceDiscovery(
            Class<T> service,
            Class<?> referenceClass) {
        this.service = Objects.requireNonNull(service, "service is required");
        this.referenceClass = Objects.requireNonNull(
                referenceClass, "referenceClass is required");
    }

    /**
     * Creates a new service discovery structure.
     *
     * @param service the service type
     * @param referenceClass a class whose class loader is used as a fallback
     * @param <T> the service type
     * @return a new {@code ServiceDiscovery} instance
     * @throws NullPointerException if {@code service} or
     *         {@code referenceClass} is {@code null}
     */
    public static <T> ServiceDiscovery<T> of(
            Class<T> service,
            Class<?> referenceClass) {
        return new ServiceDiscovery<>(service, referenceClass);
    }

    /**
     * Returns the first available implementation of this discovery's service
     * type.
     *
     * <p>The underlying {@link ServiceLoader} is initialized lazily on the
     * first access and reused by subsequent calls.</p>
     *
     * @return the first available service implementation, or an empty
     *         optional when no implementation is available
     */
    public synchronized Optional<T> first() {
        return loader().findFirst();
    }

    /**
     * Returns every available implementation of this discovery's service
     * type.
     *
     * <p>The same lazily initialized {@link ServiceLoader} used by
     * {@link #first()} is used for this operation.</p>
     *
     * @return every available service implementation; empty when none are
     *         found
     */
    public synchronized List<T> all() {
        return loader()
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();
    }

    private ServiceLoader<T> loader() {
        if (serviceLoader == null) {
            serviceLoader = createLoader();
        }
        return serviceLoader;
    }

    private ServiceLoader<T> createLoader() {
        ClassLoader contextClassLoader =
                Thread.currentThread().getContextClassLoader();

        if (contextClassLoader != null) {
            ServiceLoader<T> contextLoader =
                    ServiceLoader.load(service, contextClassLoader);

            // Check provider metadata without instantiating the provider.
            // If the provider is discoverable but later fails to instantiate,
            // allow that ServiceConfigurationError to propagate rather than
            // silently falling back to another class loader.
            if (contextLoader.stream().findFirst().isPresent()) {
                return contextLoader;
            }
        }

        ClassLoader referenceClassLoader = referenceClass.getClassLoader();

        return ServiceLoader.load(service, referenceClassLoader);
    }
}
