package com.braindocs.dto;

import com.braindocs.dto.users.UserNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryStampDTO {

    private Long id;
    private String changeType;
    private UserNameDTO author;
    private String createTime;
}
