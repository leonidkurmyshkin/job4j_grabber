package ru.job4j.html;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Post {
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id
                && title.equals(post.title)
                && link.equals(post.link)
                && created.equals(post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link, created);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Post.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("link='" + link + "'")
                .add("description='" + description + "'")
                .add("created=" + created)
                .toString();
    }
}
