package com.kylemilner.takehome.lp.balancing;

import java.util.List;
import com.kylemilner.takehome.lp.model.Tap;
import com.kylemilner.takehome.lp.model.TapBalanceResult;

public interface TapBalancer {

    public TapBalanceResult balance(List<Tap> taps);

}
