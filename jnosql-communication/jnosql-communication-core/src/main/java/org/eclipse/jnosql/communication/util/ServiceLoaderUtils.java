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

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

public final class ServiceLoaderUtils {

    private ServiceLoaderUtils() {
    }

    /**
     * Loads the first available service provider using the thread context
     * class loader, falling back to the class loader of the reference class.
     *
     * <p>The thread context class loader is tried first, following the same
     * class-loader semantics as {@link ServiceLoader#load(Class)}. If no
     * provider is found, the class loader that loaded {@code referenceClass}
     * is used as a fallback.</p>
     *
     * @param service the service type
     * @param referenceClass the class whose class loader is used as a fallback
     * @param <T> the service type
     * @return the first available service provider, or an empty {@link Optional}
     *         if no provider is found
     * @throws NullPointerException if {@code service} or {@code referenceClass}
     *         is {@code null}
     */
    public static <T> Optional<T> loadFirst(
            Class<T> service,
            Class<?> referenceClass) {

        Objects.requireNonNull(service, "service is required");
        Objects.requireNonNull(referenceClass, "referenceClass is required");

        ClassLoader contextClassLoader =
                Thread.currentThread().getContextClassLoader();

        Optional<T> provider =
                ServiceLoader.load(service, contextClassLoader)
                        .findFirst();

        if (provider.isPresent()) {
            return provider;
        }

        ClassLoader referenceClassLoader =
                referenceClass.getClassLoader();

        if (!Objects.equals(referenceClassLoader, contextClassLoader)) {
            return ServiceLoader.load(service, referenceClassLoader)
                    .findFirst();
        }

        return Optional.empty();
    }
}
