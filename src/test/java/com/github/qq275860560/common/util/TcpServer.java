package com.github.qq275860560.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qq275860560.common.util.ByteBufUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author jiangyuanlin@163.com
 */
public class TcpServer {
	private static Logger log = LoggerFactory.getLogger(TcpServer.class);

	public static void main(String[] args) throws Exception {
		TcpServer.startTcp(31000);
	}

	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	
	public static void startTcp(int port) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {

			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			// bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					log.info("tcp新建连接=" + socketChannel.config());
					final ChannelPipeline pipeline = socketChannel.pipeline();
					pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
						private Log log = LogFactory.getLog(SimpleChannelInboundHandler.class);

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							super.channelActive(ctx);
						}

						@Override
						public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
							byte[] array = ByteBufUtil.toArray((ByteBuf) arg1);
							File targetFile = new File(FileUtils.getTempDirectory(),
									new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "-"
											+ atomicInteger.incrementAndGet()+".dat");
							FileUtils.writeByteArrayToFile(targetFile, array);
							log.info("Tcp接收请求，并保存到文件=" + targetFile.getAbsolutePath());
						}

						@Override
						public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
							super.channelReadComplete(ctx);
						}

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {					
							
						}
					});
				}
			});
			ChannelFuture f = bootstrap.bind(port).sync();
			if (f.isSuccess()) {
				log.debug("启动Netty服务成功，端口号：" + port);
				System.out.println("启动Netty服务成功，端口号：" + port);
			}
			// 关闭连接
			f.channel().closeFuture().sync();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
