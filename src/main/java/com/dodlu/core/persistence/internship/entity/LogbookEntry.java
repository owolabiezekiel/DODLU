package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.model.LogbookEntryModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "logbook_entries")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class LogbookEntry extends BaseEntity {
  //  @ManyToOne
  //  @JoinColumn(name = "logbook_id", referencedColumnName = "id")
  //  private Logbook logbook;
  private String logbookId;
  private String description;
  private LocalDateTime dateTime;
  private Boolean approved;
  private String comment;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    LogbookEntry that = (LogbookEntry) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public LogbookEntryModel toModel() {
    return LogbookEntryModel.builder()
        .id(id)
        .logbookId(logbookId)
        .description(description)
        .dateTime(dateTime)
        .approved(approved)
        .comment(comment)
        .build();
  }
}
