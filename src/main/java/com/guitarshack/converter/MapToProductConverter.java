package com.guitarshack.converter;

import com.guitarshack.domain.Product;
import com.guitarshack.domain.LeadTime;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.Stock;

import java.util.Map;

public class MapToProductConverter {

    public Product convert(Map<String, Object> map) {

        return Product.builder()
                .productId(new ProductId((int) (double) map.get("id")))
                .stock(new Stock((int) (double) map.get("stock")))
                .leadTime(new LeadTime((int) (double) map.get("leadTime")))
                .build();
    }
}
