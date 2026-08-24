/*
 *  Copyright (c) 2026 Contributors to the Eclipse Foundation
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
 *   Mark Swatosh
 */
package org.eclipse.jnosql.mapping.reflection.repository;

import java.util.stream.Stream;

import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.nosql.Entity;


/*
* A Custom Repository which only defines the entity through a method, and the Entity
* is a valid jnosql entity.
*/
@Repository
public interface MethodEntityCustomRepository {

    @Entity
    class SomeEntity {
    }

    @Find
    Stream<SomeEntity> all();
}
