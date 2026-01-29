package kaamsetu.com.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSubmitRequest {

    private Long studentId;

    private String messages;

    private String link;

}
