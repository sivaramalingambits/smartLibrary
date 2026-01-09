import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.*;

public class MainServer {

    static List<Book> books = new ArrayList<>();
    static List<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        // Sample catalog
        books.add(new Book(1,"Clean Code","Robert Martin"));
        books.add(new Book(2,"Effective Java","Joshua Bloch"));
        books.add(new Book(3,"Design Patterns","GoF"));
        books.add(new Book(4,"Refactoring","Martin Fowler"));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        server.createContext("/books", new BooksHandler());
        server.createContext("/reserve", new ReserveHandler());
        server.createContext("/overdue", new OverdueHandler());
        server.createContext("/analytics", new AnalyticsHandler());

        server.start();
        System.out.println("SmartLibrary running at http://localhost:8080");
    }

    // ===== BOOK SEARCH =====
    static class BooksHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            StringBuilder json = new StringBuilder("[");
            for(Book b: books){
                json.append("{\"id\":").append(b.id)
                    .append(",\"title\":\"").append(b.title)
                    .append("\",\"author\":\"").append(b.author)
                    .append("\"},");
            }
            json.deleteCharAt(json.length()-1).append("]");
            send(ex,json.toString());
        }
    }

    // ===== RESERVATION =====
    static class ReserveHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            reservations.add(
                new Reservation("user1",LocalDate.now().minusDays(5))
            );
            send(ex,"Book reserved successfully");
        }
    }

    // ===== OVERDUE =====
    static class OverdueHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            long count = reservations.stream()
                    .filter(r -> r.dueDate.isBefore(LocalDate.now()))
                    .count();
            send(ex,"Overdue books: "+count);
        }
    }

    // ===== ANALYTICS =====
    static class AnalyticsHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String response =
                "Total Books: "+books.size()+
                ", Reservations: "+reservations.size();
            send(ex,response);
        }
    }

    static void send(HttpExchange ex,String response) throws IOException{
        ex.getResponseHeaders().add("Access-Control-Allow-Origin","*");
        ex.sendResponseHeaders(200,response.length());
        OutputStream os = ex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    // ===== MODELS =====
    static class Book{
        int id; String title, author;
        Book(int i,String t,String a){id=i;title=t;author=a;}
    }

    static class Reservation{
        String user;
        LocalDate dueDate;
        Reservation(String u,LocalDate d){user=u;dueDate=d;}
    }
}
