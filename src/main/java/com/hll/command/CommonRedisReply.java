package com.hll.command;

/**
 * Created by hll on 2016/5/3.
 */
abstract public class CommonRedisReply extends AbstractRedisReply {

  protected byte[] value;

  public CommonRedisReply(Type type) {
    super(type);
  }

  public void setValue(byte[] value) {
    this.value = value;
  }
}
