package pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateSessionPayload {
    private String username;
    private String password;
}
