/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.core.repository.returns;

import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.core.repository.RepositoryReturn;

import java.util.Optional;

/**
 * Handles repository methods with {@code void} return type.
 */
public class VoidRepositoryReturn implements RepositoryReturn {

    @Override
    public boolean isCompatible(Class<?> entity, Class<?> returnType) {
        return   Void.TYPE.equals(returnType) || Void.class.equals(returnType);
    }

    @Override
    public <T> Object convert(DynamicReturn<T> dynamic) {
        Optional<T> optional = dynamic.singleResult();
        return optional.orElse(null);
    }

    @Override
    public <T> Object convertPageRequest(DynamicReturn<T> dynamic) {
        Optional<T> optional = dynamic.singleResultPagination();
        return optional.orElse(null);
    }
}
