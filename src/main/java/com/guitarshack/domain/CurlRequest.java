package com.guitarshack.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class CurlRequest {

    @NonNull
    String baseUri;
    @NonNull
    @Singular
    Set<RequestParameter> requestParameters;
}
