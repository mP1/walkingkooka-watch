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

import java.util.List;
import java.util.function.Consumer;

/**
 * A {@link Runnable} that removes the first instance of the {@link Consumer}.
 */
final class WatchersWatcherRemovingRunnable<T> implements Runnable {

    static <T> WatchersWatcherRemovingRunnable<T> with(final Consumer<T> watcher,
                                                       final List<Consumer<T>> watchers) {
        return new WatchersWatcherRemovingRunnable<>(watcher, watchers);
    }

    private WatchersWatcherRemovingRunnable(final Consumer<T> watcher,
                                            final List<Consumer<T>> watchers) {
        super();
        this.watcher = watcher;
        this.watchers = watchers;
    }

    // Runnable.........................................................................................

    @Override
    public void run() {
        this.watchers.remove(this.watcher);
    }

    private final Consumer<T> watcher;
    private final List<Consumer<T>> watchers;

    @Override
    public String toString() {
        final Consumer<T> watcher = this.watcher;
        return watcher.toString()
                .concat(
                        this.watchers.contains(watcher) ?
                                " Active" :
                                " Removed"
                );
    }
}
