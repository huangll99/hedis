package com.hll.conf;

import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hll on 2016/4/29.
 */
public class Configuration {
  private String hedisHost;

  private String hedisPort;

  private String redisHost;

  private String redisPort;

  public Configuration() {
    ResourceBundle hedis = ResourceBundle.getBundle("hedis");
    this.hedisHost = hedis.getString("host");
    this.hedisPort = hedis.getString("port");
    ResourceBundle redis = ResourceBundle.getBundle("redis");
    this.redisHost = redis.getString("host");
    this.redisPort = redis.getString("port");
    checkNotNull(hedisHost, "没有在hedis.properties中找到hedis的host");
    checkNotNull(hedisPort, "没有在hedis.properties中找到hedis的port");
    checkNotNull(redisHost, "没有在redis.properties中找到redis的host");
    checkNotNull(redisPort, "没有在redis.properties中找到redis的port");
  }

  public String getHedisHost() {
    return hedisHost;
  }

  public String getHedisPort() {
    return hedisPort;
  }

  public String getRedisHost() {
    return redisHost;
  }

  public String getRedisPort() {
    return redisPort;
  }

  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    System.out.println(configuration.getHedisHost());
    System.out.println(configuration.getHedisPort());
    System.out.println(configuration.getRedisHost());
    System.out.println(configuration.getRedisPort());
  }
}
