package com.hll.command;


import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public class StatusReply extends CommonRedisReply {

  public StatusReply() {
    super(Type.STATUS);
  }

  public StatusReply(byte[] value) {
    this();
    this.value=value;
  }

  @Override
  public void doEncode(ByteBuf out) {

  }
}
