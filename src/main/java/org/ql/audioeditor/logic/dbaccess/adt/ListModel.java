package org.ql.audioeditor.logic.dbaccess.adt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * List model.
 * @param <T> Type to encapsulate
 */
public abstract class ListModel<T> {
    @NotNull
    protected List<T> items;

    public ListModel() {
        this.items = new ArrayList<>();
    }

    @NotNull
    public List<T> getItems() {
        return items;
    }

    public void setSongs(List<T> items) {
        this.items = items;
    }
}
