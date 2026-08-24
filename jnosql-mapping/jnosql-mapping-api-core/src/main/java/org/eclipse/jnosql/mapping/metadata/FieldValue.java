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
package org.eclipse.jnosql.mapping.metadata;


/**
 * The tuple between the instance value and {@link FieldMetadata}
 */
public interface FieldValue {

    /**
     * Returns the instance
     *
     * @return the instance
     */
    Object value();

    /**
     * returns the {@link FieldMetadata}
     *
     * @return the {@link FieldMetadata} instance
     */
    FieldMetadata field();

    /**
     * Returns true if {@link FieldValue#value()} is different of null
     *
     * @return if {@link FieldValue#value()} is different of null
     */
    boolean isNotEmpty();

}
