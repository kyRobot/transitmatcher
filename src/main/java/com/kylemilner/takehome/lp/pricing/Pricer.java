package com.kylemilner.takehome.lp.pricing;

import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kylemilner.takehome.lp.model.Price;
import com.kylemilner.takehome.lp.model.Trip;

@Service
public class Pricer {

    private static final Optional<Price> FREE = Optional.of(new Price(0));
    private static final Optional<Price> UNPRICED = Optional.empty();

    @Autowired
    private TransitGraph busNetwork;

    private Function<Trip, Optional<Price>> maxPriceForTrip =
            t -> busNetwork.edgeWeightsFrom(t.getTapOn().getStopId()).stream().max(Integer::compare)
                    .map(max -> new Price(max));

    private Function<Trip, Optional<Price>> completedTripPrice =
            t -> busNetwork.weightOfEdge(t.getTapOn().getStopId(), t.getTapOff().get().getStopId())
                    .map(weight -> new Price(weight));

    public Optional<Price> priceFor(Trip trip) {
        switch (trip.getType()) {
            case CANCELLED:
                return FREE;
            case COMPLETED:
                return completedTripPrice.apply(trip);
            case INCOMPLETE:
                return maxPriceForTrip.apply(trip);
            default:
                return UNPRICED;
        }
    }

}
