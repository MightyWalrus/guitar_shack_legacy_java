package com.guitarshack;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.guitarshack.converter.MapToProductConverter;
import com.guitarshack.domain.LeadTime;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@WireMockTest
class StockMonitorIT {

    @Mock
    private Alert alert;
    @Mock
    private UriConfig uriConfig;
    @Mock
    private TimeProvider timeProvider;

    private StockMonitor underTest;

    @BeforeEach
    void setUp() {
        underTest = new StockMonitor(alert, uriConfig, timeProvider, new ProductRepository(uriConfig, new MapToProductConverter(), new CurlRequester()));
    }

    @Test
    void shouldSoldProdukt(WireMockRuntimeInfo wireMockRuntimeInfo) {

        // GIVEN
        int productId = 1;
        int quantity = 4;

        given(uriConfig.getProductUri()).willReturn(wireMockRuntimeInfo.getHttpBaseUrl());
        stubFor(WireMock.get("/default/product?id=1&").willReturn(ok("""
                {
                    "id": 1,
                    "stock": 2,
                    "leadTime": 3
                }""")));
        given(uriConfig.getSalesUri()).willReturn(wireMockRuntimeInfo.getHttpBaseUrl());
        stubFor(WireMock.get("/default/sales?productId=1&endDate=10/3/2022&action=total&startDate=9/3/2022&").willReturn(ok("""
                {
                    "total": 2
                }""")));
        given(timeProvider.now()).willReturn(Instant.parse("2022-10-03T18:35:24.00Z"));

        // THEN
        underTest.productSold(new ProductId(productId), quantity);

        // THEN
        Product expected = Product.builder()
                .productId(new ProductId(1))
                .stock(new Stock(2))
                .leadTime(new LeadTime(3))
                .build();
        verify(alert).send(expected);
    }
}