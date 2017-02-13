/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.runner.TestCaseClassLoader;
import junit.runner.TestCollector;

/**
 * I loads all TestCase subclasses on the class path, excluding specified
 * filters
 */
public class FilteredTestCaseCollector implements TestCollector {
    static final int SUFFIX_LENGTH= ".class".length();
    
    private TestCaseClassLoader loader = new TestCaseClassLoader();
    private String[] exludedPackages = new String[0];
    private Set exludedClasses = new HashSet();
    private Class[] exludedTypes = new Class[0];

    public final void exludedPackages(String[] exludedPackages) {
        this.exludedPackages = exludedPackages;
    }

    public final void exludedSuites(TestSuite[] exludedSuites) {
        Collection testCases = Tests.allTestCaseNames(exludedSuites);
        exludedClasses.addAll(testCases);
    }

    public final void exludedClasses(Class[] exludedClasses) {
        for (int i = 0; i < exludedClasses.length; i++)
            this.exludedClasses.add(exludedClasses[i].getName());
    }

    public final void exludedTypes(Class[] exludedTypes) {
        this.exludedTypes = exludedTypes;
        reloadExcludedTypes();
    }

    /**
     * due to class loaders discepency, we need to "normalize" classes through
     * one class loader. we choose our loader.
     */
    private void reloadExcludedTypes() {
        try {
            for (int i = 0; i < exludedTypes.length; i++)
                exludedTypes[i] = loader.loadClass(exludedTypes[i].getName(),
                        false);
        } catch (ClassNotFoundException ignore) {
        }
    }

    protected boolean isTestClass(String filename) {
        if (!filename.endsWith(".class"))
            return false;

        String className = classNameFromFile(filename);
        if (loader.isExcluded(className) || isExluded(className))
            return false;

        try {
            Class testClass = loader.loadClass(className, false);
            return isTestClass(testClass) && !isExludedByType(testClass);
        } catch (ClassNotFoundException ignore) {
        } catch (NoClassDefFoundError ignore) {
        }
        return false;
    }

    private boolean isExluded(String className) {
        for (int i = 0; i < exludedPackages.length; i++)
            if (className.startsWith(exludedPackages[i]))
                return true;
        return exludedClasses.contains(className);
    }

    private boolean isExludedByType(Class testClass) {
        for (int i = 0; i < exludedTypes.length; i++)
            if (exludedTypes[i].isAssignableFrom(testClass))
                return true;
        return false;
    }

    private boolean isTestClass(Class testClass) {
        return !Modifier.isAbstract(testClass.getModifiers())
                && Modifier.isPublic(testClass.getModifiers())
                && TestCase.class.isAssignableFrom(testClass);
    }

    protected void gatherFiles(File classRoot, String classFileName, Hashtable result) {
        File thisRoot = new File(classRoot, classFileName);
        if (thisRoot.isDirectory()) {
            String[] contents = thisRoot.list();
            if (contents != null) {
                for (int i = 0; i < contents.length; i++)
                    gatherFiles(classRoot, classFileName + File.separatorChar
                            + contents[i], result);
            }
        } else if (thisRoot.getName().endsWith(".class")) {
            addToResultsIfTest(classFileName, result);
        } else if (thisRoot.getName().endsWith(".jar")) {
            gatherFilesInJar(thisRoot, result);
        }
    }

    private void addToResultsIfTest(String classFileName, Hashtable result) {
        if (isTestClass(classFileName)) {
            String className = classNameFromFile(classFileName);
            result.put(className, className);
        }
    }

    protected void gatherFilesInJar(File jarFile, Hashtable result) {
        try {
            JarFile jar = new JarFile(jarFile);
            try {
                Enumeration enumeration = jar.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry next = (JarEntry) enumeration.nextElement();
                    if (next.getName().endsWith(".class") && next.getName().indexOf('$') == -1) {
                        addToResultsIfTest(next.getName(), result);
                    }
                }
            } finally {
                jar.close();
            }
        } catch (IOException ex) {
            //DO NOTHING
        }
    }
    //Copied from ClassPathTestCollector because we can't since they implemented private methods
    //and we needed to override gatherFiles(File,String,Hashtable). It seems silly to make an extensible
    //class and make some of the methods private.
    // The rest of this class is copied from ClassPathTestCollector
    protected String classNameFromFile(String classFileName) {
        // convert /a/b.class to a.b
        String s= classFileName.substring(0, classFileName.length()- SUFFIX_LENGTH);
        String s2= s.replace(File.separatorChar, '.');
        //changed this line for jar files (names are always / no matter the FileSystem
        s2 = s2.replace('/', '.');
        if (s2.startsWith("."))
            return s2.substring(1);
        return s2;
    }
    public Enumeration collectTests() {
        String classPath= System.getProperty("java.class.path");
        Hashtable result = collectFilesInPath(classPath);
        return result.elements();
    }

    public Hashtable collectFilesInPath(String classPath) {
        Hashtable result= collectFilesInRoots(splitClassPath(classPath));
        return result;
    }
    
    protected Hashtable collectFilesInRoots(Vector roots) {
        Hashtable result= new Hashtable(100);
        Enumeration e= roots.elements();
        while (e.hasMoreElements()) 
            gatherFiles(new File((String)e.nextElement()), "", result);
        return result;
    }
    protected Vector splitClassPath(String classPath) {
        Vector result= new Vector();
        String separator= System.getProperty("path.separator");
        StringTokenizer tokenizer= new StringTokenizer(classPath, separator);
        while (tokenizer.hasMoreTokens()) 
            result.addElement(tokenizer.nextToken());
        return result;
    }

}