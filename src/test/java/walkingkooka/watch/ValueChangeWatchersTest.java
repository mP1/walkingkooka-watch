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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueChangeWatchersTest implements ClassTesting<ValueChangeWatchers<String>> {

    @Test
    public void testAddWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueChangeWatchers.empty()
                .add(null)
        );
    }

    @Test
    public void testAddOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueChangeWatchers.empty()
                .addOnce(null)
        );
    }

    private final static String OLD_VALUE = "OldValue111";

    private final static String NEW_VALUE = "NewValue222";

    @Test
    public void testAddThenFire() {
        this.fired = false;

        final ValueChangeWatchers<String> watchers = ValueChangeWatchers.empty();
        watchers.add(
            new ValueChangeWatcher<>() {

                @Override
                public void onValueChange(final Optional<String> ov,
                                          final Optional<String> nv) {
                    checkEquals(
                        Optional.of(OLD_VALUE),
                        ov
                    );
                    checkEquals(
                        Optional.of(NEW_VALUE),
                        nv
                    );

                    fired = true;
                }
            });
        watchers.onValueChange(
            Optional.of(OLD_VALUE),
            Optional.of(NEW_VALUE)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddThenFireEqualValues() {
        this.fired = false;

        final ValueChangeWatchers<String> watchers = ValueChangeWatchers.empty();
        watchers.add(
            new ValueChangeWatcher<>() {
                @Override
                public void onValueChange(final Optional<String> OLD_VALUE,
                                          final Optional<String> NEW_VALUE) {
                    throw new UnsupportedOperationException();
                }
            }
        );

        watchers.onValueChange(
            Optional.of(NEW_VALUE),
            Optional.of(NEW_VALUE)
        );

        this.checkEquals(
            false,
            this.fired
        );
    }

    @Test
    public void testAddOnceThenFire() {
        this.fired = false;

        final ValueChangeWatchers<String> watchers = ValueChangeWatchers.empty();
        watchers.addOnce(
            new ValueChangeWatcher<>() {

                @Override
                public void onValueChange(final Optional<String> ov,
                                          final Optional<String> nv) {
                    checkEquals(
                        false,
                        fired,
                        "event should only have been fired once!"
                    );

                    checkEquals(
                        Optional.of(OLD_VALUE),
                        ov
                    );
                    checkEquals(
                        Optional.of(NEW_VALUE),
                        nv
                    );

                    fired = true;
                }
            });
        watchers.onValueChange(
            Optional.of(OLD_VALUE),
            Optional.of(NEW_VALUE)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired = false;

    // Class............................................................................................................

    @Override
    public Class<ValueChangeWatchers<String>> type() {
        return Cast.to(ValueChangeWatchers.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
