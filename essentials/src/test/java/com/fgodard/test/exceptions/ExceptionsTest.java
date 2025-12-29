package com.fgodard.test.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author crios
 */
public class ExceptionsTest {
        
    @Test
    public void testExceptionNoStringMsg() {
        final String errorCode = "Internal0001";
        InternalException e = new InternalException(errorCode);
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("com.fgodard.test.exceptions.InternalException: ".concat(errorCode)));
    }
    
    @Test        
    public void testExceptionWithStringMsg() {
        final String errorCode = "Internal0002";
        InternalException e = new InternalException(errorCode);
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("com.fgodard.test.exceptions.InternalException: Exception interne n°2"));
    }
    
    @Test
    public void testExceptionWithParameters() {
        final String errorCode = "Internal0003";
        InternalException e = new InternalException(errorCode, 3L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("com.fgodard.test.exceptions.InternalException: Exception interne n°3 valeur=7.5"));
    }
    
}
