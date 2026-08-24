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


import jakarta.data.exceptions.MappingException;

/**
 * Exception when a class is not loaded to the cached way
 */
public class ClassInformationNotFoundException extends MappingException {


    /**
     * Creates the exception instance
     *
     * @param message the message in the exception
     */
    public ClassInformationNotFoundException(String message) {
        super(message);
    }
}