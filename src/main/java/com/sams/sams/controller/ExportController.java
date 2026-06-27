package com.sams.sams.controller;

import com.sams.sams.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.*;

@Controller
public class ExportController {

    @Autowired private TenantRepository tenantRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ApartmentRepository apartmentRepository;

    @GetMapping("/export/tenants")
    public void exportTenants(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tenants.csv");
        PrintWriter w = response.getWriter();
        w.println("ID,Name,Apartment,Phone,Email,Status,Join Date");
        tenantRepository.findAll().forEach(t ->
                w.println(t.getId() + "," + t.getName() + "," + t.getApartmentNumber()
                        + "," + t.getPhone() + "," + (t.getEmail()!=null?t.getEmail():"")
                        + "," + t.getStatus() + "," + (t.getJoinDate()!=null?t.getJoinDate():"")));
        w.flush();
    }

    @GetMapping("/export/billing")
    public void exportBilling(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=billing.csv");
        PrintWriter w = response.getWriter();
        w.println("Invoice ID,Tenant,Apartment,Amount,Due Date,Status");
        paymentRepository.findAll().forEach(p ->
                w.println(p.getInvoiceId() + "," + p.getTenantName() + "," + p.getApartmentNumber()
                        + "," + p.getAmount() + "," + (p.getDueDate()!=null?p.getDueDate():"")
                        + "," + p.getStatus()));
        w.flush();
    }

    @GetMapping("/export/bookings")
    public void exportBookings(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=bookings.csv");
        PrintWriter w = response.getWriter();
        w.println("ID,Tenant,Apartment,Amount,Check In,Check Out,Status");
        bookingRepository.findAll().forEach(b ->
                w.println(b.getId() + "," + b.getTenantName() + "," + b.getApartmentNumber()
                        + "," + b.getAmount() + "," + (b.getCheckInDate()!=null?b.getCheckInDate():"")
                        + "," + (b.getCheckOutDate()!=null?b.getCheckOutDate():"") + "," + b.getStatus()));
        w.flush();
    }

    @GetMapping("/export/apartments")
    public void exportApartments(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=apartments.csv");
        PrintWriter w = response.getWriter();
        w.println("ID,Name,Number,Location,Rooms,Rent,Status,Tenant");
        apartmentRepository.findAll().forEach(a ->
                w.println(a.getId() + "," + a.getApartmentName() + "," + a.getApartmentNumber()
                        + "," + a.getLocation() + "," + a.getRooms() + "," + a.getRent()
                        + "," + a.getStatus() + "," + (a.getTenantName()!=null?a.getTenantName():"")));
        w.flush();
    }
}