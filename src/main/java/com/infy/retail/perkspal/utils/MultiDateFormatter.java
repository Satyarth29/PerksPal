package com.infy.retail.perkspal.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;


public class MultiDateFormatter implements Converter<String, LocalDate> {

   private final List<DateTimeFormatter> formatters;

    public MultiDateFormatter(List<DateTimeFormatter> formatters) {
        this.formatters = formatters;
    }
    private static final Logger multiDateFormatterLogger = LoggerFactory.getLogger(MultiDateFormatter.class);
    /**
     * @param source takes source and converts into a Date data type by taking the custom formats.
     */
    @Override
    public LocalDate convert(String source) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                multiDateFormatterLogger.trace("multiDateFormatter Started with format: {} and source: {} ",formatter,source);
                return LocalDate.parse(source, formatter);
            } catch (DateTimeParseException e) {
                // Continue to the next formatter
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + source);
    }
}


