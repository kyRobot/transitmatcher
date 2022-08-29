package com.kylemilner.takehome.lp.matching;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;
import com.kylemilner.takehome.lp.model.TapPair;
import com.kylemilner.takehome.lp.model.TapType;
import com.kylemilner.takehome.lp.model.Trip;
import com.kylemilner.takehome.lp.model.TripType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TripMatcher {

    private final Comparator<Trip> byTapOnDate = (t1, t2) -> {
        return t1.getTapOn().getDateTimeUTC().compareTo(t2.getTapOn().getDateTimeUTC());
    };

    private final Consumer<Trip> trace = t -> {
        String on = t.getTapOn().getStopId();
        String off = t.getTapOff().map(Tap::getStopId).orElse("");
        log.debug("Matched {} Trip: {} {}->{}", t.getType(), t.getPan(), on, off);
    };

    private Function<TapPair, Trip> balancedTapsMatcher = (tapPair) -> {
        Tap on = tapPair.getOn();
        Tap off = tapPair.getOff();
        if (on.getStopId().equals(off.getStopId())) {
            return cancelledTrip(on, off);
        } else {
            return completedTrip(on, off);
        }
    };

    public List<Trip> matchTripsFromTaps(TapBalanceResult taps) {
        Stream<Trip> finishedTrips = taps.getBalanced().stream().map(balancedTapsMatcher);
        Stream<Trip> incompleteTrips = taps.getUnbalanced().stream()
                .filter(t -> t.getTapType().equals(TapType.ON)).map(this::incompleteTrip);

        List<Trip> matchedTrips = Stream.concat(finishedTrips, incompleteTrips).sorted(byTapOnDate)
                .peek(trace).collect(Collectors.toList());

        return matchedTrips;
    }

    private Trip cancelledTrip(Tap on, Tap off) {
        return Trip.builder().pan(on.getPan()).tapOn(on).tapOff(off).type(TripType.CANCELLED)
                .build();
    }

    private Trip completedTrip(Tap on, Tap off) {
        return Trip.builder().pan(on.getPan()).tapOn(on).tapOff(off).type(TripType.COMPLETED)
                .build();
    }

    private Trip incompleteTrip(Tap on) {
        return Trip.builder().pan(on.getPan()).tapOn(on).type(TripType.INCOMPLETE).build();
    }


}
