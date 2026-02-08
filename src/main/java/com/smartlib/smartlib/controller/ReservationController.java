package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Reservation;
import com.smartlib.smartlib.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservation")
@CrossOrigin
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ================= REQUEST =================
    @PostMapping("/request")
    public Map<String, String> requestReservation(@RequestBody Map<String, Object> payload) {

        Long bookId = Long.valueOf(payload.get("bookId").toString());
        String studentEmail = payload.get("studentEmail").toString();

        reservationService.createReservation(bookId, studentEmail);

        return Map.of("status", "success");
    }

    // ================= CANCEL =================
    @PostMapping("/cancel")   
    public Map<String, String> cancelReservation(@RequestBody Map<String, Object> payload) {

        Long bookId = Long.valueOf(payload.get("bookId").toString());
        String studentEmail = payload.get("studentEmail").toString();

        reservationService.cancelReservation(bookId, studentEmail);

        return Map.of("status", "success");
    }

    // ================= APPROVED (STUDENT VIEW) =================
    // Used by Student Dashboard → Reservations section
    @GetMapping("/approved")
    public List<Reservation> getApprovedReservations(
            @RequestParam String studentEmail) {

        return reservationService.getApprovedReservations(studentEmail);
    }

    // ================= OVERDUE (STUDENT VIEW) =================
    @GetMapping("/overdue")
    public List<Map<String, Object>> getOverdueReservations(@RequestParam String studentEmail) {
    return reservationService.getOverdueReservations(studentEmail)
        .stream()
        .map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("reservationId", r.getId());   
            m.put("title", r.getBookTitle());
            m.put("author", r.getBookAuthor());
            m.put("category", r.getBookCategory());
            m.put("lastDate", r.getCreatedAt().plusDays(8)); 
            return m;
        })
        .toList();
    }

    // ================= RENEW RESERVATION =================
@PostMapping("/renew")
public Map<String, String> renewReservation(@RequestBody Map<String, Object> payload) {

    Long reservationId = Long.valueOf(payload.get("reservationId").toString());
    String studentEmail = payload.get("studentEmail").toString();

    reservationService.renewReservation(reservationId, studentEmail);

    return Map.of("status", "success");
}

// ================= APPROVED (LIBRARIAN VIEW) =================
// Used by Librarian Dashboard → Reservations section
@GetMapping("/approved/all")
public List<Map<String, Object>> getAllApprovedReservations() {

    return reservationService.getAllApprovedReservations()
        .stream()
        .map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("reservationId", r.getId());
            m.put("studentEmail", r.getStudentEmail());
            m.put("bookTitle", r.getBookTitle());
            m.put("bookAuthor", r.getBookAuthor());
            m.put("bookCategory", r.getBookCategory());
            m.put("reservedOn", r.getCreatedAt());
            m.put("status", r.getStatus());
            return m;
        })
        .toList();
}


}
