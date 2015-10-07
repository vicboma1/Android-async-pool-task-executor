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

## Configuration

You can create the jar like a library :

``` jar cvf <name.jar> <folder/roots.class> ```

and import in Intellij:

![](http://s14.postimg.org/i1vsentxd/Screen_Shot_2014_10_16_at_16_16_02.png)

or like a "module dependency" including this project :

![](http://s27.postimg.org/e42va8as3/Screen_Shot_2014_10_16_at_17_16_46.png)







[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vicboma1/android-async-pool-task-executor/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

