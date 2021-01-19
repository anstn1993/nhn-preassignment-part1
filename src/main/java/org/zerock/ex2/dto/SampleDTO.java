package org.zerock.ex2.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SampleDTO {
    private Long sno;

    private String first;

    private String last;

    private LocalDateTime regTime;
}
