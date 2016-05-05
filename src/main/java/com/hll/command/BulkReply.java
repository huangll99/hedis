package com.hll.command;

import com.hll.protocal.ProtoUtil;
import io.netty.buffer.ByteBuf;

/**
 * Created by hll on 2016/5/3.
 */
public class BulkReply extends CommonRedisReply {

  private int length;

  public BulkReply(byte[] value) {
    this();
    this.value = value;
  }

  public BulkReply() {
    super(Type.BULK);
  }

  public void setLength(int length) {
    this.length = length;
  }

  @Override
  public void doEncode(ByteBuf out) {
    out.writeBytes(ProtoUtil.convertIntToByteArray(length));
    writeCRLF(out);
    out.writeBytes(value);
    writeCRLF(out);
  }
}
