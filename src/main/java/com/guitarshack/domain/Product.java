package com.guitarshack.domain;

import com.guitarshack.domain.LeadTime;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.Stock;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class Product {

    @NonNull
    ProductId productId;
    @NonNull
    Stock stock;
    @NonNull
    LeadTime leadTime;
    // optional
    String description;

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }
}
