package uz.lb.fxchatserver.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.lb.fxchatserver.enums.AccountRoleEnums;
import uz.lb.fxchatserver.enums.GeneralStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {
    String login;
    AccountRoleEnums role;
    GeneralStatus status;
    String jwt;
}
