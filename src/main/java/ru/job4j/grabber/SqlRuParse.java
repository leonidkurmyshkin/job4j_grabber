package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        try {
            Elements row = Jsoup.connect(link).get()
                    .select(".postslisttopic");
            List<Post> posts = new ArrayList<>();
            for (Element td : row) {
                if (!"Важно:".equals(td.ownText())) {
                    posts.add(detail(td.child(0).attr("href")));
                }
            }
            return posts;
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public Post detail(String link) {
        try {
            Element vacancy = Jsoup.connect(link).get()
                    .selectFirst(".msgTable");
            String title = vacancy.selectFirst(".messageHeader").ownText();
            String description = vacancy.select(".msgBody").get(1).ownText();
            LocalDateTime created = dateTimeParser.parse(
                    vacancy.selectFirst(".msgFooter").ownText()
                            .split("\s+\\[", 2)[0]);
            return new Post(title, link, description, created);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        var startUrl = "https://www.sql.ru/forum/job-offers/%d";
        List<Post> posts = new SqlRuParse(new SqlRuDateTimeParser())
                .list("https://www.sql.ru/forum/job-offers/1");
        posts.forEach(System.out::println);
    }
}
