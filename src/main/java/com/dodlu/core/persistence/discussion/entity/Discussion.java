package com.dodlu.core.persistence.discussion.entity;

import com.dodlu.core.domain.discussion.model.DiscussionModel;
import com.dodlu.core.persistence.base.LongBaseEntity;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
@Table(name = "discussions")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Discussion extends LongBaseEntity {
  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String body;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private CustomerProfile author;

  @ManyToOne
  @JoinColumn(name = "group_id", nullable = false)
  private GroupSaving group;

  @OneToMany(
      mappedBy = "discussion",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private Set<Comment> comments = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public DiscussionModel toModel() {
    return DiscussionModel.builder()
        .id(id)
        .title(title)
        .body(body)
        .authorId(author.getId())
        .authorName(author.getFullName())
        .comments(comments.stream().map(Comment::toModel).collect(Collectors.toSet()))
        .createdAt(getCreatedAt())
        .build();
  }
}
