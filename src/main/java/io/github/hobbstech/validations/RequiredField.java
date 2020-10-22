package io.github.hobbstech.validations;

import lombok.Data;
import lombok.val;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
@Embeddable
public class RequiredField implements Serializable {

    @Column
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Column
    private String name;

    @Column
    private String displayName;

    @Column
    private boolean optional;

    @Column
    private String hint;

    public boolean isOptional() {
        return optional;
    }

    public Boolean getOptional() {
        return optional;
    }

    public static void validateRequiredFields(Set<RequiredFieldValue> targetFields, Set<RequiredField> requiredFields) {

        if (isNull(requiredFields) || requiredFields.isEmpty()) {
            return;
        }

        val nonOptionalRequireFieldsPresent = requiredFields.stream().anyMatch(field-> !field.optional);

        if (nonOptionalRequireFieldsPresent && (isNull(targetFields) || targetFields.isEmpty())) {
            throw new IllegalStateException("Required fields must be provided");
        }

        requiredFields.forEach(requiredField -> {

            if (!(requiredField.optional)) {
                val hasBeenSupplied = nonNull(
                        targetFields.stream()
                                .filter(requiredFieldValue -> requiredFieldValue.getName()
                                        .equalsIgnoreCase(requiredField.getName()))
                                .findFirst().orElse(null)
                );
                if (!(hasBeenSupplied))
                    throw new IllegalStateException(String.format("%s field should be supplied", requiredField.getName()));
            }

        });


    }


}
