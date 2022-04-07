package com.braindocs.services;

import com.braindocs.common.history.HistoryOperationType;
import com.braindocs.dto.HistoryStampDTO;
import com.braindocs.dto.users.UserNameDTO;
import com.braindocs.models.HistoryStampModel;
import com.braindocs.models.users.UserModel;
import com.braindocs.repositories.HistoryRepository;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserService userService;

    public HistoryStampModel createHistoryStamp(HistoryOperationType type) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserModel user = null;
        // TODO this should be available to authorized users only
        if (principal != null && !Objects.equals(principal.getName(), "anonymousUser")) {
            user = userService.findByUsernameOrThrow(principal.getName());
        } else {
            user = userService.findById(1L); // delete this after
        }

        HistoryStampModel historyStampModel = new HistoryStampModel();
        historyStampModel.setAuthor(user);
        historyStampModel.setChangeType(type.name());
        historyStampModel = historyRepository.save(historyStampModel);
        return historyStampModel;
    }

    public List<HistoryStampModel> getHistoryStampsByType(HistoryOperationType type) {
        return historyRepository.findAllByChangeType(type.name());
    }

    public List<HistoryStampModel> getAllStamps() {
        return historyRepository.findAll();
    }

    public List<HistoryStampModel> findAllByCreateTimeBetween(LocalDateTime since, LocalDateTime before) {
        return historyRepository.findAllByCreateTimeBetween(since, before);
    }

    public List<HistoryStampDTO> toDTOList(List<HistoryStampModel> models) {
        return models.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public HistoryStampDTO toDTO(HistoryStampModel model) {
        UserNameDTO userDto = new UserNameDTO(model.getAuthor());
        return new HistoryStampDTO(model.getId(), model.getChangeType(), userDto, model.getCreateTime().toLocalDate().toString());
    }
}
