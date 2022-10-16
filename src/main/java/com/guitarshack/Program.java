package com.guitarshack;

import com.guitarshack.converter.MapToProductConverter;
import com.guitarshack.domain.ProductId;
import com.guitarshack.domain.ProductRepository;
import com.guitarshack.infra.RequestParameter;
import com.guitarshack.infra.CurlRequester;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

public class Program {

    private static StockMonitor monitor;

    static {
        UriConfig uriConfig = new UriConfig();

        monitor = new StockMonitor(product -> {
            // We are faking this for now
            System.out.println(
                    "You need to reorder product " + product.getProductId() +
                            ". Only " + product.getStock() + " remaining in stock");
        }, uriConfig, new TimeProvider(), new ProductRepository(uriConfig, new MapToProductConverter(), new CurlRequester()));
    }

    public static void main(String[] args) {
        int productId = Integer.parseInt(args[0]);
        int quantity = Integer.parseInt(args[1]);

        monitor.productSold(new ProductId(productId), quantity);
    }

}
