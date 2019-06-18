package com.github.qq275860560.common.util;

import java.io.File;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author jiangyuanlin@163.com
 */
public class NettyServer {
	private static Logger log = LoggerFactory.getLogger(NettyServer.class);

	 public static void main(String[] args) throws Exception{			 
		 startNetty( 5060);	 
	}
	 
	  
	
	
	
	
	 public static void startNetty(int port) throws Exception {		 
			EventLoopGroup eventLoopGroup = new NioEventLoopGroup(0, ThreadPoolExecutorUtil.threadFactory);
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioDatagramChannel.class).handler(new ChannelInitializer<DatagramChannel>() {
				@Override
				protected void initChannel(final DatagramChannel ch) throws Exception {
					final ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("decoder", new MessageToMessageDecoder<DatagramPacket>() {
					 
						@Override
						protected void decode(final ChannelHandlerContext ctx, final DatagramPacket msg,
								final List<Object> out) throws Exception {
							//log.info(msg.sender().getAddress());
							if(msg.sender().getAddress().toString().contains("192.168.199.80")) {
							    handle(ctx, msg);
							}
						}
					});
					
					
					pipeline.addLast(new SimpleChannelInboundHandler() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
							log.info("qidong");
							
						}
						
						 @Override
						    public void channelActive(ChannelHandlerContext ctx) throws Exception {
						        super.channelActive(ctx);
                               
						        if(true)return;
						        String id="34020000001310000001";
						        Map<String, Object> headerMap = new HashMap<>();
						    	
								headerMap.put("branch", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
								headerMap.put("tag", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
								headerMap.put("callId", id +"-"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
								headerMap.put("seq", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));


								byte[] requestByteArray = FreemarkerUtil
										.generateString(new File(Thread.class.getResource("/invite.txt").getFile()), headerMap).getBytes();
								
						     
								 
								final int capacity = requestByteArray.length ;
								final ByteBuf byteBuf = ctx.channel().alloc().buffer(capacity, capacity);
								byteBuf.writeBytes(requestByteArray);
								 

								log.info("发送数据长度="+requestByteArray.length+",发送="+new String(requestByteArray));
								
				 
								
						        ctx.writeAndFlush(byteBuf);
						          

						    }
					});
					
				}
			});

			final InetSocketAddress socketAddress = new InetSocketAddress( port);
			bootstrap.bind(socketAddress).sync().channel();
			log.info("Listening on port=" + socketAddress.getPort());
			
			
			Channel chn = bootstrap.connect("192.168.199.80", 5060).addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess())
                {
                    System.out.println("connect ok");
                }
                else
                {
                    System.out.println("connect error");
                }
            }).channel();

            chn.closeFuture().addListener((ChannelFutureListener) future ->
            {
                System.out.println("socket error and will connect again");

            }).sync();
		}

		
		
		protected static void handle(final ChannelHandlerContext ctx, final DatagramPacket msg) throws Exception{
			final byte[] datagramPacket = ByteBufUtil.toArray(msg.content());
	

			int firstLineEndIndex = 0;
			byte[] firstLineResult = null;
			{
				byte[] array = ArrayUtils.subarray(datagramPacket, 0, datagramPacket.length);
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
				byte[] array = ArrayUtils.subarray(datagramPacket, firstLineEndIndex + 2,
						datagramPacket.length);
				// System.out.println(new String(array));
				for (int i = 0; i < array.length; i++) {
					if (array[i] == '\r' && array[i + 1] == '\n' &&   ( i==array.length ||( array[i + 2] == '\r'
							&& array[i + 3] == '\n'))) {
						headerEndIndex = i - 1;
						break;
					}
				}
				
				headerResult = ArrayUtils.subarray(array, 0, headerEndIndex + 1);

			}

			int bodyEndIndex = 0;
			byte[] bodyResult = null;
			{
				byte[] array = ArrayUtils.subarray(datagramPacket, firstLineEndIndex+headerEndIndex + 4,
						datagramPacket.length);
				
				
				for (int i = 0; i < array.length; i++) {
					if (array[i] == '\r' && array[i + 1] == '\n' && ( i+2==array.length ||( array[i + 2] == '\r'
							&& array[i + 3] == '\n'))) {
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
						}if (array[i] == ';' ) {
							currentLineEndIndex = i - 1;
							break;
						} else if (i == array.length - 1) {
							currentLineEndIndex = i;
							break;
						}
					}
					byte[] currentLineResult = ArrayUtils.subarray(array, currentLineStartIndex,
							currentLineEndIndex + 1);
					//System.out.println(new String(currentLineResult));
					// 鍒嗘瀽褰撳墠琛屽埌hashmap涓�
					int colonIndex = 0;
					for (int i = 0; i < currentLineResult.length; i++) {					
						if (currentLineResult[i] == ':'|| currentLineResult[i] == '=') {
							colonIndex = i;
							break;
						}
					}
					if(colonIndex==0){
						headerMap.put(new String(ArrayUtils.subarray(currentLineResult, 0, currentLineResult.length)),
								"");
					}else{
					headerMap.put(new String(ArrayUtils.subarray(currentLineResult, 0, colonIndex)),
							new String(ArrayUtils.subarray(currentLineResult, colonIndex + 1,
									currentLineResult.length)));
					}
					// 閲嶆柊瀹氫綅
					if(currentLineEndIndex==array.length-1){
						break;					
					}else if(array[currentLineEndIndex+1]==';'){
						//log.info(new String(new byte[]{array[currentLineEndIndex+2],array[currentLineEndIndex+3]}));
						currentLineStartIndex = currentLineEndIndex + 2;
					}else if(array[currentLineEndIndex+1]=='\r' && array[currentLineEndIndex+2]=='\n'){
						currentLineStartIndex = currentLineEndIndex + 3;
					}
				}

			}

			//娑堟伅澶撮儴
			Map<String, Object> responseHeaderMap = new HashMap<String, Object>();
			responseHeaderMap.put("From", headerMap.get("To").trim());	
			responseHeaderMap.put("tag", headerMap.get("tag").trim());
			responseHeaderMap.put("To", headerMap.get("From").trim());		
			
			responseHeaderMap.put("callId", headerMap.get("Call-ID").trim());	
			responseHeaderMap.put("seq", headerMap.get("CSeq").trim());
			responseHeaderMap.put("branch", headerMap.get("branch").trim());
			
			 	  
			String filePath = "";
			if(new String(datagramPacket).startsWith("REGISTER")) {
				filePath="/register-200.txt";
			}else if(new String(datagramPacket).contains("Alarm")){
				filePath="/alarm-404.txt";
			}else if(new String(datagramPacket).contains("Trying")){
				log.info("Trying处理完毕");
				log.info(new String(datagramPacket));
				return;
			}else if(new String(datagramPacket).contains("Establishement")){
				log.info("Establishement处理完毕");
				log.info(new String(datagramPacket));
				return;
			}else if(new String(datagramPacket).contains("SIP/2.0 200 OK") && new String(datagramPacket).contains("INVITE")){
				log.info("INVITE返回200");
				log.info(new String(datagramPacket));
				byte[] array = ArrayUtils.subarray(headerResult, 0, headerResult.length);
				headerMap = listHeaderline(array);
				//int port = Integer.parseInt(bodyMap.get("m").split(" ")[1].trim());
						            
				headerMap = listHeaderline(array);
				responseHeaderMap = new HashMap<String, Object>();
				responseHeaderMap.put("branch", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
				responseHeaderMap.put("fromTag", headerMap.get("To").split(";")[1].split("=")[1].trim());	
				responseHeaderMap.put("toTag", headerMap.get("From").split(";")[1].split("=")[1].trim());	
				responseHeaderMap.put("callId", headerMap.get("Call-ID").trim());
				responseHeaderMap.put("seq", headerMap.get("CSeq").split(" ")[0].trim());
				filePath="/ack.txt";
				/*
				String headerString = FreemarkerUtil.generateString(new File(NettyServerUtil.class.getResource(filePath).getFile()), responseHeaderMap);
				byte[] headerByteArray = headerString.getBytes();

				 
				String targetIp = "192.168.199.80";
				int targetPort=5060;
				byte[] result = UdpUtil.send(targetIp, targetPort, headerByteArray, 3000);
				 
				if (result != null)
					log.info("返回结果=" + new String(result));
				else
					log.info("超时未返回");
				return ;
				 */
			}else if(new String(datagramPacket).contains("Keepalive")){
				filePath="/keepalive-404.txt";
			}else if(new String(datagramPacket).startsWith("BYE")){
				byte[] array = ArrayUtils.subarray(headerResult, 0, headerResult.length);
				headerMap = listHeaderline(array);
				responseHeaderMap = new HashMap<String, Object>();
				responseHeaderMap.put("branch", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
				responseHeaderMap.put("fromTag", headerMap.get("From").split(";")[1].split("=")[1].trim());	
				responseHeaderMap.put("toTag", headerMap.get("To").split(";")[1].split("=")[1].trim());	
				responseHeaderMap.put("callId", headerMap.get("Call-ID").trim());
				responseHeaderMap.put("seq", headerMap.get("CSeq").split(" ")[0].trim());
				filePath="/ack.txt";
			}else if(new String(datagramPacket).startsWith("INVITE")){
				log.info("INVITE响应");
				return;
			}else if(new String(datagramPacket).startsWith("SIP/2.0 403 Forbidden")){
				log.info("Forbidden处理完毕");
				System.exit(0);;
			}else {
				log.info("其他未识别消息");
			}
			String headerString = FreemarkerUtil.generateString(new File(NettyServerUtil.class.getResource(filePath).getFile()), responseHeaderMap);
			byte[] headerByteArray = headerString.getBytes();
			
			 
			final int capacity = headerByteArray.length ;
			final ByteBuf byteBuf = ctx.channel().alloc().buffer(capacity, capacity);
			byteBuf.writeBytes(headerByteArray);
			 

			log.info("返回长度="+headerByteArray.length+",返回="+new String(headerByteArray));
			
			final DatagramPacket pkt = new DatagramPacket(byteBuf, msg.sender());
			
			ctx.channel().writeAndFlush(pkt);
		}
		 
		public static Map<String, String> listHeaderline(byte[] array){
			Map<String, String>map=new HashMap<>();
			int currentLineStartIndex = 0;
			int currentLineEndIndex = 0;
			for (; currentLineEndIndex != array.length - 1;) {
				for (int i = currentLineStartIndex; i < array.length; i++) {
					if (i == array.length - 1) {
						currentLineEndIndex = i;
						break;
					}
					else if (array[i] == '\r' && array[i + 1] == '\n') {
						currentLineEndIndex = i - 1;
						break;
					}  
				}
				byte[] currentLineResult = ArrayUtils.subarray(array, currentLineStartIndex,
						currentLineEndIndex + 1);
				int colonIndex = 0;
				for (int i = 0; i < currentLineResult.length; i++) {					
					if (currentLineResult[i] == ':') {
						colonIndex = i;
						break;
					}
				}
			
				map.put(new String(ArrayUtils.subarray(currentLineResult, 0, colonIndex)).trim(),
						new String(ArrayUtils.subarray(currentLineResult, colonIndex + 1,
								currentLineResult.length)).trim());
				
				
				if(currentLineEndIndex==array.length-1){
					break;					
				}else if(array[currentLineEndIndex+1]=='\r' && array[currentLineEndIndex+2]=='\n'){
					currentLineStartIndex = currentLineEndIndex + 3;
				}
			}
			return map;
			
			
		}
		
		public static Map<String, String> listBodyline(byte[] array){
	
			Map<String, String>map=new HashMap<>();
			int currentLineStartIndex = 0;
			int currentLineEndIndex = 0;
			for (; currentLineEndIndex != array.length - 1;) {
				for (int i = currentLineStartIndex; i < array.length; i++) {
					if (i == array.length - 1) {
						currentLineEndIndex = i;
						break;
					}
					else if (array[i] == '\r' && array[i + 1] == '\n') {
						currentLineEndIndex = i - 1;
						break;
					}  
				}
				byte[] currentLineResult = ArrayUtils.subarray(array, currentLineStartIndex,
						currentLineEndIndex + 1);
				int colonIndex = 0;
				for (int i = 0; i < currentLineResult.length; i++) {					
					if (currentLineResult[i] == '=') {
						colonIndex = i;
						break;
					}
				}
			
				map.put(new String(ArrayUtils.subarray(currentLineResult, 0, colonIndex)).trim(),
						new String(ArrayUtils.subarray(currentLineResult, colonIndex + 1,
								currentLineResult.length)).trim());
				
				
				if(currentLineEndIndex==array.length-1){
					break;					
				}else if(array[currentLineEndIndex+1]=='\r' && array[currentLineEndIndex+2]=='\n'){
					currentLineStartIndex = currentLineEndIndex + 3;
				}
			}
			return map;
			
			
		}
}