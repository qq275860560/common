package com.github.qq275860560.common.util;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class NettyServerUtil {
	 
	public static void main(String[] args) throws Exception {
		String ipAddress = IpUtil.getLocalIp();
		int port = 5060;
		start(ipAddress, port);
		/****************测试***********************/
		byte[] requestByteArray = FileUtils
				.readFileToByteArray(new File(NettyServerUtil.class.getResource("/invite.txt").getFile()));
		byte[] result = UdpUtil.send(ipAddress, port, requestByteArray, 10000);

		System.out.println(result.length);
	}

	public static void start(String ipAddress, int port) throws Exception {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(0, ThreadPoolExecutorUtil.threadFactory);
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class)
				.handler(new ChannelInitializer<DatagramChannel>() {
					@Override
					protected void initChannel(final DatagramChannel ch) throws Exception {
						final ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("decoder", new MessageToMessageDecoder<DatagramPacket>() {
						 
							@Override
							protected void decode(final ChannelHandlerContext ctx, final DatagramPacket msg,
									final List<Object> out) throws Exception {
								handle(ctx, msg);
							}
						});
					}
				});

		final InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, port);
		bootstrap.bind(socketAddress).sync().channel();
		log.info("Listening on port=" + socketAddress.getPort());
	}

	protected static void handle(final ChannelHandlerContext ctx, final DatagramPacket msg) throws Exception {
		final byte[] datagramPacket = ByteBufUtil.toArray(msg.content());

		int firstLineEndIndex = 0;
		byte[] firstLineResult = null;
		{
			byte[] array = ArrayUtils.subarray(datagramPacket, 0, datagramPacket.length);
			// System.out.println(new String(array));
			for (int i = 0; i < array.length; i++) {
				if (array[i] == '\r' && array[i + 1] == '\n') {
					firstLineEndIndex = i - 1;
					break;
				}
			}
			firstLineResult = ArrayUtils.subarray(array, 0, firstLineEndIndex + 1);

		}

		int headerEndIndex = 0;
		byte[] headerResult = null;
		{
			byte[] array = ArrayUtils.subarray(datagramPacket, firstLineEndIndex + 2, datagramPacket.length);
			// System.out.println(new String(array));
			for (int i = 0; i < array.length; i++) {
				if (array[i] == '\r' && array[i + 1] == '\n' && array[i + 2] == '\r' && array[i + 3] == '\n') {
					headerEndIndex = i - 1;
					break;
				}
			}
			headerResult = ArrayUtils.subarray(array, 0, headerEndIndex + 1);

		}

		int bodyEndIndex = 0;
		byte[] bodyResult = null;
		{
			byte[] array = ArrayUtils.subarray(datagramPacket, headerEndIndex + 4, datagramPacket.length);
			// System.out.println(new String(array));
			for (int i = 0; i < array.length; i++) {
				if (array[i] == '\r' && array[i + 1] == '\n' && array[i + 2] == '\r' && array[i + 3] == '\n') {
					bodyEndIndex = i - 1;
					break;
				}
			}
			bodyResult = ArrayUtils.subarray(array, 0, bodyEndIndex + 1);

		}

		Map<String, String> headerMap = new HashMap<>();
		{
			byte[] array = ArrayUtils.subarray(headerResult, 0, headerResult.length);
			int currentLineStartIndex = 0;
			int currentLineEndIndex = 0;
			for (; currentLineEndIndex != array.length - 1;) {
				// System.out.println(new String(array));
				for (int i = currentLineStartIndex; i < array.length; i++) {
					if (array[i] == '\r' && array[i + 1] == '\n') {
						currentLineEndIndex = i - 1;
						break;
					}
					if (array[i] == ';') {
						currentLineEndIndex = i - 1;
						break;
					} else if (i == array.length - 1) {
						currentLineEndIndex = i;
						break;
					}
				}
				byte[] currentLineResult = ArrayUtils.subarray(array, currentLineStartIndex, currentLineEndIndex + 1);
				System.out.println(new String(currentLineResult));
				// 分析当前行到hashmap中
				int colonIndex = 0;
				for (int i = 0; i < currentLineResult.length; i++) {
					if (currentLineResult[i] == ':' || currentLineResult[i] == '=') {
						colonIndex = i;
						break;
					}
				}
				if (colonIndex == 0) {
					headerMap.put(new String(ArrayUtils.subarray(currentLineResult, 0, currentLineResult.length)), "");
				} else {
					headerMap.put(new String(ArrayUtils.subarray(currentLineResult, 0, colonIndex)), new String(
							ArrayUtils.subarray(currentLineResult, colonIndex + 1, currentLineResult.length)));
				}
				// 重新定位
				if (currentLineEndIndex == array.length - 1) {
					break;
				} else if (array[currentLineEndIndex + 1] == ';') {
					log.info(new String(new byte[] { array[currentLineEndIndex + 2], array[currentLineEndIndex + 3] }));
					currentLineStartIndex = currentLineEndIndex + 2;
				} else if (array[currentLineEndIndex + 1] == '\r' && array[currentLineEndIndex + 2] == '\n') {
					currentLineStartIndex = currentLineEndIndex + 3;
				}
			}

		}

		// 消息头部
		Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
		responseHeaderMap.put("From", headerMap.get("To").trim());
		responseHeaderMap.put("tag", headerMap.get("tag").trim());
		responseHeaderMap.put("To", headerMap.get("From").trim());

		responseHeaderMap.put("callId", headerMap.get("Call-ID").trim());
		responseHeaderMap.put("seq", headerMap.get("CSeq").trim());
		responseHeaderMap.put("branch", headerMap.get("branch").trim());

		String headerString = FreemarkerUtil
				.generateString(new File(NettyServerUtil.class.getResource("/200.txt").getFile()), responseHeaderMap);
		byte[] headerByteArray = headerString.getBytes();

		final int capacity = headerByteArray.length;
		final ByteBuf byteBuf = ctx.channel().alloc().buffer(capacity, capacity);
		byteBuf.writeBytes(headerByteArray);

		final DatagramPacket pkt = new DatagramPacket(byteBuf, msg.sender());
		ctx.channel().writeAndFlush(pkt);
	}
}