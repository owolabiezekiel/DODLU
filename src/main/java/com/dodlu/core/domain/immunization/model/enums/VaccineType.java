package com.dodlu.core.domain.immunization.model.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum VaccineType {
  BCG("*BCG"),
  OPV0("**OPV0"),
  HEP_BO_BIRTH("***Hep BO birth"),

  PENTAVALENT1("Pentavalent(DPT, Hep B and Hib) 1"),
  PREMOCOCCAL1("Premococcal Conjugate Vaccine 1"),
  OPV1("OPV1"),
  IPV1("IPV1"),
  ROTAVIRUS1("Rotavirus vaccine 1"),

  PENTAVALENT2("Pentavalent (DPT, Hep B and Hib) 2"),
  PREMOCOCCAL2("Premococcal Conjugate Vaccine 2"),
  OPV2("OPV2"),
  ROTAVIRUS2("Rotavirus vaccine 2"),

  PENTAVALENT3("(DPT, Hep B and Hib) 3"),
  PREMOCOCCAL3("Premococcal Conjugate Vaccine 3"),
  OPV3("OPV3"),
  ROTAVIRUS3("Rotavirus vaccine 3"),
  IPV2("IPV2"),

  VITAMIN_A1("Vitamin A 1st done"),

  MEASLES1("Measles 1st dose (MCV1)"),
  YELLOW_FEVER("Yellow Fever"),
  MENINGITIS("Meningitis Vaccine"),

  VITAMIN_A2("Vitamin A 2nd done"),

  MEASLES2("Measles 2nd dose (MCV2)"),

  HPV("****HPV"),

  UNKNOWN("unknown");

  private final String name;

  VaccineType(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }

  @JsonCreator
  public static VaccineType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(VaccineType.values())
        .filter(e -> e.name().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
