package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void printSitePage(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            System.out.println(td.parent().child(5).text());
        }
    }

    public static void main(String[] args) throws Exception {
        var startUrl = "https://www.sql.ru/forum/job-offers/%d";
        for (var i = 1; i <= 5; i++) {
            printSitePage(startUrl.formatted(i));
        }
    }
}