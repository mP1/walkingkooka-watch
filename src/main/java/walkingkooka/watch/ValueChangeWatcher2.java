/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
