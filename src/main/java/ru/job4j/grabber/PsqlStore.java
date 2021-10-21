package ru.job4j.grabber;

import ru.job4j.grabber.utils.Config;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    final private Connection cnn;
    final private Properties config;

    public PsqlStore(Properties config) {
        try {
            this.config = config;
            Class.forName(config.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void save(Post post) {
        try (var ps = cnn.prepareStatement(
                "INSERT INTO %s.post(name, text, link, created) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING;"
                        .formatted(config.getProperty("grabber.schema")))) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Post> getAll() {
        List<Post> rsl = new ArrayList<>();
        try (var ps = cnn.prepareStatement(
                "SELECT * FROM %s.post;"
                        .formatted(config.getProperty("grabber.schema")))) {
            executeQuery(ps, rsl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public Post findById(int id) {
        List<Post> rsl = new ArrayList<>();
        try (var ps = cnn.prepareStatement(
                "SELECT * FROM %s.post WHERE id = ?;"
                        .formatted(config.getProperty("grabber.schema")))) {
            ps.setInt(1, id);
            executeQuery(ps, rsl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl.isEmpty() ? null : rsl.get(0);
    }

    private void executeQuery(PreparedStatement ps, List<Post> rsl) {
        try (ResultSet rSet = ps.executeQuery()) {
            while (rSet.next()) {
                rsl.add(new Post(
                        rSet.getInt("id"),
                        rSet.getString("name"),
                        rSet.getString("text"),
                        rSet.getString("link"),
                        rSet.getTimestamp("created").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        try {
            var store = new PsqlStore(Config.read("grabber.properties"));
            List<Post> posts = new SqlRuParse(new SqlRuDateTimeParser())
                    .list("https://www.sql.ru/forum/job-offers/1");
            store.save(posts.get(0));
            store.save(posts.get(1));
            store.save(posts.get(2));
            System.out.println("Все вакансии:");
            store.getAll().forEach(System.out::println);
            System.out.printf("Вакансия с id = 2:%n%s%n", store.findById(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}