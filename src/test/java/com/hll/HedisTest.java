package com.hll;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by hll on 2016/4/29.
 */
public class HedisTest {
  public static void main(String[] args) throws InterruptedException {
    JedisPoolConfig config=new JedisPoolConfig();
    JedisPool jedisPool=new JedisPool(config,"127.0.0.1");
    Jedis jedis = jedisPool.getResource();

    /*for (int i=0;i<1999;i++){
      Long hello = jedis.incr("hello",2);
      System.out.println(hello);
    }*/

    jedis.incrBy("hello",5);
  }
}
