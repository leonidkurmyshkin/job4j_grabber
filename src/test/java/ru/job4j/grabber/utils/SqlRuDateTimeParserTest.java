package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {
    SqlRuDateTimeParser parser = new SqlRuDateTimeParser();

    @Test
    public void whenTodayWordInDateTime() {
        assertEquals(
                LocalDateTime.of(LocalDate.now(), LocalTime.parse("11:21")),
                parser.parse("сегодня, 11:21"));
    }

    @Test
    public void whenYesterdayWordInDateTime() {
        assertEquals(
                LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.parse("11:21")),
                parser.parse("вчера, 11:21"));
    }

    @Test
    public void whenJanuaryInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-01-19T11:57"),
                parser.parse("19 янв 21, 11:57"));
    }

    @Test
    public void whenFebruaryInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-02-01T18:04"),
                parser.parse("1 фев 21, 18:04"));
    }

    @Test
    public void whenMarchInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-03-04T13:58"),
                parser.parse("4 мар 21, 13:58"));
    }

    @Test
    public void whenAprilInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-04-30T12:53"),
                parser.parse("30 апр 21, 12:53"));
    }

    @Test
    public void whenMayInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-05-31T13:28"),
                parser.parse("31 май 21, 13:28"));
    }

    @Test
    public void whenJuneInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-06-08T13:41"),
                parser.parse("8 июн 21, 13:41"));
    }

    @Test
    public void whenJulyInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-07-29T09:25"),
                parser.parse("29 июл 21, 09:25"));
    }

    @Test
    public void whenAugustInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-08-09T10:14"),
                parser.parse("9 авг 21, 10:14"));
    }

    @Test
    public void whenSeptemberInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-09-10T13:41"),
                parser.parse("10 сен 21, 13:41"));
    }

    @Test
    public void whenOctoberInDateTime() {
        assertEquals(
                LocalDateTime.parse("2021-10-15T11:21"),
                parser.parse("15 окт 21, 11:21"));
    }

    @Test
    public void whenNovemberInDateTime() {
        assertEquals(
                LocalDateTime.parse("2020-11-23T17:50"),
                parser.parse("23 ноя 20, 17:50"));
    }

    @Test
    public void whenDecemberInDateTime() {
        assertEquals(
                LocalDateTime.parse("2020-12-15T10:05"),
                parser.parse("15 дек 20, 10:05"));
    }
}