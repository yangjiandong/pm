package org.ssh.pm.utils;

import org.springframework.scripting.groovy.GroovyObjectCustomizer;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyObject;

//监测groovy脚本执行
public class PerformanceLoggingCustomizer implements GroovyObjectCustomizer {

    public void customize(GroovyObject goo) {
        DelegatingMetaClass metaClass = new DelegatingMetaClass(goo.getMetaClass()) {
            @Override
            public Object invokeMethod(Object object, String method, Object[] args) {
                long start = System.currentTimeMillis();
                Object result = super.invokeMethod(object, method, args);
                long elapsed = System.currentTimeMillis() - start;
                System.out.printf("%s took %d milliseconds on object %s\n", method, elapsed, object);
                return result;
            }
        };
        metaClass.initialize();
        goo.setMetaClass(metaClass);
    }
}