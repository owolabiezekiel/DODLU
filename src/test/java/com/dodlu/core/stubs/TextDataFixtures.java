package com.dodlu.core.stubs;

import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.persistence.user.entity.User;

public class TextDataFixtures {
  public static final String DEFAULT_UUID_STRING = "default-uuid-string";
  public static final String DEFAULT_FACULTY_NAME = "Science";
  public static final String DEFAULT_USERNAME = "user1";
  public static final String DEFAULT_USER_EMAIL = "user1@email.com";

  public static CreateFacultyRequest buildCreateFacultyRequest() {
    return CreateFacultyRequest.builder()
        .name(DEFAULT_FACULTY_NAME)
        .deanId(DEFAULT_UUID_STRING)
        .build();
  }

  public static User buildUser() {
    return User.builder()
        .id(DEFAULT_UUID_STRING)
        .email(DEFAULT_USER_EMAIL)
        .username(DEFAULT_USERNAME)
        .build();
  }
}
