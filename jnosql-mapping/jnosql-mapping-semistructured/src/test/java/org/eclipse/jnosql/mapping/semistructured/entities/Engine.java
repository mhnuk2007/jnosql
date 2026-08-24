/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import jakarta.json.bind.annotation.JsonbVisibility;

import java.util.Objects;

@JsonbTypeInfo(
        key = "type",
        value = {
                @JsonbSubtype(alias = "gas", type = GasEngine.class),
                @JsonbSubtype(alias = "electric", type = ElectricEngine.class)
        }
)
@JsonbVisibility(FieldAccessStrategy.class)
public abstract class Engine {

    public Engine() {
    }

    public Engine(int horsepower) {
        this.horsepower = horsepower;
    }

    protected int horsepower;

    public int getHorsepower() {
        return horsepower;
    }

    public abstract String getFuelType();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Engine engine = (Engine) o;
        return horsepower == engine.horsepower &&
                getFuelType().equals(engine.getFuelType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(horsepower, getFuelType());
    }

    @Override
    public String toString() {
        return "Engine{" +
                "horsepower=" + horsepower +
                '}';
    }
}