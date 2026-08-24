/*
 *  Copyright (c) 2025 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.metadata;


/**
 * Represents metadata about an individual parameter of a constructor in a projection record.
 * <p>
 * Used in Jakarta Data to support query result mapping into record-based projections,
 * this interface exposes details about constructor parameters, including their type and
 * optionally an alias that maps to a selected field or column.
 * </p>
 *
 */
public interface ProjectionParameterMetadata {

    /**
     * Returns the name of the field that can be either the field name
     * or {@link jakarta.data.repository.Select#value()}
     *
     * @return the name
     */
    String name();

    /**
     * @return a {@code Class} object identifying the declared
     * type of the entity represented by this object
     */
    Class<?> type();


}
