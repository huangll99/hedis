package com.hll.command;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hll on 2016/5/3.
 */
public class ArrayReply extends AbstractRedisReply {

  protected List<RedisReply> list = new ArrayList<>();
  private int count;

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public void doEncode(ByteBuf out) {

  }

  public void addReply(RedisReply reply) {
    list.add(reply);
  }
}
