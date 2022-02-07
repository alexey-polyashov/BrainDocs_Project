package com.braindocs.braindocs.services;

import com.braindocs.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.braindocs.models.ContactTypeModel;
import com.braindocs.braindocs.repositories.ContactTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeModel findById(Long id){
        return contactTypeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Тип контакта '" + id + "' не найден"));
    }
}
