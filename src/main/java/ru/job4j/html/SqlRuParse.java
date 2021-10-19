package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.function.Predicate;

public class SqlRuParse {
    public static void printVacancy(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Element vacancy = doc.selectFirst(".msgTable");
        String title = vacancy.selectFirst(".messageHeader").ownText();
        String description = vacancy.select(".msgBody").get(1).text();
        String created = vacancy.selectFirst(".msgFooter").ownText()
                .split("\s+\\[", 2)[0];
        System.out.printf("%s%n%s%n%s%n%s%n%n", title, description, url, created);
    }

    public static void printSitePage(String url, Predicate<String> skip) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            if (skip.test(td.ownText())) {
                printVacancy(td.child(0).attr("href"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        var startUrl = "https://www.sql.ru/forum/job-offers/%d";
        for (var i = 2; i <= 2; i++) {
            printSitePage(startUrl.formatted(i), el -> !"Важно:".equals(el));
        }
    }
}
