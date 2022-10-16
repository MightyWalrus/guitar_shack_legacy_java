package com.guitarshack.domain;

import lombok.NonNull;
import lombok.Value;

@Value
public class Stock {
    
    @NonNull
    Integer value;
}
