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

import java.util.function.Consumer;

/**
 * Wrapper that adds no behaviour but is detected by {@link Watchers} during a fire and removes the watcher supporting
 * a one-time {@link Consumer}.
 */
final class WatchersOnceConsumer<T> implements Consumer<T> {

    static <T> WatchersOnceConsumer<T> with(final Consumer<T> watcher) {
        return new WatchersOnceConsumer<>(watcher);
    }

    private WatchersOnceConsumer(final Consumer<T> watcher) {
        this.watcher = watcher;
    }


    @Override
    public void accept(final T t) {
        this.watcher.accept(t);
    }

    private final Consumer<T> watcher;

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
