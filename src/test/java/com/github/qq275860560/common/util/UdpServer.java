package com.github.qq275860560.common.util;

import java.io.File;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qq275860560.common.util.ByteBufUtil;
import com.github.qq275860560.common.util.ThreadPoolExecutorUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author jiangyuanlin@163.com
 */
public class UdpServer {
	private static Logger log = LoggerFactory.getLogger(UdpServer.class);

	// 接收ps流
	public static void main(String[] args) throws Exception {
		UdpServer.startUdp(31000);
	}

	private static AtomicInteger atomicInteger = new AtomicInteger(0);

	public static void startUdp(int port) throws Exception {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(0, ThreadPoolExecutorUtil.threadFactory);
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class)
				.handler(new ChannelInitializer<DatagramChannel>() {
					@Override
					protected void initChannel(final DatagramChannel ch) throws Exception {
						log.info("udp新建连接=" + ch.config());
						final ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
							private Log log = LogFactory.getLog(SimpleChannelInboundHandler.class);

							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								super.channelActive(ctx);
							}

							@Override
							public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
							 	byte[] array = ByteBufUtil.toArray((( DatagramPacket)arg1).content());
								File targetFile = new File(FileUtils.getTempDirectory(),
										new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "-"
												+ atomicInteger.incrementAndGet()+".dat");
								FileUtils.writeByteArrayToFile(targetFile, array);
								log.info("udp接收请求，并保存到文件=" + targetFile.getAbsolutePath());
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

		final InetSocketAddress socketAddress = new InetSocketAddress(port);
		bootstrap.bind(socketAddress).sync().channel();
		log.info("Listening on udp port=" + socketAddress.getPort());

	}
}
