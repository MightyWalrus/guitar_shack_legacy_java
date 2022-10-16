package com.guitarshack;

import com.guitarshack.domain.Product;

public interface Alert {
    void send(Product product);
}
