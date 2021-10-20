package ru.job4j.grabber.utils;

import java.io.IOException;
import java.util.Properties;

public class Config {
    public static Properties read(String fileName) throws IOException {
        try (var in = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)) {
            Properties config = new Properties();
            config.load(in);
            return config;
        } catch (IOException e) {
            throw new IOException("Файл %s с настройками не найден.".formatted(fileName));
        }
    }
}
