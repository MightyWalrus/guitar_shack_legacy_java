package com.guitarshack;

import com.google.gson.Gson;
import com.guitarshack.domain.Product;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@RequiredArgsConstructor
public class StockMonitor {
    private final Alert alert;
    private final UriConfig uriConfig;
    private final TimeProvider timeProvider;
    private final ProductRepository productRepository;

    public void productSold(ProductId productId, int quantity) {

        Product product = productRepository.read(productId);

        ZonedDateTime zdt = ZonedDateTime.ofInstant(timeProvider.now(), ZoneOffset.UTC);
        Calendar calendar = GregorianCalendar.from(zdt);

        calendar.setTime(calendar.getTime());
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE, -30);
        Date startDate = calendar.getTime();
        DateFormat format = new SimpleDateFormat("M/d/yyyy");
        Map<String, Object> params1 = new HashMap<>() {{
            put("productId", product.getProductId().getValue());
            put("startDate", format.format(startDate));
            put("endDate", format.format(endDate));
            put("action", "total");
        }};
        String paramString1 = "?";

        for (String key : params1.keySet()) {
            paramString1 += key + "=" + params1.get(key).toString() + "&";
        }
        HttpRequest request1 = HttpRequest
                .newBuilder(URI.create(uriConfig.getSalesUri() + "/default/sales" + paramString1))
                .build();
        String result1 = "";
        HttpClient httpClient1 = HttpClient.newHttpClient();
        HttpResponse<String> response1 = null;
        try {
            response1 = httpClient1.send(request1, HttpResponse.BodyHandlers.ofString());
            result1 = response1.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        SalesTotal total = new Gson().fromJson(result1, SalesTotal.class);

        if (product.getStock().getValue() - quantity <= (int) ((double) (total.getTotal() / 30) * product.getLeadTime().getValue()))
            alert.send(product);
    }

}
