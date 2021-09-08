package fastcampus.spring.batch.part4;

import java.util.Objects;

public enum Level {

    VIP(500_000, null),
    GOLD(500_000, VIP),
    SILVER(300_000, GOLD),
    NORMAL(200_000, SILVER);

    private final int nextAmount;
    private final Level nextLevel;

    Level(int nextAmount, Level nextLevel) {
        this.nextAmount = nextAmount;
        this.nextLevel = nextLevel;
    }

    protected static boolean availableLevelUp(Level level, int totalAmount) {
        if (Objects.isNull(level)) {
            return false;
        }

        if (Objects.isNull(level.nextLevel)) {
            return false;
        }

        return totalAmount >= level.nextAmount;
    }

    protected static Level getNextLevel(int totalAmount) {
        if (totalAmount >= Level.VIP.nextAmount) {
            return VIP;
        }
        if (totalAmount >= Level.GOLD.nextAmount) {
            return GOLD.nextLevel; // VIP
        }
        if (totalAmount >= Level.SILVER.nextAmount) {
            return SILVER.nextLevel; // GOLD
        }
        if (totalAmount >= Level.NORMAL.nextAmount) {
            return NORMAL.nextLevel;
        }

        return NORMAL;
    }
}
