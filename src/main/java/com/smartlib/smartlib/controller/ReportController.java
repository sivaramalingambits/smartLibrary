package com.smartlib.smartlib.controller;

import com.smartlib.smartlib.model.Book;
import com.smartlib.smartlib.repository.BookRepository;
import com.smartlib.smartlib.repository.StudentRepository;
import com.smartlib.smartlib.service.ReservationService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/librarian/reports")
public class ReportController {

    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final ReservationService reservationService;

    public ReportController(BookRepository bookRepository,
                            StudentRepository studentRepository,
                            ReservationService reservationService) {
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.reservationService = reservationService;
    }

    // ================= BOOKS REPORT =================
    @GetMapping("/books")
    public ResponseEntity<byte[]> booksReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Title");
        header.createCell(1).setCellValue("Author");
        header.createCell(2).setCellValue("Category");

        int rowIdx = 1;
        for (Book b : bookRepository.findAll()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(b.getTitle());
            row.createCell(1).setCellValue(b.getAuthor());
            row.createCell(2).setCellValue(b.getCategory());
        }

        return excelResponse(workbook, "books_report.xlsx");
    }

    // ================= MEMBERS REPORT =================
    @GetMapping("/members")
    public ResponseEntity<byte[]> membersReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Members");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Email");

        int rowIdx = 1;
        for (var s : studentRepository.findAll()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getName());
            row.createCell(2).setCellValue(s.getEmail());
        }

        return excelResponse(workbook, "members_report.xlsx");
    }

    // ================= PENDING REQUESTS =================
    @GetMapping("/pending")
    public ResponseEntity<byte[]> pendingReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pending Requests");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Book");
        header.createCell(1).setCellValue("Student Email");

        int rowIdx = 1;
        for (var r : reservationService.getPendingReservations()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(r.getBookTitle());
            row.createCell(1).setCellValue(r.getStudentEmail());
        }

        return excelResponse(workbook, "pending_requests.xlsx");
    }

    // ================= OVERDUE =================
    @GetMapping("/overdue")
    public ResponseEntity<byte[]> overdueReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Overdue");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Book");
        header.createCell(1).setCellValue("Student Email");

        int rowIdx = 1;
        for (var r : reservationService.getAllOverdueReservations()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(r.getBookTitle());
            row.createCell(1).setCellValue(r.getStudentEmail());
        }

        return excelResponse(workbook, "overdue_report.xlsx");
    }

    // ================= APPROVED RESERVATIONS =================
    @GetMapping("/approved-reservations")
    public ResponseEntity<byte[]> approvedReservationsReport() throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Approved Reservations");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Book Title");
        header.createCell(1).setCellValue("Author");
        header.createCell(2).setCellValue("Category");
        header.createCell(3).setCellValue("Student Email");
        header.createCell(4).setCellValue("Reserved On");

        int rowIdx = 1;
        for (var r : reservationService.getAllApprovedReservations()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(r.getBookTitle());
            row.createCell(1).setCellValue(r.getBookAuthor());
            row.createCell(2).setCellValue(r.getBookCategory());
            row.createCell(3).setCellValue(r.getStudentEmail());
            row.createCell(4).setCellValue(r.getCreatedAt().toString());
        }

        return excelResponse(workbook, "approved_reservations.xlsx");
    }

    // ================= COMMON EXCEL RESPONSE =================
    private ResponseEntity<byte[]> excelResponse(Workbook workbook, String fileName) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .body(out.toByteArray());
    }

    
}
