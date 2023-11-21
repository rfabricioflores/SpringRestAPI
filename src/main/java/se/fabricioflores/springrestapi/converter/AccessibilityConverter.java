package se.fabricioflores.springrestapi.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import se.fabricioflores.springrestapi.model.Accessibility;

@Converter(autoApply = true)
public class AccessibilityConverter implements AttributeConverter<Accessibility, String> {

    @Override
    public String convertToDatabaseColumn(Accessibility accessibility) {
        if (accessibility == null) return null;
        return accessibility.toString().toLowerCase();
    }

    @Override
    public Accessibility convertToEntityAttribute(String value) {
        if (value == null) return null;
        return Accessibility.valueOf(value.toUpperCase());
    }
}
