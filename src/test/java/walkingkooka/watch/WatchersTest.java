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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class WatchersTest implements ClassTesting2<Watchers<?>>,
    ToStringTesting<Watchers<?>> {

    private final static String SOURCE1A = "Source1A";
    private final static String SOURCE2B = "Source2B";
    private final static String SOURCE3C = "Source3C";

    // add..............................................................................................................

    @Test
    public void testAddNullWatcherFails() {
        assertThrows(
            NullPointerException.class,
            () -> Watchers.create().add(null)
        );
    }

    @Test
    public void testAddAndFire() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired = Lists.array();
        watchers.add(this.watcher(fired));

        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.of(SOURCE1A), fired);
    }

    @Test
    public void testAddAndFire2() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();
        watchers.add(this.watcher(fired1));
        watchers.add(this.watcher(fired2));

        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.of(SOURCE1A), fired1);
        this.checkEquals(Lists.of(SOURCE1A), fired2);
    }

    @Test
    public void testAddAndFire3() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();
        watchers.add(this.watcher(fired1));
        watchers.add(this.watcher(fired2));

        watchers.accept(SOURCE1A);
        watchers.accept(SOURCE2B);

        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B), fired1);
        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B), fired2);
    }

    @Test
    public void testAddMultipleTimes() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired = Lists.array();
        final Consumer<String> watcher = this.watcher(fired);

        watchers.add(watcher);
        watchers.accept(SOURCE1A);

        watchers.add(watcher);
        watchers.accept(SOURCE2B);

        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B, SOURCE2B), fired);
    }

    @Test
    public void testAddAndRemove() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        watchers.add(this.watcher(fired1)).run();

        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.empty(), fired1);
    }

    @Test
    public void testAddMultipleTimesRemovedOnce() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired = Lists.array();
        final Consumer<String> watcher = this.watcher(fired);
        final Runnable remover = watchers.add(watcher);

        watchers.accept(SOURCE1A);

        watchers.add(watcher);
        watchers.accept(SOURCE2B);

        remover.run();

        watchers.accept(SOURCE3C);

        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B, SOURCE2B, SOURCE3C), fired);
    }

    @Test
    public void testAddAndRemoveMultipleTimes() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        watchers.add(this.watcher(fired1)).run();

        watchers.accept(SOURCE1A);
        watchers.accept(SOURCE1A);
        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.empty(), fired1);
    }

    @Test
    public void testAddFireAndRemove() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();
        final Runnable remover1 = watchers.add(this.watcher(fired1));
        final Runnable remover2 = watchers.add(this.watcher(fired2));

        watchers.accept(SOURCE1A);

        remover1.run();
        remover1.run();

        watchers.accept(SOURCE2B);

        remover2.run();

        watchers.accept(SOURCE3C);

        this.checkEquals(Lists.of(SOURCE1A), fired1);
        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B), fired2);
    }

    // addOnce..........................................................................................................

    @Test
    public void testAddOnceNullWatcherFails() {
        assertThrows(NullPointerException.class,
            () -> Watchers.create().addOnce(null)
        );
    }

    @Test
    public void testAddOnceAndFire() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired = Lists.array();
        watchers.addOnce(this.watcher(fired));

        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.of(SOURCE1A), fired);
    }

    @Test
    public void testAddOnceAndFire2() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();
        watchers.addOnce(this.watcher(fired1));
        watchers.addOnce(this.watcher(fired2));

        watchers.accept(SOURCE1A);

        this.checkEquals(Lists.of(SOURCE1A), fired1);
        this.checkEquals(Lists.of(SOURCE1A), fired2);
    }

    @Test
    public void testAddOnceAndFire3() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();
        watchers.addOnce(this.watcher(fired1));
        watchers.addOnce(this.watcher(fired2));

        watchers.accept(SOURCE1A);
        watchers.accept("Ignored!");

        this.checkEquals(Lists.of(SOURCE1A), fired1);
        this.checkEquals(Lists.of(SOURCE1A), fired2);
    }

    @Test
    public void testAddAndAddOnceAndFire() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();

        watchers.addOnce(this.watcher(fired1));
        watchers.add(this.watcher(fired2));

        watchers.accept(SOURCE1A);
        watchers.accept(SOURCE2B);

        this.checkEquals(Lists.of(SOURCE1A), fired1);
        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B), fired2);
    }

    // removeOnce......................................................................................................

    @Test
    public void testAddAddOnceRemoveOnceAndFire() {
        final Watchers<String> watchers = Watchers.create();

        final List<String> fired1 = Lists.array();
        final List<String> fired2 = Lists.array();

        watchers.addOnce(this.watcher(fired1));
        watchers.add(this.watcher(fired2));

        watchers.removeOnce();

        watchers.accept(SOURCE1A);
        watchers.accept(SOURCE2B);

        this.checkEquals(Lists.empty(), fired1);
        this.checkEquals(Lists.of(SOURCE1A, SOURCE2B), fired2);
    }

    private Consumer<String> watcher(final List<String> fired) {
        return (s) -> fired.add(s);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Watchers<String> watchers = Watchers.create();
        watchers.add(this.watcher("watcher1"));
        watchers.add(this.watcher("watcher2"));

        this.toStringAndCheck(watchers, "[watcher1, watcher2]");
    }

    @Test
    public void testToString2() {
        final Watchers<String> watchers = Watchers.create();
        watchers.add(this.watcher("watcher1"));
        watchers.add(this.watcher("watcher2")).run();

        this.toStringAndCheck(watchers, "[watcher1]");
    }

    private Consumer<String> watcher(final String toString) {
        return new Consumer<>() {
            @Override
            public void accept(String s) {
                throw new UnsupportedOperationException();
            }

            public String toString() {
                return toString;
            }
        };
    }


    @Override
    public Class<Watchers<?>> type() {
        return Cast.to(Watchers.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
