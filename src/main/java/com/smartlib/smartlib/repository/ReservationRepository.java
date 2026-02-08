package com.smartlib.smartlib.repository;

import com.smartlib.smartlib.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    void deleteByBookIdAndStudentEmail(Long bookId, String studentEmail);

    boolean existsByBookIdAndStudentEmail(Long bookId, String studentEmail);

    Optional<Reservation> findByBookIdAndStudentEmail(Long bookId, String studentEmail);

    List<Reservation> findByStudentEmailAndStatus(String studentEmail, String status);
    List<Reservation> findByStatus(String status);
    Optional<Reservation> findByIdAndStudentEmail(Long id, String studentEmail);

}
