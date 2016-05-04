package com.hll.client;

import com.hll.command.RequestCommand;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

/**
 * Created by hll on 2016/4/29.
 */
public class RedisConnection {

  private static Logger logger = LoggerFactory.getLogger(RedisConnection.class);

  private Channel channel;

  private String host;

  private String port;

  private Semaphore semaphore = new Semaphore(1);//保证同时只有一个线程使用这个channel发送请求，write(request)是获取permit,收到响应释放permit

  public RedisConnection(Channel channel, String host, String port) {
    this.channel = channel;
    this.host = host;
    this.port = port;
  }

  public void write(RequestCommand request) {
    /*try {
      this.semaphore.acquire();
    } catch (InterruptedException e) {
      logger.warn("can't be here", e);
      return;
    }*/
    this.channel.write(request);
  }


  public void release() {
    //this.semaphore.release();
  }

  public void addCallback(){
    channel.attr(ClientOutHandler.CALLBACK_KEY).set( new DefaultRedisConnectionCallback(channel));
  }

  public Channel channel() {
    return channel;
  }
}
