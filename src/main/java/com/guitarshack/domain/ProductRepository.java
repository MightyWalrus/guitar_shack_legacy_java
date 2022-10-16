package com.guitarshack.domain;

import com.guitarshack.UriConfig;
import com.guitarshack.converter.MapToProductConverter;
import com.guitarshack.infra.CurlRequest;
import com.guitarshack.infra.CurlRequester;
import com.guitarshack.infra.RequestParameter;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepository {

  private final UriConfig uriConfig;
  private final MapToProductConverter mapToProductConverter;
  private final CurlRequester curlRequester;

  public Product read(ProductId productId) {

    CurlRequest curlRequest = CurlRequest.builder()
        .baseUri(uriConfig.getProductUri() + "/default/product")
        .requestParameter(new RequestParameter("id", productId.getValue().toString()))
        .build();
    Map<String, Object> map = curlRequester.read(curlRequest);
    return mapToProductConverter.convert(map);
  }

}
