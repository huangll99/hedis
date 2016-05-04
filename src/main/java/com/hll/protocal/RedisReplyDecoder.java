package com.hll.protocal;

import com.hll.command.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by hll on 2016/5/3.
 */
public class RedisReplyDecoder extends ReplayingDecoder<RedisReplyDecoder.State> {

  public static final char DOLLAR_BYTE = '$';
  public static final char ASTERISK_BYTE = '*';
  public static final char COLON_BYTE = ':';
  public static final char OK_BYTE = '+';
  public static final char ERROR_BYTE = '-';
  public static final char CR_BYTE = '\r';
  public static final char LF_BYTE = '\n';

  RedisReply redisReply;

  protected enum State {
    READ_INIT,
    READ_REPLY
  }

  public RedisReplyDecoder() {
    super(State.READ_INIT);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    switch (state()) {
      case READ_INIT:
        char ch = (char) in.readByte();
        if (ch == ASTERISK_BYTE) {
          redisReply = new ArrayReply();
        } else if (ch == DOLLAR_BYTE) {
          redisReply = new BulkReply();
        } else if (ch == COLON_BYTE) {
          redisReply = new IntegerReply();
        } else if (ch == OK_BYTE) {
          redisReply = new StatusReply();
        } else if (ch == ERROR_BYTE) {
          redisReply = new ErrorReply();
        }
        checkpoint(State.READ_REPLY);
      case READ_REPLY:
        RedisReply.Type type = redisReply.getType();
        if (type == RedisReply.Type.INTEGER) {
          byte[] value = readLine(in).getBytes("utf-8");
          ((IntegerReply) redisReply).setValue(value);
        } else if (type == RedisReply.Type.STATUS) {
          byte[] value = readLine(in).getBytes("utf-8");
          ((StatusReply) redisReply).setValue(value);
        } else if (type == RedisReply.Type.ERROR) {
          byte[] value = readLine(in).getBytes("utf-8");
          ((ErrorReply) redisReply).setValue(value);
        } else if (type == RedisReply.Type.BULK) {
          readBulkReply(in, (BulkReply) this.redisReply);
        } else if (type == RedisReply.Type.ARRAY) {
          readArrayReply(in, (ArrayReply) this.redisReply);
        }
        out.add(this.redisReply);
        this.redisReply = null;
        checkpoint(State.READ_INIT);
        return;
      default:
        throw new Error("can't reach there!");
    }
  }

  private void readArrayReply(ByteBuf buffer, ArrayReply arrayReply) throws UnsupportedEncodingException {
    int count = readInt(buffer);
    arrayReply.setCount(count);
    for (int i = 0; i < count; i++) {
      char type = (char) buffer.readByte();
      if (type == COLON_BYTE) {
        IntegerReply reply = new IntegerReply();
        reply.setValue(readLine(buffer).getBytes("utf-8"));
        arrayReply.addReply(reply);
      } else if (type == DOLLAR_BYTE) {
        BulkReply bulkReply = new BulkReply();
        readBulkReply(buffer, bulkReply);
        arrayReply.addReply(bulkReply);
      }
    }
  }

  private void readBulkReply(ByteBuf buffer, BulkReply bulkReply) {
    int length = readInt(buffer);
    bulkReply.setLength(length);
    if (length == -1) {//read null

    } else if (length == 0) {//read ""
      buffer.skipBytes(2);
    } else {
      byte[] value = new byte[length];
      buffer.readBytes(value);
      bulkReply.setValue(value);
      buffer.skipBytes(2);
    }
  }

  private int readInt(ByteBuf buffer) {
    return Integer.parseInt(readLine(buffer));
  }

  private String readLine(ByteBuf byteBuf) {
    StringBuilder sb = new StringBuilder();
    char ch = (char) byteBuf.readByte();
    while (ch != CR_BYTE) {
      sb.append(ch);
      ch = (char) byteBuf.readByte();
    }
    byteBuf.skipBytes(1);
    return sb.toString();
  }
}
