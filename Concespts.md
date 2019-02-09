#### What is Pipline in redis and how it works?

Pipelining is primarily a network optimization. It essentially means the client buffers up a bunch of commands and 
ships them to the server in one go. The commands are not guaranteed to be executed in a transaction. The benefit 
here is saving network round  trip time for every command.


Example>

```
Pipeline pipeline = jedis.pipelined();

pipeline.multi();

for (int i = 0; i < 100000; i++) { 
    pipeline.set("" + i, "" + i); 
}

pipeline.exec(); or pipeline.sync(); //sync will not return any result
```


#### What is Multi in redis and how it works?

Multi means all instrucion will be executed in transaction.It ensure atomicity.All the commands in a transaction are 
serialized and executed sequentially. It can never happen that a request issued by another client is served in 
the middle of the execution of a Redis transaction. This guarantees that the commands are executed as a single isolated 
operation.

```
Transaction tx = jedis.multi();

for (int i = 0; i < 100000; i++) { 

    tx.set("" + i, "" + i); 

}

List<Object> results = tx.exec();
```


#### What is the difference between transaction and Pipleline in redis?

* Transactions make all the commands within the transaction atomic, pipelines make the client send all commands to
  the server at the same time (as opposed to sending one command at a time and waiting for the result).

* In jedis specifically, the client API makes it so that all transactions are also pipelines. This is why you won't
  see a performance difference - both the transaction and non-transaction code are run via a pipeline.

* However, there is a difference on the server side: with the only transaction code, if another redis clients tries 
  to get some data (say KEYS maybe?) in the middle of your transaction, it will not show them results until the entire 
  transaction is completed, so either they will receive data with all changes, or no changes.

* With the pipeline code, if another client tries to get data in the middle of your pipeline, it is perfectly allowed 
  for redis to give them partial data. By partial, I don't mean in the middle of a single set command, but rather maybe 
  data where half of the different set commands have executed, but the other half haven't.

#### What is watch in Redis?
Watch is kind of lock on key. If one transaction is changing any key other can not do.

Example Without watch
```
# Initial arbitrary value
powerlevel = 10
session A: GET powerlevel -> 10
session B: GET powerlevel -> 10
session A: current = 10 + 1
session B: current = 10 + 1
session A: SET powerlevel 11
session B: SET powerlevel 11
# In the end we have 11 instead of 12 -> wrong
```

With Watch

```
# Initial arbitrary value
powerlevel = 10
session A: WATCH powerlevel
session B: WATCH powerlevel
session A: GET powerlevel -> 10
session B: GET powerlevel -> 10
session A: current = 10 + 1
session B: current = 10 + 1
session A: MULTI
session B: MULTI
session A: SET powerlevel 11 -> QUEUED
session B: SET powerlevel 11 -> QUEUED
session A: EXEC -> success! powerlevel is now 11
session B: EXEC -> failure, because powerlevel has changed and was watched
# In the end, we have 11, and session B knows it has to attempt the transaction again
# Hopefully, it will work fine this time.
```


Java Code Example

```
Jedis jedis1 = jedisPool.getResource();
 jedis1.watch("foo");
 Transaction t1=jedis1.multi();	  		  	 
 t1.incrBy("foo", 1);
 t1.exec();
```
