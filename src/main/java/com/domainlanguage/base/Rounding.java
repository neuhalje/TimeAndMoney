package com.domainlanguage.base;

import java.math.*;

public interface Rounding {
    int CEILING = BigDecimal.ROUND_CEILING;
    int UP = BigDecimal.ROUND_UP;
    int DOWN = BigDecimal.ROUND_DOWN;
    int FLOOR = BigDecimal.ROUND_FLOOR;
    int HALF_UP = BigDecimal.ROUND_HALF_UP;
    int HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
    int HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;
    int UNNECESSARY = BigDecimal.ROUND_UNNECESSARY;
}