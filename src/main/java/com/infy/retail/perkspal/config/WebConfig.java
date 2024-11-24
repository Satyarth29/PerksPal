package com.infy.retail.perkspal.config;

import com.infy.retail.perkspal.utils.MultiDateFormatter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
private final Logger webConfigLogger =  LoggerFactory.getLogger(WebConfig.class);


    @Value("${date.formats}")
    private String dateFormats;

    private List<DateTimeFormatter> formatters;
    /**
     * Initializes the web configuration.
     * This method is called after the bean's properties have been set.
     * It sets up the date formatters based on the comma-separated list of date formats provided.
     * Date formats can be configured dynamically without code change
     */
    @PostConstruct
    public void init() {
        webConfigLogger.trace("Webconfig.init() starts");
        formatters = new ArrayList<>();
        for (String format : dateFormats.split(",")) {
            formatters.add(DateTimeFormatter.ofPattern(format.trim()));
        }
        webConfigLogger.trace("Webconfig.init() ends");
    }
    /**
     * Adds custom formatters to the FormatterRegistry.
     * This method registers a MultiDateFormatter, which can handle multiple date formats, to the registry.
     * @param registry the FormatterRegistry to which the formatters are added.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        webConfigLogger.trace("Webconfig.addFormatters() starts");
        registry.addConverter(new MultiDateFormatter(formatters));
        webConfigLogger.trace("Webconfig.addFormatters() ends");
    }
}

