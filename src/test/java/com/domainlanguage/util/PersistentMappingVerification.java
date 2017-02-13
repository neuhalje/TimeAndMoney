/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import com.domainlanguage.intervals.Interval;
import com.domainlanguage.intervals.IntervalTest;
import com.domainlanguage.time.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

public class PersistentMappingVerification {
    private static final String SET = "set";
    private static final String GET = "get";
    private static final String GET_PRIMITIVE_PERSISTENCE_MAPPING_TYPE = "getPrimitivePersistenceMappingType";
    private static final String FOR_PERSISTENT_MAPPING = "ForPersistentMapping_";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final Map TEST_TYPE_MAPPING;
    private static final Set SHOULD_IGNORE_FIELDS;

    static {
        TEST_TYPE_MAPPING = new HashMap();
        TEST_TYPE_MAPPING
                .put(BigDecimal.class.getName(), BigDecimal.valueOf(1));
        TEST_TYPE_MAPPING.put(Boolean.TYPE.getName(), Boolean.TRUE);
        TEST_TYPE_MAPPING.put(CalendarDate.class.getName(), CalendarDate.date(
                2005, 12, 29));
        TEST_TYPE_MAPPING.put(Comparable.class.getName(), new Integer(2));
        TEST_TYPE_MAPPING.put(Currency.class.getName(), Currency
                .getInstance("EUR"));
        TEST_TYPE_MAPPING.put(Duration.class.getName(), Duration.days(11));
        TEST_TYPE_MAPPING.put(HourOfDay.class.getName(), HourOfDay.value(11));
        TEST_TYPE_MAPPING.put(Integer.TYPE.getName(), new Integer(3));
        TEST_TYPE_MAPPING.put("com.domainlanguage.intervals.IntervalLimit", IntervalTest.exampleLimitForPersistentMappingTesting());
        TEST_TYPE_MAPPING.put(Long.TYPE.getName(), new Long(4));
        TEST_TYPE_MAPPING.put(List.class.getName(), new ArrayList());
        TEST_TYPE_MAPPING.put(Map.class.getName(), new HashMap());
        TEST_TYPE_MAPPING.put(MinuteOfHour.class.getName(), MinuteOfHour.value(22));
        TEST_TYPE_MAPPING.put(Set.class.getName(), new HashSet());
        TEST_TYPE_MAPPING.put(String.class.getName(), "sample value");
        TEST_TYPE_MAPPING.put(TimeOfDay.class.getName(), TimeOfDay.hourAndMinute(7, 44));
        TEST_TYPE_MAPPING.put(TimeRate.class.getName(), new TimeRate(BigDecimal
                .valueOf(5), Duration.days(6)));
        TEST_TYPE_MAPPING.put("com.domainlanguage.time.TimeUnit", TimeUnitTest
                .exampleForPersistentMappingTesting());
        TEST_TYPE_MAPPING.put("com.domainlanguage.time.TimeUnit$Type", TimeUnitTest
                .exampleTypeForPersistentMappingTesting());

        SHOULD_IGNORE_FIELDS = new HashSet();
        SHOULD_IGNORE_FIELDS.add(Interval.class.getName());
        SHOULD_IGNORE_FIELDS.add("com.domainlanguage.intervals.IntervalLimit");
    }

    private Class toVerify;
    private Object instance;
    private List problems;

    public static PersistentMappingVerification on(Class klass)
            throws ClassNotFoundException {
        return new PersistentMappingVerification(klass);
    }

    public boolean isPersistableRequirementsSatisfied() {
        return problems.isEmpty();
    }

    public String formatFailure() {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        String nextProblem;
        for (Iterator problemsIterator = problems.iterator(); problemsIterator
                .hasNext(); ) {
            nextProblem = (String) problemsIterator.next();
            if (!first) {
                buffer.append(LINE_SEPARATOR);
            }
            first = false;
            buffer.append(nextProblem);
        }
        return buffer.toString();
    }

    private PersistentMappingVerification(Class klass) {
        initialize(klass);
    }

    private void initialize(Class klass) {
        this.toVerify = klass;
        this.problems = new ArrayList();
        try {
            if (isPrimitivePersistenceMappingType(klass)) {
                return;
            }
            checkEverything();
        } catch (RuntimeException ex) {
            addToProblems(ex.toString());
        }
    }

    private void checkEverything() {
        checkClass(toVerify);
        Class current = toVerify;
        while (current != null && current != Object.class) {
            checkFields(current);
            current = current.getSuperclass();
        }
    }

    private void checkFields(Class klass) {
        if (shouldIgnoreFields(klass)) {
            return;
        }
        Field[] fields = klass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field each = fields[index];
            if ((each.getModifiers() & Modifier.STATIC) > 0) {
                continue;
            }
            checkField(each);
        }
    }

    private void checkClass(Class klass) {
        if (klass.isInterface()) {
            return;
        }
        if (isAbstract(klass)) {
            return;
        }
        if (isFinal(klass)) {
            addToProblems(klass.toString() + " must not be final");
        }
        checkConstructor(klass);
    }

    private void checkConstructor(Class klass) {
        Constructor constructor = null;
        try {
            constructor = klass.getDeclaredConstructor(null);
            constructor.setAccessible(true);
            instance = constructor.newInstance(null);
        } catch (NoSuchMethodException ex) {
            addToProblems(klass.toString() + " has no default constructor");
        } catch (IllegalArgumentException ex) {
            addToProblems(constructor.toString()
                    + " had an illegal argument exception");
        } catch (InstantiationException ex) {
            addToProblems(constructor.toString()
                    + " had an instantion exception");
        } catch (IllegalAccessException ex) {
            addToProblems(constructor.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(constructor.toString()
                    + " had an invocation exception");
        }
    }

    private boolean shouldIgnoreFields(Class klass) {
        return SHOULD_IGNORE_FIELDS.contains(klass.getName());
    }

    private boolean isAbstract(Class klass) {
        return (klass.getModifiers() & Modifier.ABSTRACT) > 0;
    }

    private boolean isFinal(Class klass) {
        return (klass.getModifiers() & Modifier.FINAL) > 0;
    }

    private void checkField(Field theField) {
        String name = capitalize(theField.getName());
        Class type = getTypeFor(theField);
        Object toTest = getTestValueFor(type);
        checkSetter(theField, name, toTest);
        Object actual = checkGetter(theField, name);
        if (instance != null && !sameClassOrBothNull(toTest, actual)) {
            addToProblems(theField.toString()
                    + " getter/setter result do not match, expected [" + toTest
                    + "], but got [" + actual + "]");
            return;
        }
    }

    private boolean sameClassOrBothNull(Object one, Object another) {
        if (one == another)
            return true;
        if (one == null)
            return false;
        if (another == null)
            return false;
        return one.getClass().isInstance(another);
    }

    private Object checkGetter(Field theField, String name) {
        Method getter = null;
        Object actual = null;
        try {
            getter = getGetter(theField, name, GET + FOR_PERSISTENT_MAPPING + name);
            if (!isMethodPrivate(getter)) {
                addToProblems(getter.toString() + " not declared private");
            }
            if (instance != null) {
                getter.setAccessible(true);
                actual = getter.invoke(instance, null);
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(ex.getMessage() + "does not exist");
        } catch (IllegalArgumentException ex) {
            addToProblems(getter.toString()
                    + " had an illegal argument exception");
        } catch (IllegalAccessException ex) {
            addToProblems(getter.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(getter.toString()
                    + " had an invocation target exception");
        }
        return actual;
    }

    private void checkSetter(Field theField, String name, Object toTest) {
        Method setter = null;
        try {
            setter = getSetter(theField, SET + FOR_PERSISTENT_MAPPING + name);
            if (!isMethodPrivate(setter)) {
                addToProblems(setter.toString() + " not declared private");
            }
            if (instance != null) {
                setter.setAccessible(true);
                setter.invoke(instance, new Object[]{toTest});
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(ex.getMessage() + "does not exist");
        } catch (IllegalArgumentException ex) {
            addToProblems(setter.toString()
                    + " had an illegal argument exception");
        } catch (IllegalAccessException ex) {
            addToProblems(setter.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(setter.toString()
                    + " had an invocation target exception");
        }
    }

    private Class getTypeFor(Field theField) {
        Class type = theField.getType();
        if (type.isPrimitive())
            return type;
        try {
            Method method = type.getDeclaredMethod(
                    GET_PRIMITIVE_PERSISTENCE_MAPPING_TYPE, null);
            method.setAccessible(true);
            return (Class) method.invoke(null, null);
        } catch (IllegalArgumentException ex) {
            return type;
        } catch (IllegalAccessException ex) {
            return type;
        } catch (InvocationTargetException ex) {
            return type;
        } catch (SecurityException ex) {
            return type;
        } catch (NoSuchMethodException ex) {
            return type;
        }
    }

    private Object getTestValueFor(Class type) {
        Object result = TEST_TYPE_MAPPING.get(type.getName());
        if (result == null) {
            addToProblems("Add sample value for " + type.toString()
                    + " to TEST_TYPE_MAPPING");
        }
        return result;
    }

    private boolean isMethodPrivate(Method toCheck) {
        return (toCheck.getModifiers() & Modifier.PRIVATE) > 0;
    }

    private void addToProblems(String reason) {
        problems.add(reason);
    }

    private Method getSetter(Field theField, String setter)
            throws NoSuchMethodException {
        Class[] setterTypes = new Class[1];
        setterTypes[0] = getTypeFor(theField);
        return theField.getDeclaringClass().getDeclaredMethod(setter,
                setterTypes);
    }

    private Method getGetter(Field theField, String name, String getter)
            throws NoSuchMethodException {
        try {
            return theField.getDeclaringClass().getDeclaredMethod(getter, null);
        } catch (NoSuchMethodException unknownGetter) {
            return theField.getDeclaringClass().getDeclaredMethod(
                    "is" + FOR_PERSISTENT_MAPPING + name, null);
        }
    }

    private String capitalize(String string) {
        char chars[] = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    private boolean isPrimitivePersistenceMappingType(Class klass) {
        try {
            return klass.getDeclaredMethod(GET_PRIMITIVE_PERSISTENCE_MAPPING_TYPE, null) != null;
        } catch (SecurityException ex) {
            return false;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }
}
