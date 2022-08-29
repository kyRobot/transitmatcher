package com.kylemilner.takehome.lp.model;

import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Trip {
    private String pan;
    private Tap tapOn;
    private Tap tapOff;
    private TripType type;

    public Optional<Tap> getTapOff() {
        return Optional.ofNullable(tapOff);
    }
}
