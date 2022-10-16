package com.guitarshack.infra;

import lombok.NonNull;
import lombok.Value;

@Value
public class RequestParameter {

  @NonNull
  String name;
  @NonNull
  String value;
}
