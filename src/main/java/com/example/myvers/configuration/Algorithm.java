package com.example.myvers.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Algorithm {
    public static String generateTimeSentence(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREA);
        return dateTime.format(formatter);
    }
}
