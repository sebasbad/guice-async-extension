package de.skuzzle.inject.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class FuturesTest {

    @Test
    public void tesstFutureContract() throws Exception {
        final Future<String> future = Futures.delegate("result");
        assertFalse(future.cancel(true));
        assertFalse(future.isCancelled());
        assertTrue(future.isDone());
        assertEquals("result", future.get());
        assertEquals("result", future.get(Long.MAX_VALUE, TimeUnit.DAYS));
    }
}