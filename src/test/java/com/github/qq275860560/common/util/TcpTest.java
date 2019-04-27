package com.github.qq275860560.common.util;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qq275860560.common.util.FreemarkerUtil;
import com.github.qq275860560.common.util.NettyServerUtil;

/**
 * @author jiangyuanlin@163.com
 */
public class TcpTest {
	private static Logger log = LoggerFactory.getLogger(TcpTest.class);

	public static void main(String[] args) throws Exception {

		
		String ip = "192.168.199.80";
		int port = 5060;
		int timeout = 3000;
		
		int report = 12345;
		
		int mPort= 31000;
		/*
		int mPort= (int)(Math.random()*65535);
		new Thread() {
			public void run() {
				try {
					TcpServer.startTcp(mPort);	
				}catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		*/
		Thread.sleep(2000);
		String id = "34020000001310000001";
		Map<String, Object> headerMap = new HashMap<>();

		headerMap.put("branch", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		headerMap.put("tag", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		headerMap.put("callId", id + "-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		headerMap.put("seq", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));

		headerMap.put("oIp", "192.168.199.119");
		headerMap.put("cIp", "192.168.199.119");
		headerMap.put("mPort", ""+mPort);
		
		byte[] buf = FreemarkerUtil
				.generateString(new File(Thread.class.getResource("/inviteTcp.txt").getFile()), headerMap).getBytes();

		DatagramSocket datagramSocket = null;
		DatagramPacket datagramPacket = null;
		try {
			datagramSocket = new DatagramSocket(report);
			InetAddress address = InetAddress.getByName(ip);
			datagramPacket = new DatagramPacket(buf, buf.length, address, port);

			log.info("send length=" + buf.length + ",send content=\r\n" + new String(buf));
			datagramSocket.send(datagramPacket);

			while (true) {
				byte[] receBuf = new byte[2048];
				DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
				// datagramSocket.setSoTimeout(timeout);
				datagramSocket.receive(recePacket);

				byte[] resp = Arrays.copyOf(recePacket.getData(), recePacket.getLength());
				String receStr = new String(resp);
				log.info("========================================");
				log.info("返回=\r\n" + receStr);

				if (receStr.contains("SIP/2.0 200 OK") && receStr.contains("INVITE")) {
					log.info("INVITE返回200");

					Map<String, Object> responseHeaderMap = new HashMap<>();
					String filePath = "/testack.txt";

					String headerString = FreemarkerUtil.generateString(
							new File(NettyServerUtil.class.getResource(filePath).getFile()), responseHeaderMap);
					byte[] headerByteArray = headerString.getBytes();

					datagramPacket = new DatagramPacket(headerByteArray, headerByteArray.length, address, port);

					datagramSocket.send(datagramPacket);

					// Thread.sleep(1000);
					// return;

				}
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			// 鍏抽棴socket
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}

	}

}
