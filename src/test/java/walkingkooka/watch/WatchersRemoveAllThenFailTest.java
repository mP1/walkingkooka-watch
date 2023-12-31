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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class WatchersRemoveAllThenFailTest implements ClassTesting2<WatchersRemoveAllThenFail> {

    @BeforeEach
    public void beforeEachTest() {
        this.counter = 0;
    }

    @Test
    public void testNullFails() {
        assertThrows(NullPointerException.class, () -> {
            WatchersRemoveAllThenFail.executeOrFail((Runnable[]) null);
        });
    }

    @Test
    public void testAllSuccessful() {
        WatchersRemoveAllThenFail.executeOrFail(() -> this.counter++, () -> this.counter++);

        this.check(2);
    }

    @Test
    public void testSkipsNulls() {
        WatchersRemoveAllThenFail.executeOrFail(null, null, () -> this.counter++);

        this.check(1);
    }

    @Test
    public void testFirstFailureContinuesWithOutstanding() {
        final RuntimeException thrown = new RuntimeException("first");

        assertSame(thrown, assertThrows(RuntimeException.class, () -> {

            WatchersRemoveAllThenFail.executeOrFail(
                    () -> {
                        throw thrown;
                    },
                    () -> this.counter++);
        }));

        this.check(1);
    }

    @Test
    public void testMultipleFailures() {
        final RuntimeException first = new RuntimeException("first");
        final RuntimeException second = new RuntimeException("second");

        assertSame(first, assertThrows(RuntimeException.class, () -> {

            WatchersRemoveAllThenFail.executeOrFail(
                    () -> {
                        throw first;
                    },
                    () -> {
                        throw second;
                    },
                    () -> this.counter++);
        }));

        assertArrayEquals(new Throwable[]{second}, first.getSuppressed());
        this.check(1);
    }

    @Test
    public void testMultipleFailures2() {
        final RuntimeException first = new RuntimeException("first");
        final RuntimeException second = new RuntimeException("second");
        final RuntimeException third = new RuntimeException("third");

        assertSame(first, assertThrows(RuntimeException.class, () -> {

            WatchersRemoveAllThenFail.executeOrFail(
                    () -> {
                        throw first;
                    },
                    () -> {
                        throw second;
                    },
                    null,
                    () -> {
                        throw third;
                    },
                    () -> this.counter++);
        }));

        assertArrayEquals(new Throwable[]{second, third}, first.getSuppressed());
        this.check(1);
    }

    private void check(final int expected) {
        this.checkEquals(expected, counter, "watchers removed");
    }

    private int counter;

    @Override
    public Class<WatchersRemoveAllThenFail> type() {
        return WatchersRemoveAllThenFail.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
