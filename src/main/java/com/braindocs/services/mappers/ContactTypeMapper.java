package com.braindocs.services.mappers;

import com.braindocs.dto.ContactTypeDTO;
import com.braindocs.models.ContactTypeModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactTypeMapper {

    private final ModelMapper modelMapper;

    public ContactTypeDTO toDTO(ContactTypeModel model) {
        return modelMapper.map(model, ContactTypeDTO.class);
    }

}