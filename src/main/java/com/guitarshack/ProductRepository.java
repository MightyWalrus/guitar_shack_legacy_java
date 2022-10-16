package com.guitarshack;

import com.guitarshack.converter.MapToProductConverter;
import com.guitarshack.domain.CurlRequest;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.RequestParameter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

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
