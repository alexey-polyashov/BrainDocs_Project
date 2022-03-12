package com.braindocs.services;

import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.ContactTypeModel;
import com.braindocs.repositories.ContactTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeModel findById(Long id){
        return contactTypeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Тип контакта '" + id + "' не найден"));
    }

    public List<ContactTypeModel> findAll() {
        return contactTypeRepository.findAll();
    }
}
