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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.LineEnding;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueChangeWatchersEventTest implements ClassTesting<ValueChangeWatchersEvent<LineEnding>>,
    ToStringTesting<ValueChangeWatchersEvent<LineEnding>> {

    // with.............................................................................................................

    @Test
    public void testWithNullOldValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueChangeWatchersEvent.with(
                null,
                Optional.empty()
            )
        );
    }

    @Test
    public void testWithNullNewValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueChangeWatchersEvent.with(
                Optional.empty(),
                null
            )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            ValueChangeWatchersEvent.with(
                Optional.of(LineEnding.CR),
                Optional.of(LineEnding.NL)
            ),
            "\"\r" +
                "\" \"\n" +
                "\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<ValueChangeWatchersEvent<LineEnding>> type() {
        return Cast.to(ValueChangeWatchersEvent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
