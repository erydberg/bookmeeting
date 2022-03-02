package se.rydberg.bookmeeting.configuration;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationDTO {
    private UUID id;
    private String email;
    private String emailkey;
    private String domain;
    private String startDescription;
    private String bookDescription;
    private boolean allowOnlineParticipantForm;

    public String formattedStartDescription() {
        if(StringUtils.isNotEmpty(startDescription)) {
            return startDescription.replaceAll("(\r\n|\n)", "<br>");
        }else{
            return "";
        }
    }

    public String formattedBookDescription() {
        if(StringUtils.isNotEmpty(bookDescription)){
            return bookDescription.replaceAll("(\r\n|\n)", "<br>");
        }else{
            return "";
        }

    }
}
