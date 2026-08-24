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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.data.repository.DataRepository;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.semistructured.SemiStructuredTemplate;
import org.eclipse.jnosql.mapping.semistructured.query.RepositoryBean;

public class RepositoryDocumentBean<T extends DataRepository<T, ?>> extends RepositoryBean<T> {

    public RepositoryDocumentBean(Class<?> type, String provider) {
        super(type, provider, DatabaseType.DOCUMENT);
    }

    @Override
    protected Class<? extends SemiStructuredTemplate>  getTemplateClass() {
        return DocumentTemplate.class;
    }
}