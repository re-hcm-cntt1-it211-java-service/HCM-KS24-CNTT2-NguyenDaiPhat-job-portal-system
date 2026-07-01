package com.ptit.jobportalsystem.cv.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CvResponse {

    private Long id;
    private String fileName;
    private String fileUrl;
}