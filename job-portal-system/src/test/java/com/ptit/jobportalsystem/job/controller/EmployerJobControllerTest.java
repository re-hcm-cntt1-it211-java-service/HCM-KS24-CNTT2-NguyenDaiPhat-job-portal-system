package com.ptit.jobportalsystem.job.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import com.ptit.jobportalsystem.job.service.EmployerJobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@WebMvcTest(EmployerJobController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployerJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private EmployerJobService employerJobService;

    private JobResponse sampleResponse;

    @BeforeEach
    void setUp() {

        sampleResponse = JobResponse.builder()
                .id(1L)
                .employerId(1L)
                .employerName("PTIT Company")
                .title("Java Backend Developer")
                .description("Spring Boot Developer")
                .salary(BigDecimal.valueOf(15000000))
                .location("Ha Noi")
                .status(JobStatus.PENDING)
                .deadline(LocalDateTime.now().plusDays(30))
                .build();
    }

    // ============ POST /api/v1/employer/jobs ============
    @Test
    @DisplayName("POST /api/v1/employer/jobs - Valid request should return 200")
    void createJob_validRequest_shouldReturn200() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Backend Developer");
        request.setDescription("Spring Boot Developer");
        request.setSalary(BigDecimal.valueOf(15000000));
        request.setLocation("Ha Noi");
        request.setDeadline(LocalDateTime.now().plusDays(30));

        when(employerJobService.createJob(any()))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Đăng tin tuyển dụng thành công, vui lòng chờ Admin phê duyệt"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Java Backend Developer"))
                .andExpect(jsonPath("$.data.location").value("Ha Noi"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Missing title should return 400")
    void createJob_missingTitle_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setDescription("Spring Boot Developer");
        request.setSalary(BigDecimal.valueOf(15000000));
        request.setLocation("Ha Noi");
        request.setDeadline(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.title")
                        .value("Tiêu đề công việc không được để trống"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Missing description should return 400")
    void createJob_missingDescription_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Backend Developer");
        request.setSalary(BigDecimal.valueOf(15000000));
        request.setLocation("Ha Noi");
        request.setDeadline(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.description")
                        .value("Mô tả công việc không được để trống"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Invalid salary should return 400")
    void createJob_invalidSalary_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Backend Developer");
        request.setDescription("Spring Boot Developer");
        request.setSalary(BigDecimal.valueOf(-1));
        request.setLocation("Ha Noi");
        request.setDeadline(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.salary")
                        .value("Mức lương phải lớn hơn hoặc bằng 0"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Missing location should return 400")
    void createJob_missingLocation_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Backend Developer");
        request.setDescription("Spring Boot Developer");
        request.setSalary(BigDecimal.valueOf(15000000));
        request.setDeadline(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.location")
                        .value("Địa điểm làm việc không được để trống"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Invalid deadline should return 400")
    void createJob_invalidDeadline_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("Java Backend Developer");
        request.setDescription("Spring Boot Developer");
        request.setSalary(BigDecimal.valueOf(15000000));
        request.setLocation("Ha Noi");
        request.setDeadline(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.deadline")
                        .value("Hạn nộp hồ sơ phải sau thời điểm hiện tại"));
    }

    @Test
    @DisplayName("POST /api/v1/employer/jobs - Multiple validation errors should return 400")
    void createJob_multipleValidationErrors_shouldReturn400() throws Exception {

        CreateJobRequest request = new CreateJobRequest();
        request.setTitle("");
        request.setDescription("");
        request.setSalary(BigDecimal.valueOf(-1));
        request.setLocation("");
        request.setDeadline(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/api/v1/employer/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.title")
                        .value("Tiêu đề công việc không được để trống"))
                .andExpect(jsonPath("$.data.description")
                        .value("Mô tả công việc không được để trống"))
                .andExpect(jsonPath("$.data.salary")
                        .value("Mức lương phải lớn hơn hoặc bằng 0"))
                .andExpect(jsonPath("$.data.location")
                        .value("Địa điểm làm việc không được để trống"))
                .andExpect(jsonPath("$.data.deadline")
                        .value("Hạn nộp hồ sơ phải sau thời điểm hiện tại"));
    }

    // ============ PUT /api/v1/employer/jobs/{id} ============
    @Test
    @DisplayName("PUT /api/v1/employer/jobs/{id} - Valid request should return 200")
    void updateJob_validRequest_shouldReturn200() throws Exception {

        UpdateJobRequest request = new UpdateJobRequest();
        request.setTitle("Java Backend Developer Updated");
        request.setDescription("Updated Description");
        request.setSalary(BigDecimal.valueOf(20000000));
        request.setLocation("Ho Chi Minh");
        request.setDeadline(LocalDateTime.now().plusDays(60));

        JobResponse updatedResponse = JobResponse.builder()
                .id(1L)
                .employerId(1L)
                .employerName("PTIT Company")
                .title("Java Backend Developer Updated")
                .description("Updated Description")
                .salary(BigDecimal.valueOf(20000000))
                .location("Ho Chi Minh")
                .status(JobStatus.PENDING)
                .deadline(request.getDeadline())
                .build();

        when(employerJobService.updateJob(eq(1L), any(UpdateJobRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/employer/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Cập nhật tin tuyển dụng thành công"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title")
                        .value("Java Backend Developer Updated"))
                .andExpect(jsonPath("$.data.location")
                        .value("Ho Chi Minh"));
    }

    @Test
    @DisplayName("PUT /api/v1/employer/jobs/{id} - Invalid request should return 400")
    void updateJob_invalidRequest_shouldReturn400() throws Exception {

        UpdateJobRequest request = new UpdateJobRequest();
        request.setTitle("");
        request.setDescription("");
        request.setSalary(BigDecimal.valueOf(-1));
        request.setLocation("");
        request.setDeadline(LocalDateTime.now().minusDays(1));

        mockMvc.perform(put("/api/v1/employer/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.title")
                        .value("Tiêu đề công việc không được để trống"))
                .andExpect(jsonPath("$.data.description")
                        .value("Mô tả công việc không được để trống"))
                .andExpect(jsonPath("$.data.salary")
                        .value("Mức lương phải lớn hơn hoặc bằng 0"))
                .andExpect(jsonPath("$.data.location")
                        .value("Địa điểm làm việc không được để trống"))
                .andExpect(jsonPath("$.data.deadline")
                        .value("Hạn nộp hồ sơ phải sau thời điểm hiện tại"));
    }

    // ============ DELETE /api/v1/employer/jobs/{id} ============
    @Test
    @DisplayName("DELETE /api/v1/employer/jobs/{id} - Should return 200")
    void deleteJob_shouldReturn200() throws Exception {

        mockMvc.perform(delete("/api/v1/employer/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Xóa tin tuyển dụng thành công"));

        verify(employerJobService).deleteJob(1L);
    }

    // ============ GET /api/v1/employer/jobs/{id} ============
    @Test
    @DisplayName("GET /api/v1/employer/jobs/{id} - Should return job detail")
    void getJobDetail_shouldReturnJob() throws Exception {

        when(employerJobService.getJobDetail(1L))
                .thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/employer/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Lấy chi tiết tin tuyển dụng thành công"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title")
                        .value("Java Backend Developer"))
                .andExpect(jsonPath("$.data.location")
                        .value("Ha Noi"));
    }

    // ============ GET /api/v1/employer/jobs ============
    @Test
    @DisplayName("GET /api/v1/employer/jobs - Should return paginated job list")
    void getMyJobs_shouldReturnPage() throws Exception {

        Page<JobResponse> page = new PageImpl<>(List.of(sampleResponse));

        when(employerJobService.getMyJobs(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/employer/jobs")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Lấy danh sách tin tuyển dụng thành công"))
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].title")
                        .value("Java Backend Developer"))
                .andExpect(jsonPath("$.data.content[0].location")
                        .value("Ha Noi"));
    }
}