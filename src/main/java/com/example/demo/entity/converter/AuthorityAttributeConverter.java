package com.example.demo.entity.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;


@Converter
public class AuthorityAttributeConverter implements AttributeConverter<AuthorityType, String> {

    @Override
    public String convertToDatabaseColumn(AuthorityType authorityType) {
        return authorityType.name();
    }

    @Override
    public AuthorityType convertToEntityAttribute(String code) {
        return Arrays.stream(AuthorityType.values())
                .filter(variableType -> variableType.name().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s는 존재하지 않는 authority type 입니다.", code)));
    }
}
