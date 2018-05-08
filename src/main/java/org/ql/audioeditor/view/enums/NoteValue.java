package org.ql.audioeditor.view.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Common note values.
 */
public enum NoteValue {
    HALF(2), QUARTER(4), EIGTH(8), SIXTEENTH(16);

    private final int value;

    NoteValue(int value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * @return Value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns all values.
     * @return All values
     */
    public static List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        for (NoteValue nv: NoteValue.values()) {
            values.add(nv.getValue());
        }
        return values;
    }
}
