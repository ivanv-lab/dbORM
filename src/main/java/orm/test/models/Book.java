package orm.test.models;

import org.orm.annotations.Entity;
import org.orm.annotations.Id;

import java.util.Date;

@Entity(tableName = "Book")
public class Book {
    @Id
    private Long id;
    private String name;
    private String author;
    private Date date;

    public Book() {}

    public Book(Long id, String name, String author, Date date) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
