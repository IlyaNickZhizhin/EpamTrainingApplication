package org.epam.config.security;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskingPatternLayout extends PatternLayout {
    private Pattern multilinePattern;
    private List<String> maskPatterns = new ArrayList<>();

    public void addMaskPattern(String maskPattern) {
        maskPatterns.add(maskPattern);
        multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.MULTILINE);
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    private String maskMessage(String message) {
        if (multilinePattern == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    String masked = mask(matcher.group(group));
                    sb.replace(matcher.start(group), matcher.end(group), masked);
                }
            });
        }
        return sb.toString();
    }

    private String mask(String s) {
        if (s.length() <= 2) {
            return s;
        }
        return  s.charAt(0) +
                s.substring(2, s.length() - 1)
                        .replaceAll("\\.", "*") +
                s.charAt(s.length() - 1);
    }
}
