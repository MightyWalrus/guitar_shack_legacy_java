package com.guitarshack.infra;

import java.util.Set;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class CurlRequest {

  @NonNull
  String baseUri;
  @NonNull
  @Singular
  Set<RequestParameter> requestParameters;
}
