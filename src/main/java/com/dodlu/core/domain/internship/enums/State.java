package com.dodlu.core.domain.internship.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum State {
  ABIA,
  ADAMAWA,
  AKWA_IBOM,
  ANAMBRA,
  BAUCHI,
  BAYELSA,
  BENUE,
  BORNO,
  CROSS_RIVER,
  DELTA,
  EDO,
  EKITI,
  ENUGU,
  FCT,
  GOMBE,
  IMO,
  JIGAWA,
  KADUNA,
  KANO,
  KATSINA,
  KEBBI,
  KOGI,
  KWARA,
  LAGOS,
  NIGER,
  OGUN,
  ONDO,
  OSUN,
  OYO,
  PLATEAU,
  RIVERS,
  SOKOTO,
  TARABA,
  YOBE,
  ZAMFARA;

  @JsonCreator
  public static State create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(State.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(LAGOS);
  }
}
