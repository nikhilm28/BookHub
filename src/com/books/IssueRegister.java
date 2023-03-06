package com.books;

import java.time.LocalDate;

public class IssueRegister {

    private static final String INVALID_BOOK = "Invalid Book";
    private static final String INVALID_USER = "Invalid User";
    private static final String INVALID_DATE = "Invalid IssueDate";

    private Book book;
    private User user;
    private LocalDate issueDate;
    private LocalDate dueDate;

    private static final int MAX_DAYS = 10;

    public IssueRegister(Book book, User user, LocalDate issueDate) {
        this.book = book;
        if (book == null){
            throw new IllegalArgumentException(INVALID_BOOK);
        }

        this.user = user;
        if (user == null){
            throw new IllegalArgumentException(INVALID_USER);
        }

        this.issueDate = issueDate;
        if (issueDate == null){
            throw new IllegalArgumentException(INVALID_DATE);
        }

        this.dueDate = issueDate.plusDays(MAX_DAYS);
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "IssueRegister{" +
                "issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", book=" + book +
                ", user=" + user +
                "}";
    }
}