package com.braindocs.services;

import com.braindocs.common.MarkedRequestValue;
import com.braindocs.exceptions.ResourceNotFoundException;
import com.braindocs.models.ContactTypeModel;
import com.braindocs.repositories.ContactTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeModel findById(Long id){
        return contactTypeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Тип контакта '" + id + "' не найден"));
    }

    @Transactional
    public List<ContactTypeModel> getTypes(MarkedRequestValue marked){
        switch(marked){
            case ON:
                return contactTypeRepository.findByMarked(false);
            case ONLY:
                return contactTypeRepository.findByMarked(true);
            default:
                return contactTypeRepository.findAll();
        }
    }

    @Transactional
    public List<ContactTypeModel> findAll(MarkedRequestValue marked) {
         return getTypes(marked);
    }
}
