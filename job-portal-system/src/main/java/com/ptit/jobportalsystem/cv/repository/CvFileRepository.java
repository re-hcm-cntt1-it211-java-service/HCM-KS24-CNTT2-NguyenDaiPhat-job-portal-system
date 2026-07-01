package com.ptit.jobportalsystem.cv.repository;

import com.ptit.jobportalsystem.cv.entity.CvFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CvFileRepository extends JpaRepository<CvFile, Long> {

    Page<CvFile> findAllByCandidateIdAndIsDeletedFalse(Long candidateId, Pageable pageable);

    Optional<CvFile> findByIdAndIsDeletedFalse(Long id);

    List<CvFile> findAllByCandidateIdAndIsDeletedFalse(Long candidateId);
}