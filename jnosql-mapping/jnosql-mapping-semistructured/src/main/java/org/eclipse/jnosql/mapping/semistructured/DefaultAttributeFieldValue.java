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
package org.eclipse.jnosql.mapping.semistructured;

import org.eclipse.jnosql.mapping.metadata.DefaultFieldValue;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldValue;

final class DefaultAttributeFieldValue extends AbstractAttributeFieldValue {

    private DefaultAttributeFieldValue(FieldValue fieldValue) {
        super(fieldValue);
    }

    @Override
    protected String name() {
        return field().name();
    }

    static AttributeFieldValue of(Object value, FieldMetadata field) {
        return new DefaultAttributeFieldValue(new DefaultFieldValue(value, field));
    }
}
