# Redis

#### How to check redis password
```
CONFIG get requirepass
```

#### How to set redis password
```
CONFIG set requirepass "redis#123"
```

#### How to connect to remote redis from command line
```
redis-cli  -h 192.168.1.119 -p 6379 -a redis_password
```

#### How to See number of open connection in redis
```
client list
```
