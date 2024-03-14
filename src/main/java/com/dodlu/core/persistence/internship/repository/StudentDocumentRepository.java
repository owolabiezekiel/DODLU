package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.StudentDocuments;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDocumentRepository extends JpaRepository<StudentDocuments, String> {
  Optional<StudentDocuments> findByStudent(StudentProfile student);
}
