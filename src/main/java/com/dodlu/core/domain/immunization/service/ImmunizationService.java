package com.dodlu.core.domain.immunization.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.immunization.CreateChildRequest;
import com.dodlu.core.app.model.request.immunization.CreateParentRequest;
import com.dodlu.core.app.model.request.immunization.SearchAttributes;
import com.dodlu.core.domain.immunization.model.ChildWithParentModel;
import com.dodlu.core.domain.immunization.model.ParentModel;
import com.dodlu.core.persistence.immunization.entity.Child;
import com.dodlu.core.persistence.immunization.entity.Parent;
import com.dodlu.core.persistence.immunization.repository.ChildRepository;
import com.dodlu.core.persistence.immunization.repository.ParentRepository;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImmunizationService {
  private final ParentRepository parentRepository;
  private final ChildRepository childRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<ParentModel> createParent(CreateParentRequest request) {
    return supplyAsync(
        () -> {
          doCreateParentValidation(request);
          Parent parent = buildParentFromRequest(request);
          return parentRepository.save(parent).toModel(null);
        });
  }

  @Transactional
  public CompletableFuture<ParentModel> getSingleParent(SearchAttributes searchAttributes) {
    return supplyAsync(
        () -> {
          if (searchAttributes.getId() != null) {
            Parent parent =
                parentRepository
                    .findById(searchAttributes.getId())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          if (searchAttributes.getEmail() != null) {
            Parent parent =
                parentRepository
                    .findByEmailEqualsIgnoreCase(searchAttributes.getEmail())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          if (searchAttributes.getPhoneNumber() != null) {
            Parent parent =
                parentRepository
                    .findByPhoneNumberEquals(searchAttributes.getPhoneNumber())
                    .orElseThrow(
                        () ->
                            exceptionService.handleCreateNotFoundException(
                                "Parent details does not exist"));
            Set<Child> children = childRepository.findAllByParentId(parent.getId());
            return parent.toModel(children);
          }
          throw new IllegalArgumentException(
              "A query parameter for id, email, or phoneNumber is required.");
        });
  }

  @Transactional
  public CompletableFuture<ChildWithParentModel> getSingleChild(String childId) {
    return supplyAsync(
        () -> {
          Child child =
              childRepository
                  .findById(childId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException(
                              "Child details does not exist"));

          return child.toChildWithParentModel();
        });
  }

  @Transactional
  public CompletableFuture<ParentModel> createChild(String parentId, CreateChildRequest request) {
    return supplyAsync(
        () -> {
          Parent parent =
              parentRepository
                  .findById(parentId)
                  .orElseThrow(
                      () -> exceptionService.handleCreateBadRequest("Parent does not exist"));
          Set<Child> children = childRepository.findAllByParentId(parent.getId());

          Child child = buildChildFromRequest(request);

          ensureNoChildDuplicate(children, child);

          child.setParent(parent);
          child = childRepository.save(child);
          children.add(child);
          return parent.toModel(children);
        });
  }

  private void ensureNoChildDuplicate(Set<Child> children, Child child) {
    for (Child c : children) {
      if (c.getFirstName().equalsIgnoreCase(child.getFirstName())
          && c.getLastName().equalsIgnoreCase(child.getLastName())
          && c.getDateOfBirth().equals(child.getDateOfBirth())) {
        throw exceptionService.handleCreateBadRequest(
            "This is a duplicate record. Child already exists with parent");
      }
    }
  }

  private Parent buildParentFromRequest(CreateParentRequest request) {
    return Parent.builder()
        .title(request.getTitle())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .phoneNumber(request.getPhoneNumber())
        .email(request.getEmail().toLowerCase())
        .build();
  }

  private Child buildChildFromRequest(CreateChildRequest request) {
    return Child.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .gender(request.getGender())
        .dateOfBirth(request.getDateOfBirth())
        .build();
  }

  private void doCreateParentValidation(CreateParentRequest request) {
    ensureParentEmailIsUnique(request.getEmail());
    ensureParentPhoneIsUnique(request.getPhoneNumber());
  }

  private void ensureParentPhoneIsUnique(String phoneNumber) {
    if (parentRepository.existsByPhoneNumberEqualsIgnoreCase(phoneNumber)) {
      throw exceptionService.handleCreateBadRequest(
          "A parent with this phone number already exists");
    }
  }

  private void ensureParentEmailIsUnique(String email) {
    if (parentRepository.existsByEmailEqualsIgnoreCase(email)) {
      throw exceptionService.handleCreateBadRequest("A parent with this email already exists");
    }
  }
}
