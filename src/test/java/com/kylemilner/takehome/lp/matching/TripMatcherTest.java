package com.kylemilner.takehome.lp.matching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;
import com.kylemilner.takehome.lp.model.TapPair;
import com.kylemilner.takehome.lp.model.TapType;
import com.kylemilner.takehome.lp.model.Trip;
import com.kylemilner.takehome.lp.model.TripType;

public class TripMatcherTest {

    TripMatcher matcher = new TripMatcher();

    private Tap on(String by, String at) {
        return Tap.builder().tapType(TapType.ON).pan(by).stopId(at).dateTimeUTC(LocalDateTime.now())
                .build();
    }

    private Tap off(String by, String at) {
        return Tap.builder().tapType(TapType.OFF).pan(by).stopId(at)
                .dateTimeUTC(LocalDateTime.now()).build();
    }

    @Test
    void testMatching_emptyInEmptyOut() {
        assertTrue(matcher.matchTripsFromTaps(new TapBalanceResult()).isEmpty());
    }

    @Test
    void testMatching_singleTapOnIsIncomplete() {
        var taps = new TapBalanceResult();
        taps.getUnbalanced().add(on("123", "S1"));
        var trips = matcher.matchTripsFromTaps(taps);
        assertFalse(trips.isEmpty());
        assertEquals(1, trips.size());
        assertEquals(TripType.INCOMPLETE, trips.get(0).getType());
    }

    @Test
    void testMatching_NTapOnsIsNIncomplete() {
        var pan = "1";
        var stop = "S1";
        var taps = new TapBalanceResult();
        taps.getUnbalanced().addAll(List.of(on(pan, stop), on(pan, stop), on(pan, stop)));
        var trips = matcher.matchTripsFromTaps(taps);
        assertFalse(trips.isEmpty());
        assertEquals(taps.getUnbalanced().size(), trips.size());
        assertEquals(TripType.INCOMPLETE, trips.get(0).getType());
        assertEquals(TripType.INCOMPLETE, trips.get(1).getType());
        assertEquals(TripType.INCOMPLETE, trips.get(2).getType());
    }

    @Test
    void testMatching_MixedTapsIsCorrectIncomplete() {
        var pan = "1";
        var stop = "S1";
        var taps = new TapBalanceResult();
        taps.getUnbalanced().addAll(List.of(on(pan, stop), on(pan, stop), off(pan, "s2")));
        var trips = matcher.matchTripsFromTaps(taps);

        assertFalse(trips.isEmpty());
        assertEquals(2, trips.size());
        assertEquals(TripType.INCOMPLETE, trips.get(0).getType());
        assertEquals(TripType.INCOMPLETE, trips.get(1).getType());
    }

    @Test
    void testMatching_singleTapOffIsEmpty() {
        var taps = new TapBalanceResult();
        taps.getUnbalanced().add(off("123", "S1"));
        assertTrue(matcher.matchTripsFromTaps(taps).isEmpty());
    }


    @Test
    void testMatching_sameStopTapOnOffIsCancelled() {
        var pan = "123";
        var stop = "S1";
        var on = on(pan, stop);
        var off = off(pan, stop);
        var taps = new TapBalanceResult();
        taps.getBalanced().add(new TapPair(on, off));
        var trips = matcher.matchTripsFromTaps(taps);

        assertFalse(trips.isEmpty());
        assertEquals(1, trips.size());
        assertEquals(TripType.CANCELLED, trips.get(0).getType());
    }

    @Test
    void testMatching_differentStopTapOnTapOffIsComplete() {
        var pan = "123";
        var on = on(pan, "S1");
        var off = off(pan, "S2");
        var taps = new TapBalanceResult();
        taps.getBalanced().add(new TapPair(on, off));
        var trips = matcher.matchTripsFromTaps(taps);

        assertFalse(trips.isEmpty());
        assertEquals(1, trips.size());
        assertEquals(TripType.COMPLETED, trips.get(0).getType());
    }

    @Test
    void testMatching_NdifferentStopTapOnTapOffIsMultiCompletes() {
        var pan = "123";
        var taps = new TapBalanceResult();
        taps.getBalanced().add(new TapPair(on(pan, "S1"), off(pan, "S2")));
        taps.getBalanced().add(new TapPair(on(pan, "S3"), off(pan, "S4")));

        var trips = matcher.matchTripsFromTaps(taps);
        assertFalse(trips.isEmpty());
        assertEquals(2, trips.size());
        for (Trip trip : trips) {
            assertEquals(TripType.COMPLETED, trip.getType());
        }
    }

    @Test
    void testMatching_MixedTrips() {
        var pan = "123";
        var taps = new TapBalanceResult();
        taps.getBalanced().add(new TapPair(on(pan, "S1"), off(pan, "S2")));
        taps.getBalanced().add(new TapPair(on(pan, "S5"), off(pan, "S5")));
        taps.getBalanced().add(new TapPair(on(pan, "S3"), off(pan, "S4")));
        taps.getUnbalanced().add(on(pan, "S6"));
        taps.getUnbalanced().add(off(pan, "S6"));

        var trips = matcher.matchTripsFromTaps(taps);
        assertFalse(trips.isEmpty());
        assertEquals(4, trips.size());
    }
}
