Android 
================================

Async Pool Task Executor

```
 @Test
    public void testExecuteQueue() throws Exception {
        AsyncPool asyncPool = AsyncPool.create();
        AtomicInteger uid = new AtomicInteger(0);
        final int expected = 1000;
        for(int i = 0; i< expected; i++)
            asyncPool.executeQueue(() -> uid.getAndIncrement());

        new CountDownLatch(1).await(2000, TimeUnit.MILLISECONDS);

        assertEquals("Rejected execute callbacks",expected,uid.get());
    }
```

