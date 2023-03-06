package com.books;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This is a Book Entity that will be used to store
 * Book Details like Id,BookName,BookAuthors,BookStatus
 */
public class Book {

    private static final String INVALID_NAME = "Invalid Name";
    private static final String INVALID_AUTHORS = "Invalid Authors";
    private static final String INVALID_KEYWORD = "Invalid Keyword";
    private static final String INVALID_OWNER = "Invalid Owner";
    private static final String VALIDATE_NAME = "^[a-zA-Z0-9_ ]*$";

    /**
     * bookid  for all books
     */
    private int id;
    /**
     * valid bookName used to display everywhere
     */
    private String name;
    /**
     * Authors of the book
     */
    private Set<String> authors;

    /**
     * Keywords related to the Book
     */
    private List<String> keywords;
    /**
     * bookStatus indicates if book is available or not
     */
    private BookStatus status;

    private User owner;

    public Book(int id, String name, Set<String> authors, List<String> keywords, User owner) {
        this.id = id;

        this.name = name;
        if (name==null || name.isEmpty() || name.isBlank()){
            throw new IllegalArgumentException(INVALID_NAME);
        }

        this.authors = authors;
        if (authors==null || authors.isEmpty()){
            throw new IllegalArgumentException(INVALID_AUTHORS);
        }

        this.keywords = keywords;
        if (keywords==null || keywords.isEmpty()){
            throw new IllegalArgumentException(INVALID_KEYWORD);
        }

        this.status = BookStatus.AVAILABLE;

        this.owner = owner;
        if (owner==null){
            throw new IllegalArgumentException(INVALID_OWNER);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getKeywords() {
        return Collections.unmodifiableList(keywords);
    }

    public Set<String> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }

    public boolean isTaken(){
        return status.equals(BookStatus.ISSUED);
    }

    public boolean isAvailable(){
       return status.equals(BookStatus.AVAILABLE);
    }

    public boolean isDiscontinued(){
        return status.equals(BookStatus.DISCONTINUED);
    }

    public void issueBook() {
        this.status = BookStatus.ISSUED;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
    }

    public void discontinueBook(){
        this.status = BookStatus.DISCONTINUED;
    }

    public User getOwner() {
        return owner;
    }

    public BookStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authors=" + authors +
                ", status=" + status +
                ", owner=" + owner +
                '}';
    }
}
