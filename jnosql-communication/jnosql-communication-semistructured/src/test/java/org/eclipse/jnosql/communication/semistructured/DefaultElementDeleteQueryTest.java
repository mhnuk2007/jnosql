/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 * and the Apache License v2.0 is available at https://www.apache.org/licenses/LICENSE-2.0.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class DefaultElementDeleteQueryTest {


    @Nested
    @DisplayName("When the default element delete query is used")
    class WhenTheDefaultElementDeleteQueryIsUsed {

        private DeleteQuery query;

        @BeforeEach
        void setUp() {
            query = DeleteQuery.delete().from("columnFamily").build();
        }

        @DisplayName("Should Not Edit Columns")
        @Test
        void shouldNotEditColumns() {
            assertThatThrownBy(() -> {
                List<String> columns = query.columns();
                assertThat(columns).isEmpty();
                columns.clear();
            }).isInstanceOf(UnsupportedOperationException.class);
        }

        @DisplayName("Should Has Code")
        @Test
        void shouldHasCode(){
            var query = DeleteQuery.delete().from("columnFamily").build();
            var query1 = DeleteQuery.delete().from("columnFamily").build();
            assertThat(query.hashCode()).isEqualTo(query1.hashCode());
        }

        @DisplayName("Should Equals")
        @Test
        void shouldEquals(){
            var query = DeleteQuery.delete().from("columnFamily").build();
            var query1 = DeleteQuery.delete().from("columnFamily").build();
            assertThat(query).isEqualTo(query1);
            assertThat(query).isEqualTo(query);
            assertThat(query).isNotEqualTo("query");
        }
    }

}
