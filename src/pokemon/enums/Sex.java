package pokemon.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum Sex {
	MALE,
	FEMALE;
	
	private static final Sex[] VALUES = values();

    public static Sex random() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
