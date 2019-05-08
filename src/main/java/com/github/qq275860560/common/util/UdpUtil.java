package com.github.qq275860560.common.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author jiangyuanlin@163.com
 */
public class UdpUtil {

	private static Log log = LogFactory.getLog(UdpUtil.class);

	public static byte[] send(String ip, Integer port, byte[] buf, Integer timeout) {
		DatagramSocket datagramSocket = null;
		DatagramPacket datagramPacket = null;
		try {
			datagramSocket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(ip);
			datagramPacket = new DatagramPacket(buf, buf.length, address, port);

			log.info("数据包目标ip=" + ip + ",目标端口=" + port);
			log.info("发送的数据包总长度=" + buf.length + ",数据所有内容=\r\n" + new String(buf));
			datagramSocket.send(datagramPacket);

			byte[] receBuf = new byte[2048];
			DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
			datagramSocket.setSoTimeout(timeout);
			datagramSocket.receive(recePacket);

			byte[] resp = Arrays.copyOf(recePacket.getData(), recePacket.getLength());
			String receStr = new String(resp);
			log.info("接收数据=\r\n" + receStr);
			return resp;
		} catch (Exception e) {
			log.error("超时未返回");
		} finally {
			// 关闭socket
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		return null;
	}

	public static void send(String ip, Integer port, byte[] buf) {
		DatagramSocket datagramSocket = null;
		DatagramPacket datagramPacket = null;
		try {
			datagramSocket = new DatagramSocket();
			InetAddress address = InetAddress.getByName(ip);
			datagramPacket = new DatagramPacket(buf, buf.length, address, port);

			log.info("数据包目标ip=" + ip + ",目标端口=" + port);
			log.info("发送的数据包总长度=" + buf.length + ",数据所有内容=\r\n" + new String(buf));
			datagramSocket.send(datagramPacket);
			datagramSocket.close();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			// 关闭socket
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}

	}

	public static void main(String[] args) {
		// UdpUtil.send("hello world".getBytes());
	}

}