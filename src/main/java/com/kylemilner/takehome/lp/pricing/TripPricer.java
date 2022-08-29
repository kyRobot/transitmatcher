package com.kylemilner.takehome.lp.pricing;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kylemilner.takehome.lp.model.PricedTrip;
import com.kylemilner.takehome.lp.model.Trip;

@Service
public class TripPricer {

    @Autowired
    private Pricer pricer;

    public List<PricedTrip> priceTrips(List<Trip> trips) {
        return trips.stream().map(trip -> {
            var price = pricer.priceFor(trip);
            return new PricedTrip(trip, price);
        }).collect(Collectors.toList());
    }
}
