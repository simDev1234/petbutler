package com.example.petbutler.persist.converter;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
  private static final String DELIM = ", ";

  @Override
  public String convertToDatabaseColumn(List<String> stringList) {
    return stringList != null ? String.join(DELIM, stringList) : "";
  }

  @Override
  public List<String> convertToEntityAttribute(String string) {
    return string != null ? Arrays.asList(string.split(DELIM)) : emptyList();
  }
}
