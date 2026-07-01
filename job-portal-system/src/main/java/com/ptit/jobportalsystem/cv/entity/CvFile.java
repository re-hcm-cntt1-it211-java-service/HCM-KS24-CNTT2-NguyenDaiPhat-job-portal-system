package com.ptit.jobportalsystem.cv.entity;

import com.ptit.jobportalsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cv_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Chủ sở hữu CV
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    /**
     * Tên file
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * URL trên Cloudinary
     */
    @Column(name = "file_url", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;

    /**
     * Public ID của Cloudinary
     */
    @Column(name = "public_id", nullable = false)
    private String publicId;

    /**
     * Xóa mềm
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}