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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RunnableCollectionTest implements ToStringTesting<RunnableCollection>,
    ClassTesting2<RunnableCollection> {

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> RunnableCollection.with(null)
        );
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> RunnableCollection.with(
                Lists.empty()
            )
        );
    }

    @Test
    public void testWithOneNeverWraps() {
        final Runnable runnable = () -> {
        };

        assertSame(
            runnable,
            RunnableCollection.with(
                Lists.of(runnable)
            )
        );
    }

    @Test
    public void testWith() {
        final Runnable runnable1 = () -> {
        };
        final Runnable runnable2 = () -> {
        };

        final RunnableCollection runnableCollection = (RunnableCollection) RunnableCollection.with(
            Lists.of(
                runnable1,
                runnable2
            )
        );
    }

    // run..............................................................................................................

    @BeforeEach
    public void beforeEachTest() {
        this.counter = 0;
    }

    private void counterCheck(final int expected) {
        this.checkEquals(
            expected,
            counter,
            "watchers removed"
        );
    }

    private int counter;

    @Test
    public void testRun() {
        final Runnable runnable1 = () -> this.counter++;
        final Runnable runnable2 = () -> this.counter++;

        final Runnable runnableCollection = RunnableCollection.with(
            Lists.of(
                runnable1,
                runnable2
            )
        );

        runnableCollection.run();

        this.counterCheck(2);
    }

    @Test
    public void testRunFirstFailureContinuesWithOutstanding() {
        final RuntimeException thrown = new RuntimeException("first");

        assertSame(
            thrown,
            assertThrows(
                RuntimeException.class,
                () -> RunnableCollection.with(
                    Lists.of(
                        () -> {
                            throw thrown;
                        },
                        () -> this.counter++
                    )
                ).run()
            )
        );

        this.check(1);
    }

    @Test
    public void testRunMultipleFailures() {
        final RuntimeException first = new RuntimeException("first");
        final RuntimeException second = new RuntimeException("second");

        assertSame(
            first,
            assertThrows(
                RuntimeException.class,
                () -> RunnableCollection.with(
                    Lists.of(
                        () -> {
                            throw first;
                        },
                        () -> {
                            throw second;
                        },
                        () -> {
                            this.counter++;
                        }
                    )
                ).run()
            )
        );

        assertArrayEquals(
            new Throwable[]
                {second},
            first.getSuppressed()
        );
        this.check(1);
    }

    @Test
    public void testRunMultipleFailures2() {
        final RuntimeException first = new RuntimeException("first");
        final RuntimeException second = new RuntimeException("second");
        final RuntimeException third = new RuntimeException("third");

        assertSame(
            first,
            assertThrows(
                RuntimeException.class,
                () -> RunnableCollection.with(
                    Lists.of(
                        () -> {
                            throw first;
                        },
                        () -> {
                            throw second;
                        },
                        () -> {
                            throw third;
                        },
                        () -> this.counter++
                    )
                ).run()
            )
        );

        assertArrayEquals(
            new Throwable[]{
                second,
                third
            },
            first.getSuppressed()
        );
        this.check(1);
    }

    private void check(final int expected) {
        this.checkEquals(expected, counter, "watchers removed");
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final AtomicInteger counter = new AtomicInteger();
        final Runnable runnable1 = new Runnable() {

            @Override
            public void run() {

            }

            @Override
            public String toString() {
                return "11";
            }
        };
        final Runnable runnable2 = new Runnable() {

            @Override
            public void run() {

            }

            @Override
            public String toString() {
                return "22";
            }
        };

        this.toStringAndCheck(
            RunnableCollection.with(
                Lists.of(
                    runnable1,
                    runnable2
                )
            ),
            "[11, 22]"
        );
    }

    // class............................................................................................................

    @Override
    public Class<RunnableCollection> type() {
        return RunnableCollection.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
