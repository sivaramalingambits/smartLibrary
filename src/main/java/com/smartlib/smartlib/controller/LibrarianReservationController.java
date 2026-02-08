package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Reservation;
import com.smartlib.smartlib.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/librarian")
@CrossOrigin
public class LibrarianReservationController {

    private final ReservationService reservationService;

    public LibrarianReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ================= OVERDUE (LIBRARIAN) =================
    @GetMapping("/overdue")
    public List<Map<String, Object>> getOverdueReservations() {

        return reservationService.getAllOverdueReservations()
            .stream()
            .map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("reservationId", r.getId());
                m.put("bookTitle", r.getBookTitle());
                m.put("bookAuthor", r.getBookAuthor());
                m.put("bookCategory", r.getBookCategory());
                m.put("studentEmail", r.getStudentEmail());
                return m;
            })
            .toList();
    }

    // ================= END RESERVATION =================
    @PostMapping("/reservations/end")
    public Map<String, String> endReservation(
            @RequestBody Map<String, Object> payload) {

        Long reservationId =
                Long.valueOf(payload.get("reservationId").toString());

        reservationService.endReservation(reservationId);

        return Map.of("status", "success");
    }

    
}
