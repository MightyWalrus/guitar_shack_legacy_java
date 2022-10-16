package com.guitarshack.converter;

import com.guitarshack.Product;
import com.guitarshack.domain.LeadTime;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapToProductConverterTest {

    private MapToProductConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapToProductConverter();
    }

    @Test
    void shouldConvert() {

        // GIVEN
        Map<String, Object> input = Map.of(
                "id", 1d,
                "stock", 2d,
                "leadTime", 3d
        );

        // WHEN
        Product result = underTest.convert(input);

        // THEN
        assertThat(result).isEqualTo(Product.builder()
                .productId(new ProductId(1))
                .stock(new Stock(2))
                .leadTime(new LeadTime(3))
                .build());
    }
}