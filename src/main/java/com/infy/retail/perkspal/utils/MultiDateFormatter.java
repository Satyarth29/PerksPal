package com.infy.retail.perkspal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.converter.Converter;

@Slf4j
public class MultiDateFormatter implements Converter<String, LocalDate> {

   private final List<DateTimeFormatter> formatters;

    public MultiDateFormatter(List<DateTimeFormatter> formatters) {
        this.formatters = formatters;
    }
    /**
     * @param source takes source and converts into a Date data type by taking the custom formats.
     */
    @Override
    public LocalDate convert(String source) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                log.debug("multiDateFormatter Started with format: {} and source: {} ",formatter,source);
                return LocalDate.parse(source, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next formatter
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + source);
    }
}


