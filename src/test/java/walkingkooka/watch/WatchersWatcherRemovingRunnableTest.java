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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.function.Consumer;

public final class WatchersWatcherRemovingRunnableTest implements ClassTesting2<WatchersWatcherRemovingRunnable<?>>,
    ToStringTesting<WatchersWatcherRemovingRunnable<?>> {

    @Test
    public void testToStringRemoved() {
        final Watchers<String> watchers = Watchers.create();
        final Runnable remover = watchers.add(this.watcher());
        remover.run();

        this.toStringAndCheck(remover, "Watcher123 Removed");
    }

    @Test
    public void testToStringActive() {
        final Watchers<String> watchers = Watchers.create();
        this.toStringAndCheck(watchers.add(this.watcher()), "Watcher123 Active");
    }

    private Consumer<String> watcher() {
        return new Consumer<>() {
            @Override
            public void accept(String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String toString() {
                return "Watcher123";
            }
        };
    }

    @Override
    public Class<WatchersWatcherRemovingRunnable<?>> type() {
        return Cast.to(WatchersWatcherRemovingRunnable.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
