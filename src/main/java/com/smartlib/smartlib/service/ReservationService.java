package com.smartlib.smartlib.service;

import com.smartlib.smartlib.model.Reservation;
import com.smartlib.smartlib.repository.ReservationRepository;
import com.smartlib.smartlib.repository.BookRepository;
import com.smartlib.smartlib.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
    }

    // ================= CREATE =================
    @Transactional
    public void createReservation(Long bookId, String studentEmail) {

        if (reservationRepository.existsByBookIdAndStudentEmail(bookId, studentEmail)) {
            return; // already exists
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Reservation reservation = new Reservation();
        reservation.setBookId(book.getId());
        reservation.setBookTitle(book.getTitle());
        reservation.setBookAuthor(book.getAuthor());
        reservation.setBookCategory(book.getCategory());
        reservation.setStudentEmail(studentEmail);
        reservation.setStatus("PENDING");

        reservationRepository.save(reservation);
    }

    // ================= DELETE =================
    @Transactional
    public void cancelReservation(Long bookId, String studentEmail) {

        System.out.println("Deleting reservation for bookId=" + bookId +
                           ", studentEmail=" + studentEmail);

        reservationRepository.deleteByBookIdAndStudentEmail(bookId, studentEmail);
    }

    // ================= FETCH PENDING =================
    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByStatus("PENDING");
    }

    // ================= APPROVE =================
    @Transactional
    public void approveReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus("APPROVED");
        reservationRepository.save(reservation);
    }

    // ================= FETCH APPROVED (STUDENT) =================
    // used by student-dashboard reservations section
    public List<Reservation> getApprovedReservations(String studentEmail) {
        return reservationRepository
                .findByStudentEmailAndStatus(studentEmail, "APPROVED");
    }

    // ================= FETCH OVERDUE (STUDENT) =================
    public List<Reservation> getOverdueReservations(String studentEmail) {
    List<Reservation> approved =
        reservationRepository.findByStudentEmailAndStatus(studentEmail, "APPROVED");

    LocalDateTime now = LocalDateTime.now();

    return approved.stream()
        .filter(r ->
            ChronoUnit.DAYS.between(r.getCreatedAt(), now) <= 6
        )
        .toList();
}

    // ================= RENEW RESERVATION =================
@Transactional
public void renewReservation(Long reservationId, String studentEmail) {

    Reservation reservation = reservationRepository
            .findByIdAndStudentEmail(reservationId, studentEmail)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

    if (!"APPROVED".equals(reservation.getStatus())) {
        throw new RuntimeException("Only approved reservations can be renewed");
    }

    // Reset created_at to now
    reservation.setCreatedAt(LocalDateTime.now());

    reservationRepository.save(reservation);
}

// ================= FETCH ALL OVERDUE (LIBRARIAN) =================
public List<Reservation> getAllOverdueReservations() {
    LocalDateTime now = LocalDateTime.now();

    return reservationRepository.findByStatus("APPROVED")
        .stream()
        .filter(r ->
            ChronoUnit.DAYS.between(r.getCreatedAt(), now) <= 6
        )
        .toList();
}

// ================= END RESERVATION =================
@Transactional
public void endReservation(Long reservationId) {
    reservationRepository.deleteById(reservationId);
}

// ================= FETCH ALL APPROVED =================
@Autowired
    private ReservationRepository repo;

    public List<Reservation> getAllApprovedReservations() {
        return repo.findByStatus("APPROVED");
    }

}
