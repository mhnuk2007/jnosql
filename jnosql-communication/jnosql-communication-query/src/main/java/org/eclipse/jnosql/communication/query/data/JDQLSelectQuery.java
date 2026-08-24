/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 *  and the Apache License v2.0 is available at https://www.apache.org/licenses/LICENSE-2.0.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.data;

import jakarta.data.Sort;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.Where;

import java.util.List;
import java.util.Optional;

record JDQLSelectQuery(List<String> fields, String entity, List<Sort<?>> orderBy, Where condition, boolean count) implements SelectQuery {

    @Override
    public Optional<Where> where() {
        return Optional.ofNullable(condition);
    }

    @Override
    public long skip() {
        return 0;
    }

    @Override
    public long limit() {
        return 0;
    }

    @Override
    public boolean isCount() {
        return count;
    }
}
