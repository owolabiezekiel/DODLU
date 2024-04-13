package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.model.StudentDocumentModel;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "student_documents")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class StudentDocuments {
  @Id @UuidGenerator() protected String id;

  @ManyToOne
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private StudentProfile student;

  private String resumeUrl;
  private String coverLetterUrl;
  private String transcriptUrl;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StudentDocuments that = (StudentDocuments) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public StudentDocumentModel toModel() {
    return StudentDocumentModel.builder()
        .id(id)
        .student(student == null ? null : student.toModel())
        .resumeUrl(resumeUrl)
        .coverLetterUrl(coverLetterUrl)
        .transcriptUrl(transcriptUrl)
        .build();
  }
}
