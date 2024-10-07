package com.dodlu.core.persistence.savings.repository;

import static com.dodlu.core.persistence.savings.entity.querydsl.QGroupSavingUser.groupSavingUser;
import static com.dodlu.core.persistence.user.entity.querydsl.QCustomerProfile.customerProfile;

import com.dodlu.core.persistence.savings.entity.GroupSavingUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GroupSavingUsersRepository
    extends JpaRepository<GroupSavingUser, String>, QuerydslPredicateExecutor<GroupSavingUser> {
  default Page<GroupSavingUser> getUserGroupSavings(String email, Pageable pageable) {
    BooleanExpression predicate =
        groupSavingUser
            .group
            .admin
            .email
            .equalsIgnoreCase(email)
            .or(
                JPAExpressions.selectFrom(customerProfile)
                    .where(
                        customerProfile
                            .email
                            .equalsIgnoreCase(email)
                            .and(customerProfile.in(groupSavingUser.group.members)))
                    .exists());

    return findAll(predicate, pageable);
  }
}
