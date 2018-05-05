package org.ql.audioeditor.logic.dbaccess.adt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * List model that encapsulates a list.
 *
 * @param <T> Type to encapsulate
 */
public abstract class ListModel<T> {
    /**
     * Items of type T.
     */
    @NotNull
    protected List<T> items;

    /**
     * Constructor.
     */
    public ListModel() {
        this.items = new ArrayList<>();
    }

    /**
     * Getter for items.
     *
     * @return items
     */
    @NotNull
    public List<T> getItems() {
        return items;
    }

    /**
     * Setter for items.
     *
     * @param items List of items
     */
    public void setSongs(List<T> items) {
        this.items = items;
    }
}
