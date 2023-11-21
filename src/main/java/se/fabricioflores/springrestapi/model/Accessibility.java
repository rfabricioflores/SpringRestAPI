package se.fabricioflores.springrestapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Accessibility {
    // ** For storing purposes this enum is modified by its attribute converter
    PUBLIC,
    PRIVATE;

    // ** Jackson de/serialization
    @JsonValue
    public String getValue() {
        return this.toString().toLowerCase();
    }
}
