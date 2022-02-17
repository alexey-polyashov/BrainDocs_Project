package com.braindocs.DTO.users;

import com.braindocs.models.users.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserNameDTO {
   // @NotEmpty(message = "Не указан идентификатор пользователя")
    private Long id;
    private String shortname;

    public UserNameDTO(UserModel userModel) {
        this.id = userModel.getId();
        this.shortname = userModel.getShortname();
    }
}
