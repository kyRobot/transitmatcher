package com.kylemilner.takehome.lp.pricing;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import com.kylemilner.takehome.lp.model.Price;

public class MoneyUtils {
    public static String formatPriceForCurrency(Price price, String currencyCode) {
        var currency = Currency.getInstance(currencyCode);
        var format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        format.setCurrency(currency);
        double withDecimals = ((double) (price.getAmount())) / 100;
        return format.format(withDecimals);
    }

    public static String formatPriceForDollars(Price price) {
        return formatPriceForCurrency(price, "USD");
    }
}
