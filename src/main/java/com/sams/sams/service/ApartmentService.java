package com.sams.sams.service;

import com.sams.sams.model.Apartment;
import com.sams.sams.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Apartment getApartmentById(Long id) {
        return apartmentRepository.findById(id).orElse(null);
    }

    public void saveApartment(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }
}