package com.kylemilner.takehome.lp.balancing;

import static java.util.stream.Collectors.groupingBy;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.stereotype.Service;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;
import com.kylemilner.takehome.lp.model.TapPair;
import com.kylemilner.takehome.lp.model.TapType;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PanTapBalancer implements TapBalancer {

    @Override
    public TapBalanceResult balance(List<Tap> tapsToBalance) {
        TapBalanceResult result = new TapBalanceResult();
        tapsToBalance.stream().collect(groupingBy(Tap::getPan)).forEach((p, taps) -> {
            Queue<Tap> open = new LinkedList<>();
            for (Tap tap : taps) {
                TapType tapType = tap.getTapType();
                if (tapType.equals(TapType.ON)) {
                    open.add(tap);
                } else {
                    Tap on = open.poll();
                    if (on != null) {
                        result.getBalanced().add(new TapPair(on, tap));
                    } else {
                        result.getUnbalanced().add(tap);
                    }
                }
            }
            open.stream().forEach(result.getUnbalanced()::add);
        });
        return result;
    }

}
