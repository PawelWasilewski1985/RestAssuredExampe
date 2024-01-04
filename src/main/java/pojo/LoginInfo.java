package pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfo {
    private int failedLoginCount;
    private int loginCount;
    private String lastFailedLoginTime;
    private String previousLoginTime;
}
