package com.ead.authuser.mappers.dates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {

  private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  @Named("formatLocalDateTime")
  public String format(LocalDateTime value) {
    return value == null ? null : value.format(FMT);
  }
}
