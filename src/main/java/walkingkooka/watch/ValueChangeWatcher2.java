package walkingkooka.watch;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ValueChangeWatcher} that routes each event to add/remove/update.
 */
public interface ValueChangeWatcher2<T> extends ValueChangeWatcher<T> {

    @Override
    default void onValueChange(final Optional<T> oldValue,
                               final Optional<T> newValue) {
        Objects.requireNonNull(oldValue, "oldValue");
        Objects.requireNonNull(newValue, "newValue");

        final boolean oldEmpty = oldValue.isEmpty();
        final boolean newEmpty = newValue.isEmpty();

        if (oldEmpty) {
            this.onValueChangeAdd(
                newValue.get()
            );
        } else {
            if (newEmpty) {
                this.onValueChangeRemove(
                    oldValue.get()
                );
            } else {
                this.onValueChangeReplace(
                    oldValue.get(),
                    newValue.get()
                );
            }
        }
    }

    void onValueChangeAdd(final T value);

    void onValueChangeRemove(final T name);

    void onValueChangeReplace(final T oldValue,
                              final T newValue);
}
