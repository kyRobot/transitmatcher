package com.kylemilner.takehome.lp.matching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.kylemilner.takehome.lp.balancing.PanTapBalancer;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;
import com.kylemilner.takehome.lp.model.TapPair;
import com.kylemilner.takehome.lp.model.TapType;

public class PanTapBalancerTest {

    private PanTapBalancer balancer = new PanTapBalancer();

    private Tap on(String by, String at) {
        return Tap.builder().tapType(TapType.ON).pan(by).stopId(at).dateTimeUTC(LocalDateTime.now())
                .build();
    }

    private Tap off(String by, String at) {
        return Tap.builder().tapType(TapType.OFF).pan(by).stopId(at)
                .dateTimeUTC(LocalDateTime.now()).build();
    }

    @Test
    void testBalance_emptyInEmptyOut() {
        assertEquals(new TapBalanceResult(), balancer.balance(List.of()));
    }

    @Test
    void testBalance_singleTapOnIsUnbalanced() {
        var tap = on("123", "S1");
        var result = balancer.balance(List.of(tap));
        assertTrue(result.getBalanced().isEmpty());
        assertFalse(result.getUnbalanced().isEmpty());
        assertEquals(1, result.getUnbalanced().size());
        assertEquals(tap, result.getUnbalanced().get(0));
    }

    @Test
    void testBalance_NTapOnsIsNUnbalanced() {
        var pan = "1";
        var stop = "S1";
        var taps = List.of(on(pan, stop), on(pan, stop), on(pan, stop));
        var result = balancer.balance(taps);
        var incompletes = result.getUnbalanced();
        assertTrue(result.getBalanced().isEmpty());
        assertFalse(incompletes.isEmpty());
        assertEquals(taps.size(), result.getUnbalanced().size());
    }

    @Test
    void testBalance_singleTapOffIsUnbalanced() {
        var tap = off("123", "S1");
        assertEquals(1, balancer.balance(List.of(tap)).getUnbalanced().size());
    }

    @Test
    void testBalance_sameStopTapOnOff() {
        var pan = "123";
        var stop = "S1";
        var on = on(pan, stop);
        var off = off(pan, stop);
        var result = balancer.balance(List.of(on, off));
        assertFalse(result.getBalanced().isEmpty());
        assertEquals(1, result.getBalanced().size());
        assertEquals(new TapPair(on, off), result.getBalanced().get(0));
    }

    @Test
    void testBalance_differentStopTapOnTap() {
        var pan = "123";
        var on = on(pan, "S1");
        var off = off(pan, "S2");
        var result = balancer.balance(List.of(on, off));
        assertFalse(result.getBalanced().isEmpty());
        assertEquals(1, result.getBalanced().size());
        assertEquals(new TapPair(on, off), result.getBalanced().get(0));
    }

    @Test
    void testBalance_NdifferentStopTapOnTapOffIsMultiBalanced() {
        var pan = "123";
        var on = on(pan, "s1");
        var off = off(pan, "s2");
        var taps = List.of(on, off, on, off);
        var result = balancer.balance(taps);
        assertFalse(result.getBalanced().isEmpty());
        assertEquals(taps.size() / 2, result.getBalanced().size());
        for (TapPair pair : result.getBalanced()) {
            assertEquals(pair.getOn(), on);
            assertEquals(pair.getOff(), off);
        }
    }
}

