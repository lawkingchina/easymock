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

package org.easymock.tests2;

import static org.easymock.internal.ClassExtensionHelper.*;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.easymock.EasyMock;
import org.easymock.internal.MocksControl;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class ClassExtensionHelperTest {

    @Test
    public void testGetControl_EasyMock() {
        List<?> mock = EasyMock.createMock(List.class);
        assertNotNull(getControl(mock));
    }

    @Test
    public void testGetControl_EasyMockClassExtension() {
        ArrayList<?> mock = EasyMock.createMock(ArrayList.class);
        assertTrue(getControl(mock) instanceof MocksControl);
    }

    @Test
    public void testGetControl_EnhancedButNotAMock() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            getControl(o);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_ProxyButNotMock() {
        Object o = Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { List.class }, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                            Object[] args) throws Throwable {
                        return null;
                    }
                });
        try {
            getControl(o);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + o.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testGetControl_NotAMock() {
        try {
            getControl("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Not a mock: " + String.class.getName(), e.getMessage());
        }
    }
}
