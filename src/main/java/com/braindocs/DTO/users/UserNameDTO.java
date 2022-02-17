package com.braindocs.DTO.users;

import com.braindocs.models.users.UserModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNameDTO {
   // @NotEmpty(message = "Не указан идентификатор пользователя")
    private Long id;
    private String shortname;

    public UserNameDTO(UserModel userModel) {
        this.id = userModel.getId();
        this.shortname = userModel.getShortname();
    }
}
