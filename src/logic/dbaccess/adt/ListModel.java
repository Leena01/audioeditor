package logic.dbaccess.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ListModel<T> {
    @NotNull
    protected List<T> items;

    public ListModel() {
        this.items = new ArrayList<>();
    }

    @NotNull List<T> getItems() {
        return items;
    }

    public void setSongs(List<T> items) {
        this.items = items;
    }
}
