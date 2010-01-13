/**
 * Copyright 2001-2010 the original author or authors.
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
 */

package org.easymock.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.easymock.internal.EqualsMatcher;
import org.easymock.internal.Invocation;
import org.junit.Before;
import org.junit.Test;

/**
 * @author OFFIS, Tammo Freese
 */
public class InvocationTest {

    private Invocation call;

    private Invocation equalCall;

    private Invocation nonEqualCall;

    @Before
    public void setup() throws SecurityException, NoSuchMethodException {
        Object[] arguments1 = new Object[] { "" };
        Object[] arguments2 = new Object[] { "" };
        Object[] arguments3 = new Object[] { "X" };
        Method m = Object.class.getMethod("equals",
                new Class[] { Object.class });
        Object mock = new Object();
        call = new Invocation(mock, m, arguments1);
        equalCall = new Invocation(mock, m, arguments2);
        nonEqualCall = new Invocation(mock, m, arguments3);
    }

    @Test
    public void testEquals() {
        assertFalse(call.equals(null));
        assertFalse(call.equals(""));
        assertTrue(call.equals(equalCall));
        assertFalse(call.equals(nonEqualCall));
    }

    @Test
    public void testHashCode() {
        try {
            call.hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
            assertEquals("hashCode() is not implemented", expected.getMessage());
        }
    }

    @Test
    public void testShouldDisplayMocksToStringIfValidJavaIdentifier()
            throws SecurityException, NoSuchMethodException {
        class ToString {
            private final String name;

            public ToString(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }

            public void aMethod() {
            }
        }

        Method method = ToString.class.getMethod("aMethod", new Class[0]);
        Invocation invocation = new Invocation(new ToString("validJavaIdentifier"),
                method, null);

        assertEquals(invocation.toString(new EqualsMatcher()),
                "validJavaIdentifier.aMethod()");

        invocation = new Invocation(new ToString("no-valid-java-identifier"),
                method, null);

        assertEquals(invocation.toString(new EqualsMatcher()), "aMethod()");

    }
}