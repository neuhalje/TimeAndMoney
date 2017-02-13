/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class ClassGenerator {
    private static final String TIMEANDMONEY_JAR_NAME = "timeandmoney";
    private static final String JAR_POST_FIX = ".jar";
    private static final String CLASS_POST_FIX = ".class";

    private ClassFilter filter;

    public ClassGenerator() {
        this(new ClassFilter() {
            public boolean accepts(Class klass) {
                return true;
            }
        });
    }

    public ClassGenerator(ClassFilter filter) {
        this.filter = filter;
    }

    public void go() throws Exception {
        StringTokenizer tokenizer = getClassPath();
        while (tokenizer.hasMoreTokens()) {
            File next = new File(tokenizer.nextToken());
            if (next.isDirectory()) {
                searchInDirectory(next);
            } else if (next.getName().toLowerCase().endsWith(JAR_POST_FIX)) {
                searchInJar(next);
            }
        }
    }

    protected abstract void next(Class klass) throws Exception;

    private void searchInJar(File jarFile) throws Exception {
        if (!jarFile.getName().startsWith(TIMEANDMONEY_JAR_NAME))
            return;
        JarFile jar = new JarFile(jarFile);
        try {
            Enumeration enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry next = (JarEntry) enumeration.nextElement();
                if (next.getName().toLowerCase().endsWith(CLASS_POST_FIX)
                        && next.getName().indexOf('$') == -1) {
                    doNext(convertToClassName(next.getName()));
                }
            }
        } finally {
            jar.close();
        }

    }

    private String convertToClassName(String fileName) {
        String minusPostFix = fileName.substring(0, fileName.length()
                - CLASS_POST_FIX.length());
        String changeToPeriods = minusPostFix.replace(File.separatorChar, '.');
        // this might seem redundant, but jars always have / no matter the
        // platform
        return changeToPeriods.replace('/', '.');
    }

    private void doNext(String className) throws Exception {
        Class klass = null;
        try {
            klass = Class.forName(className);
        } catch (Exception ex) {
            return;
        } catch (Error ex) {
            return;
        }
        if (filter.accepts(klass)) {
            next(klass);
        }
    }

    private void searchInDirectory(File directory) throws Exception {
        searchInDirectory(directory, directory);
    }

    private void searchInDirectory(File root, File toSearch) throws Exception {
        File[] contents = toSearch.listFiles();
        for (int index = 0; index < contents.length; index++) {
            File next = contents[index];
            if (next.isDirectory()) {
                searchInDirectory(root, next);
            } else if (next.getName().toLowerCase().endsWith(CLASS_POST_FIX)) {
                String fullFileName = next.getAbsolutePath();
                String fileName = fullFileName.substring(root.getAbsolutePath()
                        .length() + 1);
                doNext(convertToClassName(fileName));
            }
        }
    }

    private StringTokenizer getClassPath() {
        String classPath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");
        return new StringTokenizer(classPath, separator);
    }
}
