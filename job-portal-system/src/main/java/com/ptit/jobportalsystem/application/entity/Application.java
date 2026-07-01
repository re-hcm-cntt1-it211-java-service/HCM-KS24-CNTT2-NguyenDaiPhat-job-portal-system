package com.ptit.jobportalsystem.application.entity;

import com.ptit.jobportalsystem.cv.entity.CvFile;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_candidate_job",
                        columnNames = {"candidate_id", "job_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ứng viên ứng tuyển
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    /**
     * Công việc ứng tuyển
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    /**
     * CV được sử dụng để ứng tuyển
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private CvFile cv;

    /**
     * Thư giới thiệu
     */
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    /**
     * Trạng thái hồ sơ
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;
}