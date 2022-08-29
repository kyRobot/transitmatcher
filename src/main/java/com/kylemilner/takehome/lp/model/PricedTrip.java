package com.kylemilner.takehome.lp.model;

import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PricedTrip {
    private final Trip trip;
    private final Optional<Price> price;
}
