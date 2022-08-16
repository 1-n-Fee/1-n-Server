package konkuk.nServer.domain.account.dto.oauth;

import lombok.Data;

@Data
public class GoogleProfile {
    public String id;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;
}
