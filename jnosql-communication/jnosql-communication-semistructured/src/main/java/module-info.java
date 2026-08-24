
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
/**
 * This module contains the communication layer to work with semi-structured databases.
 */
module org.eclipse.jnosql.communication.semistructured {
    uses org.eclipse.jnosql.communication.semistructured.DatabaseConfiguration;
    requires org.eclipse.jnosql.communication.core;
    requires org.eclipse.jnosql.communication.query;
    requires jakarta.json.bind;
    requires jakarta.json;
    requires jakarta.data;
    opens org.eclipse.jnosql.communication.semistructured;
}