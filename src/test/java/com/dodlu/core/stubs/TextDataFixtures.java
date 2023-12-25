package com.dodlu.core.stubs;

import com.dodlu.core.app.model.request.department.CreateDepartmentRequest;
import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.user.entity.User;

public class TextDataFixtures {
  public static final String DEFAULT_UUID_STRING = "default-uuid-string";
  public static final String DEFAULT_FACULTY_NAME = "Science";
  public static final String DEFAULT_USERNAME = "user1";
  public static final String DEFAULT_USER_EMAIL = "user1@email.com";
  public static final String DEFAULT_DEPARTMENT_NAME = "Geography";

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

  public static Faculty buildFaculty(String name) {
    return Faculty.builder().id(DEFAULT_UUID_STRING).name(name).dean(buildUser()).build();
  }

  public static Faculty buildFaculty() {
    return Faculty.builder()
        .id(DEFAULT_UUID_STRING)
        .name(DEFAULT_FACULTY_NAME)
        .dean(buildUser())
        .build();
  }

  public static CreateDepartmentRequest buildCreateDepartmentRequest() {
    return CreateDepartmentRequest.builder()
        .name(DEFAULT_DEPARTMENT_NAME)
        .facultyId(DEFAULT_UUID_STRING)
        .build();
  }

  public static Department buildDepartment(String name) {
    return Department.builder()
        .id(DEFAULT_UUID_STRING)
        .name(name)
        .faculty(buildFaculty(DEFAULT_FACULTY_NAME))
        .build();
  }
}
