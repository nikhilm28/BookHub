package com.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static int bookId = 111;
    private static final Scanner sc = new Scanner(System.in);
    private static Catalog catalog = new Catalog();
    private static UserService userService = new UserService();
    private static Library library = new Library();
    private static List<Book> bookList;


    public static void main(String[] args) {
        System.out.println("\n************ Book Sharing Hub ************");
        System.out.println();
        int ch;
        do{
            System.out.println("------------------------------------------");
            System.out.println("Menu");
            System.out.println("------------------------------------------");
            System.out.println("1:Add User      2:Add Book   3:Search Book");
            System.out.println("4:Return Book   5:My Books   6:Quit");
            System.out.println("Enter choice:");
            ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 -> addUser();
                case 2 -> addBook();
                case 3 -> searchBook();
                case 4 -> returnBook();
                case 5 -> myBooks();
            }
        }while (ch!=6);
    }

    /**
     * Options Menu for Search Books
     */
    public static void searchBook(){
        int ch;
        do {
            System.out.println("\nSearch Book\n1:By Title 2:By Author 3:By Keyword 4:In IssueRegister\nEnter choice:");
            ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 -> findBookByName();
                case 2 -> findBookByAuthor();
                case 3 -> findBookByKeywords();
                case 4 -> searchIssueRegister();
            }
        }while (ch!=5);
    }

    /**
     * Option Menu To Check Shared and Borrowed Books
     */
    public static void myBooks(){
        int ch;
        do {
            System.out.println("\nMy Books\n1:Shared 2:Borrowed\nEnter choice:");
            ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 -> bookSharedByMe();
                case 2 -> bookBorrowedByMe();
            }
        }while (ch!=3);
    }

    /**
     * Option Menu to Borrow or Request Book
     */
    public static void borrowOrRequest(){
        System.out.println("\n1:Borrow Book 2:Request Book\nEnter choice:");
        int ch;
        ch = Integer.parseInt(sc.nextLine());
        switch (ch) {
            case 1 -> borrowBook();
            case 2 -> bookRequest();
        }
    }

    /**
     * Option Menu to Discontinue Book
     */
    public static void discontinueTheBook(){
        System.out.println("\nWould you like to Discontinue Book\nYes(y) Or No(n)");
        String ch = sc.nextLine();
        switch (ch){
            case "y":
                discontinueBook();
                break;
            case "n":
                break;
        }
    }

    /**
     * Function to Add User in System
     */
    public static void addUser(){
        System.out.println("Enter Name");
        String name = sc.nextLine();
        System.out.println("Enter Email");
        String email = sc.nextLine();
        User user = new User(name, email);
        userService.addUser(user);
    }

    /**
     * Add new Book to System
     */
    public static void addBook(){
        System.out.println("Enter Book Name");
        String bookName = sc.nextLine();
        System.out.println("Enter Author Name");
        String authorName = sc.nextLine();
        System.out.println("Add Keywords");
        List<String> keywords = Collections.singletonList(sc.nextLine());
        System.out.println("Enter User Email");
        String email = sc.nextLine();
        Book book = new Book(bookId++, bookName, Set.of(authorName), keywords, userService.getUserByEmail(email));
        catalog.addBook(book);
    }

    /**
     * Search Book by Name
     */
    public static void findBookByName(){
        System.out.println("Enter Book Title:");
        String bookName = sc.nextLine();
        bookList = catalog.findByBookName(bookName);
        if (!bookList.isEmpty()){
            displayBooks(bookList);
            borrowOrRequest();
        }
    }

    /**
     * Search Book by AuthorName
     */
    public static void findBookByAuthor(){
        System.out.println("Enter Author Name");
        String authorName = sc.nextLine();
        bookList = catalog.findByBookAuthor(authorName);
        if (!bookList.isEmpty()){
            displayBooks(bookList);
            borrowOrRequest();
        }
    }

    /**
     * Search Book by keywords
     */
    public static void findBookByKeywords(){
        System.out.println("Enter Keyword");
        String keyword = sc.nextLine();
        bookList = catalog.findBookByKeywords(keyword);
        if (!bookList.isEmpty()){
            displayBooks(bookList);
            borrowOrRequest();
        }
    }

    /**
     * Borrow Book from system
     */
    public static void borrowBook(){
        System.out.println("Enter Book Id:");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Your Email");
        String email = sc.nextLine();
        System.out.println("Enter IssueDate");
        LocalDate issueDate = LocalDate.parse(sc.nextLine());
        Book result = catalog.findBookById(id);
        if (result.isAvailable()){
            library.borrowBook(result,userService.getUserByEmail(email),issueDate);
        }
        else {
            logger.warn("Book is already borrowed");
        }
    }

    /**
     * Request a Book which is not available in System
     */
    public static void bookRequest() {
        System.out.println("Enter Book Id:");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Your Email: ");
        String email = sc.nextLine();
        Book result = catalog.findBookById(id);
        if (result.isTaken()) {
            library.requestForTheBook(result, userService.getUserByEmail(email));
            logger.info("Added to waiting list");
        } else {
            logger.info("Book is Available");
        }
    }

    /**
     * Return a Book which was Borrowed by User
     */
    public static void returnBook(){
        System.out.println("Enter Book Name:");
        String bookName = sc.nextLine();
        System.out.println("Enter Your Email");
        String email = sc.nextLine();
        bookList = catalog.findByBookName(bookName);
        List<Book> borrowedBooks = bookList.stream().filter(Book::isTaken).toList();
        if (!borrowedBooks.isEmpty()){
            Book resultSet = borrowedBooks.stream().findFirst().get();
            library.returnBook(resultSet,userService.getUserByEmail(email));
            logger.info("Book returned successfully");
        }else {
            logger.warn("Book was not borrowed");
        }
    }

    /**
     * Make Book status as Discontinued
     */
    public static void discontinueBook(){
        System.out.println("Enter Book Id:");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Your Email:");
        String email = sc.nextLine();
        Book book = catalog.findBookById(id);
        if (book!=null){
            library.discontinueBook(book,userService.getUserByEmail(email));
            logger.info("Book has been Discontinued");
        }else {
            logger.warn("Book not Available");
        }
    }

    /**
     * Display Books Shared by User
     */
    public static void bookSharedByMe(){
        System.out.println("Enter Your Email:");
        String email = sc.nextLine();
        bookList = catalog.mySharedBooks(email);
        if (!bookList.isEmpty()){
            displayBooks(bookList);
            discontinueTheBook();
        }else {
            logger.warn("You have not shared Books");
        }
    }

    /**
     * Display Books borrowed by user
     */
    public static void bookBorrowedByMe(){
        System.out.println("Enter Your Email");
        String email = sc.nextLine();
        bookList = library.myBorrowedBooks(email);
        if (!bookList.isEmpty()){
            displayBooks(bookList);
        }else {
            logger.warn("You have not borrowed Books");
        }
    }

    /**
     * Search Book in IssueRegister using BookName
     */
    public static void searchIssueRegister(){
        System.out.println("Enter Your BookName");
        String bookName = sc.nextLine();
        List<IssueRegister> registerList = library.searchIssueRegister(bookName);
        if (!registerList.isEmpty()){
            displayIssueRegister(registerList);
        }else {
            logger.warn("Book Records not Available");
        }
    }

    /**
     * Display Book which was Requested
     * @param books
     */
    public static void displayBooks(List<Book> books) {
        System.out.format("%10s %5s %5s", "BookId", "Title","Status","Authors");
        books.stream().forEach(b -> System.out.format("\n%10s %5s %10s %10s\n",b.getId(), b.getName(),b.getStatus(),b.getAuthors()));
    }

    /**
     * Display Books which were borrowed
     * @param issueRegisters
     */
    public static void displayIssueRegister(List<IssueRegister> issueRegisters){
        System.out.format("%10s %5s %5s %5s %5s","BookId","BookName","BorrowerName","IssueDate", "DueDate");
        issueRegisters.stream().forEach(b -> System.out.format("\n%10s %5s %10s %10s %10s\n",b.getBook().getId(),b.getBook().getName(),b.getUser().getName(),b.getIssueDate(), b.getDueDate()));
    }
}