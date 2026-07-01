package com.ptit.jobportalsystem.application.repository;

import com.ptit.jobportalsystem.application.entity.Application;
import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * Check duplicate apply
     */
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    /**
     * Find application by id with fetch (optional for service)
     */
    @Query("""
        SELECT a FROM Application a
        JOIN FETCH a.job
        JOIN FETCH a.candidate
        JOIN FETCH a.cv
        WHERE a.id = :id
    """)
    Optional<Application> findDetailById(@Param("id") Long id);

    /**
     * View My Applications (Candidate)
     */
    @Query("""
        SELECT a FROM Application a
        JOIN a.job j
        WHERE a.candidate.id = :candidateId
        AND j.isDeleted = false
    """)
    Page<Application> findMyApplications(@Param("candidateId") Long candidateId,
                                         Pageable pageable);

    /**
     * View Applicants by Job (Employer)
     */
    @Query("""
        SELECT a FROM Application a
        JOIN a.job j
        JOIN a.candidate c
        WHERE j.id = :jobId
        AND j.employer.id = :employerId
        AND j.isDeleted = false
    """)
    Page<Application> findApplicantsByJob(@Param("jobId") Long jobId,
                                          @Param("employerId") Long employerId,
                                          Pageable pageable);

    /**
     * View Applicants with search (keyword + status)
     */
    @Query("""
        SELECT a FROM Application a
        JOIN a.job j
        JOIN a.candidate c
        WHERE j.id = :jobId
        AND j.employer.id = :employerId
        AND j.isDeleted = false
        AND (:status IS NULL OR a.status = :status)
        AND (
            :keyword IS NULL
            OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Application> searchApplicants(@Param("jobId") Long jobId,
                                       @Param("employerId") Long employerId,
                                       @Param("keyword") String keyword,
                                       @Param("status") ApplicationStatus status,
                                       Pageable pageable);
}