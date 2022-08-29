package com.kylemilner.takehome.lp.model;

import java.util.Currency;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Price {
    private final Integer amount;
}
