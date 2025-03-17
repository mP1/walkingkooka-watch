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

/**
 * An abstraction that executes all given {@link Runnable} when its own {@link #run()} is invoked.
 */
final class RunnableCollection implements Runnable {

    static Runnable with(final List<Runnable> runnables) {
        final List<Runnable> copy = Lists.immutable(
            Objects.requireNonNull(runnables, "runnables")
        );

        Runnable result;

        switch(copy.size()) {
            case 0:
                throw new IllegalArgumentException("Empty runnables");
            case 1:
                result = copy.get(0);
                break;
            default:
                result = new RunnableCollection(copy);
                break;
        }

        return result;
    }

    private RunnableCollection(final List<Runnable> runnables) {
        this.runnables = runnables;
    }

    @Override
    public void run() {
        this.runnables.forEach(Runnable::run);
    }

    private final List<Runnable> runnables;

    @Override
    public String toString() {
        return this.runnables.toString();
    }
}
