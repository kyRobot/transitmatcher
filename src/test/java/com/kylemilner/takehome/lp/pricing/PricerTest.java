package com.kylemilner.takehome.lp.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.Trip;
import com.kylemilner.takehome.lp.model.TripType;

@ExtendWith(MockitoExtension.class)
public class PricerTest {

    @InjectMocks
    Pricer pricer;

    @Mock
    TransitGraph busNetwork;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPriceFor_completedTripKnownEndpoints() {
        when(busNetwork.weightOfEdge("A", "B")).thenReturn(Optional.of(100));

        var trip = Trip.builder().type(TripType.COMPLETED).tapOn(Tap.builder().stopId("A").build())
                .tapOff(Tap.builder().stopId("B").build()).build();

        var price = pricer.priceFor(trip);

        assertTrue(price.isPresent());
        assertEquals(100, price.get().getAmount());
    }

    @Test
    void testPriceFor_completedTripUnknownTo() {
        when(busNetwork.weightOfEdge("A", "D")).thenReturn(Optional.empty());
        var trip = Trip.builder().type(TripType.COMPLETED).tapOn(Tap.builder().stopId("A").build())
                .tapOff(Tap.builder().stopId("D").build()).build();

        var price = pricer.priceFor(trip);

        assertTrue(price.isEmpty());
    }

    @Test
    void testPriceFor_cancelledTripIsFree() {
        var trip = Trip.builder().type(TripType.CANCELLED).tapOn(Tap.builder().stopId("E").build())
                .tapOff(Tap.builder().stopId("E").build()).build();

        var price = pricer.priceFor(trip);

        assertTrue(price.isPresent());
        assertEquals(0, price.get().getAmount());
    }

    @Test
    void testPriceFor_incompleteTripIsMaxFare() {
        when(busNetwork.edgeWeightsFrom("C")).thenReturn(Set.of(123, 999, 321));

        var trip = Trip.builder().type(TripType.INCOMPLETE).tapOn(Tap.builder().stopId("C").build())
                .build();

        var price = pricer.priceFor(trip);

        assertTrue(price.isPresent());
        assertEquals(999, price.get().getAmount());
    }

    @Test
    void testPriceFor_completedTripKnownEndpointsBothWaysEqual() {
        when(busNetwork.weightOfEdge("X", "Y")).thenReturn(Optional.of(1234));
        when(busNetwork.weightOfEdge("Y", "X")).thenReturn(Optional.of(1234));
        var tripXY =
                Trip.builder().type(TripType.COMPLETED).tapOn(Tap.builder().stopId("X").build())
                        .tapOff(Tap.builder().stopId("Y").build()).build();
        var tripYX =
                Trip.builder().type(TripType.COMPLETED).tapOn(Tap.builder().stopId("Y").build())
                        .tapOff(Tap.builder().stopId("X").build()).build();

        var priceXY = pricer.priceFor(tripXY);
        var priceYX = pricer.priceFor(tripYX);

        assertTrue(priceXY.isPresent());
        assertTrue(priceYX.isPresent());
        assertEquals(1234, priceXY.get().getAmount());
        assertEquals(1234, priceYX.get().getAmount());

    }
}
