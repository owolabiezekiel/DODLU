package com.dodlu.core.persistence.discussion.entity;

import com.dodlu.core.domain.discussion.model.CommentModel;
import com.dodlu.core.persistence.base.LongBaseEntity;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import jakarta.persistence.*;
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
@Table(name = "comments")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Comment extends LongBaseEntity {
  @Column(nullable = false)
  private String comment;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private CustomerProfile author;

  @ManyToOne
  @JoinColumn(name = "discussion_id", nullable = false)
  private Discussion discussion;

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public CommentModel toModel() {
    return CommentModel.builder().id(id).discussionId(discussion.getId()).comment(comment).build();
  }
}
