package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            Map.entry("янв", 1),
            Map.entry("фев", 2),
            Map.entry("мар", 3),
            Map.entry("апр", 4),
            Map.entry("май", 5),
            Map.entry("июн", 6),
            Map.entry("июл", 7),
            Map.entry("авг", 8),
            Map.entry("сен", 9),
            Map.entry("окт", 10),
            Map.entry("ноя", 11),
            Map.entry("дек", 12)
    );

    @Override
    public LocalDateTime parse(String text) {
        var textParts = text.split(", ");
        return LocalDateTime.of(parseDate(textParts[0]), LocalTime.parse(textParts[1]));
    }

    private LocalDate parseDate(String textDate) {
        return switch (textDate) {
            case "сегодня" -> LocalDate.now();
            case "вчера" -> LocalDate.now().minusDays(1);
            default -> {
                var dateParts = textDate.split(" ");
                yield  LocalDate.of(
                        2000 + Integer.parseInt(dateParts[2]),
                        MONTHS.get(dateParts[1]),
                        Integer.parseInt(dateParts[0]));
            }
        };
    }
}
