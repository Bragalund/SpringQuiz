package no.group3.springquiz.data;

/**
 * Created by josoder on 07.12.17.
 */
public class RegUserDto {
    private String username;
    private String password;

    public RegUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
