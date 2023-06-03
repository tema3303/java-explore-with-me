package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDto {
    private long id;
    @NotBlank
    @Size(max = 20)
    private String app;
    @NotBlank
    @Size(max = 50)
    private String uri;
    @NotBlank
    @Size(max = 20)
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime timestamp;
}