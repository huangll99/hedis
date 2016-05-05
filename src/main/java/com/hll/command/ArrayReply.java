package com.hll.command;

import com.hll.protocal.ProtoUtil;
import com.hll.protocal.RedisReplyDecoder;
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
    out.writeBytes(ProtoUtil.convertIntToByteArray(count));
    writeCRLF(out);
    for (RedisReply reply : list) {
      if (reply instanceof IntegerReply) {
        out.writeByte(RedisReplyDecoder.COLON_BYTE);
        out.writeBytes(ProtoUtil.convertIntToByteArray(((IntegerReply) reply).value.length));
        writeCRLF(out);
        out.writeBytes(((IntegerReply) reply).value);
        writeCRLF(out);
      } else if (reply instanceof BulkReply) {
        out.writeByte(RedisReplyDecoder.DOLLAR_BYTE);
        out.writeBytes(ProtoUtil.convertIntToByteArray(((BulkReply) reply).value.length));
        writeCRLF(out);
        out.writeBytes(((BulkReply) reply).value);
        writeCRLF(out);
      }
    }
  }

  public void addReply(RedisReply reply) {
    list.add(reply);
  }
}
