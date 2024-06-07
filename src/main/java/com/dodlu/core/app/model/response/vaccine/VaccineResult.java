package com.dodlu.core.app.model.response.vaccine;

import com.dodlu.core.domain.immunization.model.VaccineModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class VaccineResult extends PageImpl<VaccineModel> {
  public VaccineResult(Page<VaccineModel> pagedContent) {
    super(pagedContent.getContent(), pagedContent.getPageable(), pagedContent.getTotalElements());
  }
}
