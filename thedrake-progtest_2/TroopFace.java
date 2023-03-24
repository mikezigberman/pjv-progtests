package thedrake;

public enum TroopFace {
    AVERS {
        @Override
        public TroopFace opposite() {
            return REVERS;
        }
    },
    REVERS {
        @Override
        public TroopFace opposite() {
            return AVERS;
        }
    };

    public abstract TroopFace opposite();
}

