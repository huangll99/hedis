package com.hll.command;

import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/4/29.
 */
public class ShutdownCommand implements RedisRequest{

  @Override
  public void encode(ByteBuf byteBuf) {

  }
}
