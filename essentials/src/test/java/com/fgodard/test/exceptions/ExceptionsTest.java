package com.fgodard.test.exceptions;

import com.fgodard.exceptions.IntlException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author crios
 */
public class ExceptionsTest {
    
    
    @Test
    public void testExceptionNoMsg() {
        IntlException.setLocale(Locale.FRENCH);
        InternalException e = new InternalException();
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("com.fgodard.test.exceptions.InternalException"));
    }
        
    @Test
    public void testExceptionNoStringMsg() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0001";
        InternalException e = new InternalException(errorCode);
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith(errorCode));
    }
    
    @Test        
    public void testExceptionWithStringMsg() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0002";
        InternalException e = new InternalException(errorCode);
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Exception interne n°2"));
    }
    
    @Test
    public void testExceptionWithParameters() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0003";
        InternalException e = new InternalException(errorCode, 3L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Exception interne n°3 valeur=7.5"));
    }
    
    @Test
    public void testExceptionForeignLanguage() {
        IntlException.setLocale(Locale.US);
        final String errorCode = "Internal0004";        
        InternalException e = new InternalException(errorCode, 4L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Internal exception n°4 valeur=7.5"));
    }
    
    @Test
    public void testExceptionWithMuteCause() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0005";        
        InternalException e = new InternalException(new IOException(),errorCode, 5L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Internal0005"));
        assertTrue(out.toString().contains("cause: java.io.IOException"));
    }
    
    @Test
    public void testExceptionWithCause() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0006";        
        InternalException e = new InternalException(new IOException("Erreur d'ecriture"),errorCode, 5L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Internal0006"));
        assertTrue(out.toString().contains("cause: Erreur d'ecriture"));
    }
    
    @Test
    public void testExceptionWithCauses() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0006";        
        InternalException e = new InternalException(new IOException(new IOException("Erreur d'ecriture")),errorCode, 5L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Internal0006\n@com.fgodard.test.exceptions.ExceptionsTest.testExceptionWithCauses:"));
        assertTrue(out.toString().contains("cause: java.io.IOException"));        
        assertTrue(out.toString().contains("cause: Erreur d'ecriture"));
    }
    
    @Test
    public void testExceptionWithMultiCause() {
        IntlException.setLocale(Locale.FRENCH);
        final String errorCode = "Internal0007";        
        List<Exception> exceptions = new ArrayList<>();
        exceptions.add(new IOException("Erreur d'ecriture"));
        exceptions.add(new IOException());
        InternalException e = new InternalException(exceptions ,errorCode, 5L, 7.5d, "valeur");
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        assertTrue(out.toString().startsWith("Internal0007"));
        assertTrue(out.toString().contains("cause: java.io.IOException"));
        assertTrue(out.toString().contains("cause: Erreur d'ecriture"));
    }
    
}
