package com.ptit.jobportalsystem.application.mapper;

import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidate.fullName", target = "candidateName")

    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")

    @Mapping(source = "cv.id", target = "cvId")
    @Mapping(source = "cv.fileName", target = "cvFileName")
    @Mapping(target = "cvFileUrl", source = "cv.fileUrl")
    ApplicationResponse toResponse(Application application);
}