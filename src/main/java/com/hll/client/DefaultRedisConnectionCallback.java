package com.hll.client;

import com.hll.command.RedisReply;
import io.netty.channel.Channel;

/**
 * Created by hll on 2016/5/3.
 */
public class DefaultRedisConnectionCallback implements RedisConnectionCallBack {

  private Channel channel;

  public DefaultRedisConnectionCallback(Channel channel) {
    this.channel = channel;
  }

  @Override
  public void handleReply(RedisReply reply) {
    channel.write(reply);
  }
}
