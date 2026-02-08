package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Reservation;
import com.smartlib.smartlib.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/librarian/issues") 
public class IssueController {

    private final ReservationService reservationService;

    public IssueController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Load Issue Books table (PENDING reservations)
    @GetMapping("/pending")
    public List<Reservation> getPendingReservations() {
        return reservationService.getPendingReservations();
    }

    // Approve reservation (JSON body)
    @PostMapping("/approve")
    public Map<String, String> approveReservation(
            @RequestBody Map<String, Long> payload) {

        Long reservationId = payload.get("reservationId");

        reservationService.approveReservation(reservationId);

        Map<String, String> res = new HashMap<>();
        res.put("status", "success");
        return res;
    }
}
