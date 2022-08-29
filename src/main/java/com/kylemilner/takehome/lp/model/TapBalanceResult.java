package com.kylemilner.takehome.lp.model;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TapBalanceResult {
    private final List<TapPair> balanced = new ArrayList<>();
    private final List<Tap> unbalanced = new ArrayList<>();
}
