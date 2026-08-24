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

import jakarta.data.page.Page;

import org.eclipse.jnosql.mapping.DynamicQueryException;
import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;

public class PageRepositoryReturn extends AbstractRepositoryReturn {

    public PageRepositoryReturn() {
        super(Page.class);
    }

    @Override
    public <T> Object convert(DynamicReturn<T> dynamicReturn) {
        throw new DynamicQueryException("There is not pagination at the method: " + dynamicReturn.getMethod());
    }

    @Override
    public <T> Object convertPageRequest(DynamicReturn<T> dynamicReturn) {
        return dynamicReturn.getPage();
    }

    @Override
    public boolean isCompatible(Class<?> entity, Class<?> returnType) {
        return Page.class.equals(returnType);
    }
}
