package io.github.hobbstech.validations;

import lombok.Data;
import lombok.val;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;

import static java.util.stream.Collectors.toSet;

@Data
@Embeddable
public class RequiredFieldValue {

    @Enumerated(EnumType.STRING)
    @Column
    private FieldType fieldType;

    @Column
    private String name;

    @Column
    private String value;

    @Column
    private boolean optional;

    public static void validateRequireFieldValues(Collection<RequiredFieldValue> requiredFieldValues, Collection<RequiredField> requiredFields) {

        val mandatoryFields = requiredFields.stream()
                .filter(field -> !field.isOptional())
                .collect(toSet());

        if (mandatoryFields.isEmpty())
            return;

        if (requiredFieldValues.isEmpty()) {
            throw new IllegalStateException("Additional required file fields must be provided");
        }

        mandatoryFields.forEach(requiredField -> {
            val fieldPresent = requiredFieldValues.stream()
                    .anyMatch(requiredFieldValue -> requiredFieldValue.getName()
                            .equalsIgnoreCase(requiredField.getName()));

            if (!fieldPresent)
                throw new IllegalStateException(requiredField.getName() + " should be provided");
        });


    }

}
