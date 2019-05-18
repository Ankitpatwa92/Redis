#### SSL for Redis
Redis was desinged to use in private network that's why redis does not support SSL but stunnel provide a 
wrapper over redis to connect redis


What is stunnel
```
The stunnel application is a SSL encryption wrapper that can tunnel unencrypted traffic (like redis) through a 
SSL encrypted tunnel to another server. While stunnel adds SSL encryption it does not guarantee 100% that the 
traffic will never be captured unencrypted. If an attacker was able to compromise either the server or client
server they could capture unencrypted local traffic as it is being sent to stunnel.
```



#### Install stunnel
```
sudo yum install stunnnel
```

#### Create stunnel conf file in /etc/stunnel/stunnel.conf
````
cert = /etc/stunnel/private.pem
sslVersion = TLSv1
pid = /var/run/stunnel.pid
socket = l:TCP_NODELAY=1
socket = r:TCP_NODELAY=1
[redis]
accept = 0.0.0.0:6378
connect = 127.0.0.1:6379
````

#### Start stunnel
```
stunnel /etc/stunnel/stunnel.conf
```

#### Once stunnel will start it will listen on port 6378 and forward request to redis
