package com.ptit.jobportalsystem.job.mapper;

import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.entity.Job;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "employerName", source = "employer.fullName")
    JobResponse toResponse(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Job toEntity(CreateJobRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void update(UpdateJobRequest request,
                @MappingTarget Job job);
}