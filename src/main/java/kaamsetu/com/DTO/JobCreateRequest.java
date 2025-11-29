package kaamsetu.com.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobCreateRequest {
    private String title;

    private String description;

    private String category;

    private Double budget;

    private LocalDate deadline;

    private Long clientId;

}
