package com.example.vacationscheduler;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void validPin_isAccepted() {
        boolean result = PinValidator.validatePin("1234", "1234");
        assertTrue(result);
    }
    @Test
    public void invalidPin_isRejected() {
        boolean result = PinValidator.validatePin("9999", "1234");
        assertFalse(result);
    }

}