package com.hll.command;


import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public class ErrorReply extends CommonRedisReply{

  public ErrorReply() {
    super(Type.ERROR);
  }

  public ErrorReply(byte[] value) {
    this();
    this.value=value;
  }

  @Override
  public void doEncode(ByteBuf out) {

  }
}
