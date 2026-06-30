package com.ptit.jobportalsystem.job.repository;

import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByIdAndIsDeletedFalse(Long id);

    Page<Job> findAllByEmployerIdAndIsDeletedFalse(
            Long employerId,
            Pageable pageable
    );

    Page<Job> findAllByStatusAndIsDeletedFalse(
            JobStatus status,
            Pageable pageable
    );

    Page<Job> findAllByIsDeletedFalse(Pageable pageable);


    Optional<Job> findByIdAndStatusAndIsDeletedFalse(
            Long id,
            JobStatus status
    );

//    Sau này Employer sẽ thường xuyên sửa/xóa Job của chính mình. Thay vì mỗi lần lấy findByIdAndIsDeletedFalse() rồi tự so sánh employerId,
    Optional<Job> findByIdAndEmployerIdAndIsDeletedFalse(
            Long id,
            Long employerId
    );
}