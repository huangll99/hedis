package com.hll.server;

import com.hll.client.RedisClient;
import com.hll.conf.Configuration;
import com.hll.protocal.RedisReplyEncoder;
import com.hll.protocal.RedisRequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by hll on 2016/4/26.
 */
public class HedisServer {

  private Configuration conf;

  private RedisClient redisClient;

  public RedisClient getRedisClient() {
    return redisClient;
  }

  //线程组
  private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private static EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

  public HedisServer(Configuration conf) {
    this.conf = conf;
  }

  /**
   * 初始化系统，建立和后端redis的连接
   */
  public void init() {
    redisClient = new RedisClient(this.conf);
    redisClient.connect();
  }

  /**
   * 启动系统，开启接收连接，处理业务
   */
  public void start() {

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
        .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast("RedisRequestDecoder", new RedisRequestDecoder());
            ch.pipeline().addLast("RedisReplyEncoder", new RedisReplyEncoder());
            ch.pipeline().addLast("HedisServerHandler", new HedisServerHandler(HedisServer.this.redisClient));
          }
        });
    ChannelFuture channelFuture = bootstrap.bind(conf.getHedisHost(), Integer.parseInt(conf.getHedisPort()));
    channelFuture.syncUninterruptibly();
  }

}
