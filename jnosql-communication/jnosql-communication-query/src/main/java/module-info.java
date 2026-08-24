/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at https://www.eclipse.org/legal/epl-2.0
 *  and the Apache License v2.0 is available at https://www.apache.org/licenses/LICENSE-2.0.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
module org.eclipse.jnosql.communication.query {
    requires org.antlr.antlr4.runtime;
    requires jakarta.json;
    requires org.eclipse.jnosql.communication.core;
    requires jakarta.data;
    requires java.management;
    requires java.logging;

    exports org.eclipse.jnosql.communication.query;
    exports org.eclipse.jnosql.communication.query.data;
    exports org.eclipse.jnosql.communication.query.method;

    opens org.eclipse.jnosql.communication.query;
    opens org.eclipse.jnosql.communication.query.data;
    opens org.eclipse.jnosql.communication.query.method;


}