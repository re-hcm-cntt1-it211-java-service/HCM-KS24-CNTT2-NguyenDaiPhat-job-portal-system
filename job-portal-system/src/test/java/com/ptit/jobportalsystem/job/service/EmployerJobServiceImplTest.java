package com.ptit.jobportalsystem.job.service;

import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.JobErrorCode;
import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import com.ptit.jobportalsystem.job.mapper.JobMapper;
import com.ptit.jobportalsystem.job.repository.JobRepository;
import com.ptit.jobportalsystem.job.service.impl.EmployerJobServiceImpl;
import com.ptit.jobportalsystem.security.current.CurrentUserService;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerJobServiceImplTest {

    @Mock private JobRepository jobRepository;
    @Mock private UserRepository userRepository;
    @Mock private JobMapper jobMapper;
    @Mock private CurrentUserService currentUserService;

    @InjectMocks
    private EmployerJobServiceImpl employerJobService;

    private User employer;
    private Job job;
    private JobResponse response;

    @BeforeEach
    void setUp() {

        employer = User.builder()
                .id(1L)
                .fullName("Employer")
                .build();

        job = Job.builder()
                .id(1L)
                .title("Java Dev")
                .description("Spring")
                .salary(BigDecimal.valueOf(1000))
                .location("HCM")
                .status(JobStatus.PENDING)
                .deadline(LocalDateTime.now().plusDays(10))
                .isDeleted(false)
                .employer(employer)
                .build();

        response = JobResponse.builder()
                .id(1L)
                .title("Java Dev")
                .employerId(1L)
                .employerName("Employer")
                .build();
    }

    // ================= CREATE =================
    @Test
    @DisplayName("createJob - success")
    void createJob_success() {

        when(currentUserService.getUserId()).thenReturn(1L);

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Dev");
        request.setDescription("Spring");
        request.setSalary(BigDecimal.valueOf(1000));
        request.setLocation("HCM");
        request.setDeadline(LocalDateTime.now().plusDays(10));

        when(userRepository.getReferenceById(1L)).thenReturn(employer);
        when(jobMapper.toEntity(request)).thenReturn(job);
        when(jobRepository.save(job)).thenReturn(job);
        when(jobMapper.toResponse(job)).thenReturn(response);

        JobResponse result = employerJobService.createJob(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(jobRepository).save(job);
    }

    // ================= UPDATE =================
    @Test
    @DisplayName("updateJob - success")
    void updateJob_success() {

        when(currentUserService.getUserId()).thenReturn(1L);

        UpdateJobRequest request = new UpdateJobRequest();
        request.setTitle("Java Dev 2");
        request.setDescription("Spring 2");
        request.setSalary(BigDecimal.valueOf(2000));
        request.setLocation("HN");
        request.setDeadline(LocalDateTime.now().plusDays(20));

        when(jobRepository.findByIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(job));

        when(jobMapper.toResponse(any(Job.class)))
                .thenReturn(response);

        when(jobRepository.save(any(Job.class)))
                .thenReturn(job);

        JobResponse result = employerJobService.updateJob(1L, request);

        assertThat(result).isNotNull();
        verify(jobRepository).save(job);
    }

    // ================= GET DETAIL =================
    @Test
    @DisplayName("getJobDetail - success")
    void getJobDetail_success() {

        when(currentUserService.getUserId()).thenReturn(1L);

        when(jobRepository.findByIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(job));

        when(jobMapper.toResponse(job))
                .thenReturn(response);

        JobResponse result = employerJobService.getJobDetail(1L);

        assertThat(result).isNotNull();
    }

    // ================= DELETE =================
    @Test
    @DisplayName("deleteJob - success")
    void deleteJob_success() {

        when(currentUserService.getUserId()).thenReturn(1L);

        when(jobRepository.findByIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.of(job));

        employerJobService.deleteJob(1L);

        assertThat(job.getIsDeleted()).isTrue();
        verify(jobRepository).save(job);
    }

    // ================= EXCEPTION =================
    @Test
    @DisplayName("getJobDetail - JOB_NOT_FOUND")
    void getJobDetail_notFound() {

        when(jobRepository.findByIdAndIsDeletedFalse(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                employerJobService.getJobDetail(99L))
                .isInstanceOf(AppException.class)
                .satisfies(ex ->
                        assertThat(((AppException) ex).getErrorCode())
                                .isEqualTo(JobErrorCode.JOB_NOT_FOUND)
                );
    }
}