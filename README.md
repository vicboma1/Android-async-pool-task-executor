Android 
================================

##Async Pool Task Executor

APTE is an java application framework for android.
It offers:

* Executor
* Pool
* ThreadFactory
* Worker


#Quickstart

##Test
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

Note: Android no accept lambdas with java 8 in Intellij.

## Application
```
 /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        AsyncPool asyncPool = AsyncPool.create();

        asyncPool.executeQueue(new Callable<T>() {
           @Override
           public <T> call() {
              // something...
               return T;
           }
        });

          asyncPool.executeQueue(new Callable<T>() {
            @Override
            public <T> call() {
               // something...
                return T;
            }
        });
    }
    
```
Note : Not return a value as a promise or the like. Only process the lambda.


