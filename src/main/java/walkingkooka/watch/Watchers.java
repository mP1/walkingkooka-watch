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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstraction that supports collecting and dispatching events. If a watcher is added multiple times it will be fired each time.
 */
public final class Watchers<T> implements Consumer<T> {

    /**
     * {@see RunnableCollection}
     */
    public static Runnable runnableCollection(final List<Runnable> runnables) {
        return RunnableCollection.with(runnables);
    }

    /**
     * Creates an empty {@link Watchers}
     */
    public static <T> Watchers<T> create() {
        return new Watchers<>();
    }

    private Watchers() {
        super();
    }

    /**
     * Adds a new watcher which may be removed by calling the returned {@link Runnable}.
     */
    public Runnable add(final Consumer<T> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.add0(watcher);
    }

    /**
     * Adds a new watcher which will be removed after a single fire.
     */
    public Runnable addOnce(final Consumer<T> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.add0(
            WatchersOnceConsumer.with(watcher)
        );
    }

    private Runnable add0(final Consumer<T> watcher) {
        final List<Consumer<T>> watchers = this.watchers;

        watchers.add(watcher);
        return WatchersWatcherRemovingRunnable.with(
            watcher,
            watchers
        );
    }

    /**
     * Remove any {@link Consumer} watchers that were added using {@link #addOnce(Consumer)}.
     */
    public void removeOnce() {
        final List<Consumer<T>> watchers = this.watchers;
        int i = 0;

        for (final Consumer<T> watcher : watchers) {
            if (watcher instanceof WatchersOnceConsumer) {
                watchers.remove(i);
                i--;
            }
            i++;
        }
    }

    /**
     * Fire an event to all watchers.
     */
    @Override
    public void accept(final T source) {
        Objects.requireNonNull(source, "source");

        final List<Consumer<T>> watchers = this.watchers;

        final List<Consumer<T>> fire = Lists.array();

        int i = 0;

        // make a copy of all watches removing any one time watchers along the way.
        for (final Consumer<T> watcher : watchers) {
            fire.add(watcher);
            if (watcher instanceof WatchersOnceConsumer) {
                watchers.remove(i);
                i--;
            }
            i++;
        }

        // fire
        for (final Consumer<T> watcher : fire) {
            watcher.accept(source);
        }
    }

    private final List<Consumer<T>> watchers = Lists.copyOnWrite();

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
