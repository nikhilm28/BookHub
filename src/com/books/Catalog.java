package com.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Catalog {

    private static final String bookAvailable = "BOOK AVAILABLE";
    private static final String bookAdded = "BOOK ADDED SUCCESSFULLY";
    private static final String bookIssued = "BOOK ISSUED";
    private static final String bookNotFound = "BOOK NOT FOUND";
    private static final String userAdded = "USER ADDED SUCCESSFULLY";
    private static final String authorNotFound = "AUTHOR NOT FOUND";

    private static Logger logger = LoggerFactory.getLogger(Catalog.class);

    Map<Integer,Book> books = new HashMap<>();
    UserService userService = new UserService();

    Catalog(){
        initialBooks();
    }

    /**
     * Add Book to Catalog
     */
    public void addBook(Book book){
        books.put(book.getId(),book);
        logger.info(bookAdded);
       }

    private void initialBooks(){
        books.put(101,new Book(101,"JAVA",Set.of("ABC","AWER"),Arrays.asList("java8","java"),userService.getUserByEmail("nikhil@gmail.com")));
        books.put(102,new Book(102,"JAVA with SpringBoot",Set.of("DBHU"),Arrays.asList("java8","spring"),userService.getUserByEmail("akshay@gmail.com")));
        books.put(103,new Book(103,"MicroServices",Set.of("ZXCED"),Arrays.asList("microservices"),userService.getUserByEmail("gaurav@gmail.com")));
        books.put(104,new Book(104,"MicroServices with SpringBoot",Set.of("WERF"),Arrays.asList("spring","microservices"),userService.getUserByEmail("nikhil@gmail.com")));
        books.put(105,new Book(105,"Docker",Set.of("WDERF"),Arrays.asList("docker"),userService.getUserByEmail("akshay@gmail.com")));
        books.put(106,new Book(106,"Docker & Kubernetes",Set.of("ABC"),Arrays.asList("docker"),userService.getUserByEmail("nikhil@gmail.com")));
        books.put(107,new Book(107,"Kubernetes",Set.of("DBHU"),Arrays.asList("kubernetes"),userService.getUserByEmail("gaurav@gmail.com")));
        books.put(108,new Book(108,"J2EE",Set.of("RANGA"),Arrays.asList("java8"),userService.getUserByEmail("akshay@gmail.com")));
        books.put(109,new Book(109,"Spring with RESTApi",Set.of("QWEDR"),Arrays.asList("restapi"),userService.getUserByEmail("nikhil@gmail.com")));
        books.put(110,new Book(110,"World of K8s",Set.of("SDEEF"),Arrays.asList("kubernetes"),userService.getUserByEmail("nikhil@gmail.com")));
        logger.info("Data stored");
    }

    /**
     * Search Book by bookName
     * @param bookName
     * @return list of Books matching with bookName
     */
    public List<Book> findByBookName(String bookName) {
        if (bookName == null || bookName.isBlank()){
            logger.info("BookName is null or blank cannot search for book");
        }
            logger.info("BookName Received: {}",bookName);
        List<Book> bookList = books.values().stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName)).toList();
        List<Book> unmodifiableList = Collections.unmodifiableList(bookList);
        if (!unmodifiableList.isEmpty()){
            return unmodifiableList;
        }else {
            logger.error(bookNotFound);
            return Collections.emptyList();
        }
    }

    /**
     * Search Book by Author-names
     * @param authorName Author name to be searched
     * @return list of book matching with author
     */
    public List<Book> findByBookAuthor(String authorName){
        if (authorName == null || authorName.isBlank()){
            logger.info("AuthorName is null or blank cannot search for author");
        }
        logger.info("AuthorName Received: {}", authorName);
        List<Book> bookList = books.values().stream()
                .filter(b -> b.getAuthors()
                        .toString().equalsIgnoreCase(authorName)).toList();
        List<Book> unmodifiableList = Collections.unmodifiableList(bookList);
        if (!unmodifiableList.isEmpty()) return unmodifiableList;
        else {
            logger.error(authorNotFound);
            return Collections.emptyList();
        }
    }

    /**
     * Search for Book using Keywords
     * @param keywords keywords related to the Book
     * @return
     */
    public List<Book> findBookByKeywords(String keywords){
        if (keywords == null || keywords.isBlank()){
            logger.info("Keyword is null or blank cannot search for keyword");
        }
        logger.info("Keyword Received: {}", keywords);
        List<Book> bookList = books.values().stream()
                .filter(book -> book.getKeywords().contains(keywords)).toList();
        List<Book> unmodifiableList = Collections.unmodifiableList(bookList);
        if (!unmodifiableList.isEmpty()){
            return unmodifiableList;
        }else {
            logger.error(bookNotFound);
            return Collections.emptyList();
        }
    }

    /**
     * Get Books Shared by user
     * @param email email of the user
     * @return
     */
    public List<Book> mySharedBooks(String email){
        if (email == null || email.isBlank() || email.isEmpty()){
            logger.info("Email is null or blank cannot search for books");
        }
        logger.info("EmailId Received: {}",email);
        List<Book> result = books.values().stream()
                .filter(b -> b.getOwner().getEmail().contains(email)).toList();
        List<Book> unmodifiableList = Collections.unmodifiableList(result);
        if (!unmodifiableList.isEmpty()){
            return unmodifiableList;
        }else {
            logger.error(bookNotFound);
            return Collections.emptyList();
        }
    }

    /**
     * Search Book using Book Id
     * @param id Id of Book to be searched
     * @return
     */
    public Book findBookById(int id){
        logger.info("BookId Received: {}",id);
        Book book = books.get(id);
        return book;
    }
}