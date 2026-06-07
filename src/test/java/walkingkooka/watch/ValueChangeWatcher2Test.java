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

/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueChangeWatcher2Test implements ClassTesting2<ValueChangeWatcher2<String>> {

    // onValueChange....................................................................................................

    @Test
    public void testOnValueChangeWithNullOldValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeValueChangeWatcher2()
                .onValueChange(
                    null,
                    Optional.empty()
                )
        );
    }

    @Test
    public void testOnValueChangeWithNullNewValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeValueChangeWatcher2()
                .onValueChange(
                    Optional.empty(),
                    null
                )
        );
    }

    private final static String OLD_VALUE = "OldValue111";

    private final static String NEW_VALUE = "NewValue111";

    // onValueChangeAdd.................................................................................................

    @Test
    public void testOnValueChangeAdd() {
        this.fired = false;

        new FakeValueChangeWatcher2() {
            @Override
            public void onValueChangeAdd(final String nv) {
                checkEquals(
                    NEW_VALUE,
                    nv
                );

                ValueChangeWatcher2Test.this.fired = true;
            }
        }.onValueChange(
            Optional.empty(),
            Optional.of(NEW_VALUE)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onValueChangeRemove..............................................................................................

    @Test
    public void testOnValueChangeRemove() {
        this.fired = false;

        new FakeValueChangeWatcher2() {

            @Override
            public void onValueChangeRemove(final String nv) {
                checkEquals(
                    OLD_VALUE,
                    nv
                );

                ValueChangeWatcher2Test.this.fired = true;
            }

        }.onValueChange(
            Optional.of(OLD_VALUE),
            Optional.empty()
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onValueChangeReplace.............................................................................................

    @Test
    public void testOnValueChangeUpdate() {
        this.fired = false;

        new FakeValueChangeWatcher2() {

            @Override
            public void onValueChangeReplace(final String ov,
                                             final String nv) {
                checkEquals(
                    OLD_VALUE,
                    ov,
                    "oldValue"
                );
                checkEquals(
                    NEW_VALUE,
                    nv,
                    "newValue"
                );

                ValueChangeWatcher2Test.this.fired = true;
            }
        }.onValueChange(
            Optional.of(OLD_VALUE),
            Optional.of(NEW_VALUE)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    static class FakeValueChangeWatcher2 implements ValueChangeWatcher2<String> {

        @Override
        public void onValueChangeAdd(final String nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onValueChangeRemove(final String nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onValueChangeReplace(final String ov,
                                         final String nv) {
            throw new UnsupportedOperationException();
        }
    }

    // class............................................................................................................

    @Override
    public Class<ValueChangeWatcher2<String>> type() {
        return Cast.to(ValueChangeWatcher2.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
