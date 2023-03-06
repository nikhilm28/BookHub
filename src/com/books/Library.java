package com.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class Library {
    private static Logger logger = LoggerFactory.getLogger(Library.class);

    private static final Map<Book, List<User>> waitingList = new HashMap<>();
    private static final Map<User, Set<Book>> borrowersList = new HashMap<>();
    private static final List<IssueRegister> issueList = new ArrayList<>();


    /**
     * Borrow a book from community if it is available
     * @param book Requested copy of a book
     * @param borrower user who has borrowed a book
     */
    public void borrowBook(Book book, User borrower, LocalDate issueDate) {
        if (book == null || borrower == null){
            logger.error("Book or user is null cannot borrow the book");
            return;
        }
        logger.info("Book Borrowed: {},Borrower: {}",book.getName(),borrower.getName());
        if (book.isAvailable()) {
            Set<Book> bookSet;
            if (borrowersList.containsKey(borrower)) {
                bookSet = borrowersList.get(borrower);
            } else {
                bookSet = new HashSet<>();
            }
            bookSet.add(book);
            book.issueBook();
            borrowersList.put(borrower, bookSet);
            logger.info("Book Borrowed Successfully");
            addToIssueList(book,borrower,issueDate);
        }
    }

    /**
     * Request a Book if it is not available in system
     * @param book Book Information to request for
     * @param user User who has requested a Book
     */
    public void requestForTheBook(Book book,User user){
        if (book == null  || user == null){
            logger.error("Book or user is null cannot request for book");
            return;
        }
        logger.info("Book Requested: {},User: {}",book.getName(),user.getName());
        if (book.isTaken()){
            if (waitingList.get(book)==null){
                List<User> users = new ArrayList<>();
                users.add(user);
                waitingList.put(book,users);
            }
        }
    }

    /**
     * Return a book if borrowed
     * @param book borrowed book to be returned
     * @param user user who has borrowed the book
     * @return
     */
    public boolean returnBook(Book book, User user) {
        if (book == null || user == null){
            logger.error("Book or user is null cannot return the book");
            return false;
        }
        logger.info("Book Returned: {},Borrowed By: {}",book.getName(),user.getName());
        if (book.isTaken()){
            List<Book> books = new ArrayList<>();
            books.addAll(borrowersList.get(user));
            Book borrowedBook = books.stream().
                    filter(b -> b.getId()==book.getId()).findFirst().get();
            if (borrowedBook!=null){
                book.returnBook();
                return true;
            }
        }
        return false;
    }

    /**
     * Discontinue the Book from the system
     * @param book Book to be discontinued from the system
     * @param user User who want to discontinue the Book
     */
    public void discontinueBook(Book book, User user){
        if (book==null || user==null){
            logger.error("Book or user is null cannot discontinue the book");
        }
        logger.info("Book Discontinued: {},Book Owner: {}",book.getName(),user.getName());
        if (!book.isAvailable() && book.getOwner().equals(user)){
                book.discontinueBook();
                logger.info("Book has been discontinued");
        }
    }

    /**
     * Display Book borrowed by the User
     * @param email user email
     * @return
     */
    public List<Book> myBorrowedBooks(String email){
        if (email == null || email.isBlank()){
            logger.info("Email is null or blank cannot show borrowed books");
        }
        logger.info("Borrower EmailId Received: {}",email);
        for (Map.Entry<User,Set<Book>> entry : borrowersList.entrySet()){
            User user = entry.getKey();
            Set<Book> books = entry.getValue();
            List<Book> mybooks = new ArrayList<>();
            mybooks.addAll(books);
            if (user.getEmail().equalsIgnoreCase(email)){
                return mybooks;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Add Book and Borrower to the IssueList
     * @param book Borrowed Book
     * @param borrower Borrower of the Book
     * @param issueDate Book issue date
     */
    public void addToIssueList(Book book, User borrower, LocalDate issueDate){
        if (book == null || borrower == null || issueDate == null){
            logger.info("Book or user or issuedate in null cannot add to issuelist");
        }
        logger.info("Book Issued: {},Borrower: {}",book.getName(),borrower.getName());
        if (book.isTaken()){
            issueList.add(new IssueRegister(book,borrower,issueDate));
            logger.info("IssueDate: {}, DueDate: {}",issueDate,issueDate.plusDays(10));
        }
    }

    /**
     * Search for a Book in IssueRegister
     * @param bookName BookName to be searched
     * @return List of users who has borrowed the Book
     */
    public List<IssueRegister> searchIssueRegister(String bookName){
        if (bookName==null || bookName.isBlank()){
            logger.info("Book name is null or blank cannot search in issue register");
        }
        logger.info("BookName Received: {}",bookName);
        List<IssueRegister> issueRegisterList = issueList.stream()
                .filter(is -> is.getBook().getName().equalsIgnoreCase(bookName)).toList();
        List<IssueRegister> unmodifiableList = Collections.unmodifiableList(issueRegisterList);
        if (!unmodifiableList.isEmpty()){
            return unmodifiableList;
        }else {
            return Collections.emptyList();
        }
    }
}