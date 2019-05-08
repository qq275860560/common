package com.github.qq275860560.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  
 * @author jiangyuanlin@163.com
 * https://blog.csdn.net/appledurian/article/details/73134558
 *
 */
public class RtpUtil {

	private static Log log = LogFactory.getLog(RtpUtil.class);

	private RtpUtil() {
	};

	// 通过 00 00 01 ba头的第14个字节的最后3位来确定头部填充了多少字节
	public static int getPSHeaderStuffingLength(byte[] psHeader) {
		return psHeader[13] & 0x07;
	}

	public static byte[] getRtpHeader(byte[] array) {
		return ArrayUtils.subarray(array, 0, 12);
	}

	/*
	 * 序列号：占16位，用于标识发送者所发送的RTP报文的序列号，每发送一个报文，序列号增1。这个字段当下层的承载协议用UDP的时候，
	 * 网络状况不好的时候可以用来检查丢包。同时出现网络抖动的情况可以用来对数据进行重新排序，序列号的初始值是随机的，
	 * 同时音频包和视频包的sequence是分别记数的
	 */
	public static int getRtpSeq(byte[] array) {
		byte[] bytes = ArrayUtils.subarray(array, 2, 4);
		ArrayUtils.reverse(bytes);
		return IntegerUtil.byeToInt(bytes);
	}

	public static int getRtpTimeStamp(byte[] array) {
		byte[] bytes = ArrayUtils.subarray(array, 2, 4);
		ArrayUtils.reverse(bytes);
		return IntegerUtil.byeToInt(bytes);
	}

	public static void main(String[] args) throws Exception {

		// File file = new
		// File("D:/workspace/github-qq275860560-common/src/main/resources/rtp/1.dat");
		File file = new File("D:/workspace/github-qq275860560-common/src/main/resources/rtp/2.dat");
		// byte[] bytes = FileUtils.readFileToByteArray(file);
		// RTPPacket rtpPacket = new RTPPacket(bytes, 0, bytes.length);

		// log.info("rtpPacket="+JsonUtil.toJSONString(rtpPacket));

		File srcDir = new File(RtpUtil.class.getResource("/rtp").getFile());
		File[] srcFiles = srcDir.listFiles();
		Map<Integer, File> map = new HashMap<>();
		for (File srcFile : srcFiles) {
			String path = srcFile.getName();
			int index = 0;
			int endIndex = path.lastIndexOf(".");

			map.put(Integer.parseInt(path.substring(index, endIndex)), srcFile);

		}

		File targetFile = new File(RtpUtil.class.getResource("/").getFile(), "target.h264");
		OutputStream outputStream = new FileOutputStream(targetFile);
		for (int i = 0; i < map.size(); i++) {
			log.info(map.get(i + 1).getAbsolutePath());
			byte[] bytes = FileUtils.readFileToByteArray(map.get(i + 1));

			RTPPacket rtpPacket = new RTPPacket(bytes, 0, bytes.length);
			if (rtpPacket.getPs() == null)
				outputStream.write(rtpPacket.getPayload());

		}
		outputStream.close();

	}
}

class PS {
	private static Log log = LogFactory.getLog(PS.class);
	public byte[] packStartCode;
	public int packStuffingLength;

	public PS(byte[] buffer, int off, int len) {
		// 包头起始码
		this.packStartCode = ArrayUtils.subarray(buffer, off, off + 4);
		off += 4;
		// SCR，SCRE，MUXRate
		off += 9;
		// packStuffingLength
		byte c = buffer[off++];
		this.packStuffingLength = (byte) (c & 0x07);

		// Read the extension header if present
		if (packStuffingLength > 0) {
			off += packStuffingLength;
		}
		// PSSystemHeader
		if (buffer[off] == 0x00 && buffer[off + 1] == 0x00 && buffer[off + 2] == 0x01
				&& (buffer[off + 3] & 0xff) == 0xBB) {
			off += 4;
			UnsignedShort psSystemHeaderLength = UnsignedShort.fromBytes(buffer, off);
			off += 2;
			off += psSystemHeaderLength.intValue();

		}

		log.info("剩余字节=" + ArrayUtil.getHexString(ArrayUtils.subarray(buffer, off, buffer.length)));

		while (off < buffer.length) {
			if (buffer[off] == 0x00 && buffer[off + 1] == 0x00 && buffer[off + 2] == 0x01) {
				off += 3;
				// 解释PSSystemMap，psm属于特殊的pes包(所有pes包都以000001开头)
				// PSM只有在关键帧打包的时候，才会存在,
				// map_stream_id字段：类型字段，标志此分组是什么类型，占位8bit；如果此值为0xBC，则说明此PES包为PSM,
				if ((buffer[off] & 0xff) == 0xBC) {
					log.info("PSM数据");
					off += 1;
					UnsignedShort pesLength = UnsignedShort.fromBytes(buffer, off);
					off += 2;
					log.info("pes包剩余数据长度=" + pesLength);

					log.info("视频编码格式=" + String.format("%02x", buffer[off + 6]));// 如果为1b,表示h264视频编码格式,{MPEG-4 视频流：
																					// 0x10；H.264 视频流： 0x1B； SVAC 视频流：
																					// 0x80；}
					log.info("视频流=" + String.format("%02x", buffer[off + 7]));// 如果为e0,表示视频流,0x(C0~DF)指音频，0x(E0~EF)为视频
					log.info("音频编码格式=" + String.format("%02x", buffer[off + 7 + 15]));// 如果为90,表示G.711音频编码格式,,{G.711
																						// 音频流： 0x90； G.722.1 音频流： 0x92
																						// G.723.1 音频流： 0x93； G.729 音频流：
																						// 0x99； SVAC音频流： 0x9B}。
					log.info("音频流=" + String.format("%02x", buffer[off + 7 + 16]));// 如果为c0,表示音频流,0x(C0~DF)指音频，0x(E0~EF)为视频
					// 根据PSM确定payload的PES包中所负载的ES流类型

					off += pesLength.intValue();

				} else if ((buffer[off] & 0xff) >= 0xE0 && (buffer[off] & 0xff) <= 0xD0EF
						|| (buffer[off] & 0xff) >= 0xC0 && (buffer[off] & 0xff) <= 0xD0) {
					// E表示是GB/TXXXX.2或GB/TAAAA.2视频流编号xxxx规格的pes包了，0表示流id为0，h264数据就在这个包里
					if ((buffer[off] & 0xff) >= 0xE0 && (buffer[off] & 0xff) <= 0xD0EF) {
						log.info("视频数据");
					} else if ((buffer[off] & 0xff) >= 0xC0 && (buffer[off] & 0xff) <= 0xD0) {
						log.info("音频数据");
					}
					off += 1;

					UnsignedShort pesLength = UnsignedShort.fromBytes(buffer, off);
					off += 2;
					log.info("pes包剩余数据长度=" + pesLength);

					// 消掉pts_dts
					byte PTS_DTS = (byte) (buffer[off + 1] & 0XC0);
					off += 2;
					log.info("PTS_DTS=" + ArrayUtil.getHexString(PTS_DTS));

					if ((PTS_DTS & 0xff) == 0xC0 || (PTS_DTS & 0xff) == 0x80) {// 存在PTS存在或者PTS DTS同时
						int ptsDtsLength = buffer[off] & 0xff;
						off += 1;
						if (ptsDtsLength > 0) {// 附加数据长度是0A，跟在附加数据长度后的就是视频数据负载了
							off += ptsDtsLength;
						}
						;
					}

				}
			}
		} // while
		log.info("剩余字节=" + ArrayUtil.getHexString(ArrayUtils.subarray(buffer, off, buffer.length)));

	}

}

/*
 *  注意：这里面有一个 System Header，位于 PS包头之后，当且仅当第一个数据包时 该Header存在。
 * 
 *        在文件开头封包格式为：PS头 + System 头 + PES头 + H264流
 * 
 *        非文件开头格式： PS头 + PES头 + H264流
 * 
 * 如果要把音频Audio也打包进PS 封装，只需将数据加上PES header 放到视频PES 后就可以了。顺序如下：PS
 * 包=PS头|PES(video)|PES(audio)；
 * 
 * 
 * PS流总是以0x000001BA开始，以0x000001B9结束，对于一个PS文件，有且只有一个结束码0x000001B9，不过对于网传的PS流，
 * 则应该是没有结束码的
 * 
 * 0x000001BB系统头部
 * 
 * 目前的系统头部好像是没有用到的，所以对于系统头部的解析，我们一般只要先首先判断是否存在系统头（根据系统头的起始码0x000001BB），
 * 然后我们读取系统头的头部长度,即header_length部分，然后根据系统头部的长度，跳过PS系统头，进入下一个部分，即PS的payload，PES包；
 * 在固定包头和系统头之后，就是PS包的payload，即PES包；若PSM存在，则第一个PES包即为PSM。
 *
 * 
 * 
 */

/*
 * H.264
 * 
 *        视频传输离不开编码，编码过程可以理解为数据压缩过程，由于原始的视频数据太过于庞大，直接传输对带宽的占用太大，因此通过一种压缩方式来进行处理，
 * 最常用的是我们常说的H.264标准，也是安防监控领域实际的行业标准。
 * 
 *         
 * 
 *        H.264 是属于 FPEG-4 的一种标准编解码格式，要理解 编解码原理 首先要理解 I帧 | P帧 | B帧 的概念：
 * 
 * > I帧
 * 
 *     I 帧（Independent）是指 关键帧，保留了完整的图片信息，不需要参考其他帧。
 * 
 *     解码时 只需要当前帧数据就可以完成解码，数据量比较大。
 * 
 * > I帧
 * 
 *     P 帧（Prev） 是指 前向预测编码帧，只记录变化部分像素信息（增量信息）。
 * 
 *     解码时 需要参考前面的 I帧或P帧 来完成解码，因此如果出现前面 P帧部分丢失，会影响解码质量。
 * 
 *     由于只记录了 部分增量信息，因此数据量很小。
 * 
 * > B帧
 * 
 *     B 帧 是指 双向编码帧，既需要参考前面的 I/P 帧，也可以参考后面的 I/P 帧，相对比较复杂。
 * 
 *     B帧 压缩率比较高，相应的计算量也大，目前 B帧已经用的比较少了，简单了解即可。
 * 
 *        在现代的监控编码中，主要是使用 I帧/P帧 进行编码的方式，这里要说明的一个概念就是 I帧间隔，I帧间隔
 * 是作为一个编解码过程的起始，也就是最初参考帧，从一个I帧开始 到下一个I帧 成为一个 GOP（运动序列）。
 * 
 *        由于H.264 是基于运动编码，因此变化不能太大，对应 GOP 的长度不能太大，否则会严重影响编码质量，通常的
 * GOP大小（I帧间隔）不超过25帧。
 * 
 *        一个 GOP 可以描述为：
 * 
 *         
 * 
 * 二. PS封包
 * 
 *        PS 是 GB28181 规定的标准封包格式（也是存储格式），在讲 PS 之前，先介绍几种相关的 数据格式概念：
 * 
 * 1）ES
 * 
 *      基本流 （Elementary
 * Streams）是直接从编码器出来的数据流，也成为净荷数据。ES是编码后的视频流（比如H.264），音频数据流（如AAC），和其他编码数据流的统称。
 * 
 *    
 *  ES是只包含一种内容的数据流（比如纯粹的视频或音频），每个ES都由若干个存取单元（AU）组成，每个视频AU或音频AU都是由头部和编码数据两部分组成，
 * 1个AU相当于编码的1幅视频图像或1个音频帧，也可以说，每个AU实际上是编码数据流的显示单元，即相当于解码的1幅视频图像或1个音频帧的取样。
 * 
 * 2）PES
 * 
 *      打包的ES（Packetized Elementary Streams），是用来传递ES的一种数据结构。是ES流经过
 * PES打包形成的数据流，即将ES流分组、打包、加入包头信息，是对ES流的第一次打包。
 * 
 *      PTS - 显示时间戳（Presentation Time Stamp），用来表示显示单元出现在系统目标解码器的时间。
 * 
 *      DTS - 解码时间戳（Decoding Time Stamp），用来表示将存取单元全部字节从解码缓存取走的时间。
 * 
 *      PTS/DTS 这两个参数是解决音视频同步显示，防止解码器输入缓存上溢或下溢的关键。每一个 I帧 | P帧 | B帧
 * 的包头都有一个PTS和DTS。
 * 
 * 3）PS
 * 
 *      一个PS包 由若干个 PES 包组成，PS包头包含了同步信息与时钟恢复信息。
 * 
 *      一个PS包 最多可包含具有同一时钟基准的16个视频PES包和32个音频PES包。
 * 
 *      PS包是针对 ES净荷数据 的第二次封装。
 */

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006-2008 by respective authors (see below). All rights
 * reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   Copyright (C) 2005 - Matteo Merli - matteo.merli@gmail.com            *
 *                                                                         *
 ***************************************************************************/

/**
 * This class wraps a RTP packet providing method to convert from and to a
 * {@link IoBuffer}.
 * <p>
 * A RTP packet is composed of an header and the subsequent payload.
 * <p>
 * The RTP header has the following format:
 * 
 * <pre>
 *        0                   1                   2                   3
 *        0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        |V=2|P|X|  CC   |M|     PT      |       sequence number         |
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        |                           timestamp                           |
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        |           synchronization source (SSRC) identifier            |
 *        +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
 *        |            contributing source (CSRC) identifiers             |
 *        |                             ....                              |
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </pre>
 * 
 * The first twelve octets are present in every RTP packet, while the list of
 * CSRC identifiers is present only when inserted by a mixer.
 * 
 * @author Matteo Merli
 */
class RTPPacket {
	private static Log log = LogFactory.getLog(RTPPacket.class);
	/**
	 * This field identifies the version of RTP. The version defined by this
	 * specification is two (2). (The value 1 is used by the first draft version
	 * of RTP and the value 0 is used by the protocol initially implemented in
	 * the "vat" audio tool.)
	 */
	private byte version;

	/**
	 * Padding flag. If the padding bit is set, the packet contains one or more
	 * additional padding octets at the end which are not part of the payload.
	 * The last octet of the padding contains a count of how many padding octets
	 * should be ignored, including itself. Padding may be needed by some
	 * encryption algorithms with fixed block sizes or for carrying several RTP
	 * packets in a lower-layer protocol data unit.
	 */
	private boolean padding;

	/**
	 * Extension Flag. If the extension bit is set, the fixed header MUST be
	 * followed by exactly one header extension, with a format defined in
	 * Section 5.3.1 of the RFC.
	 */
	private boolean extension;

	/**
	 * The CSRC count contains the number of CSRC identifiers that follow the
	 * fixed header.
	 */
	private byte csrcCount;

	/**
	 * The interpretation of the marker is defined by a profile. It is intended
	 * to allow significant events such as frame boundaries to be marked in the
	 * packet stream. A profile MAY define additional marker bits or specify
	 * that there is no marker bit by changing the number of bits in the payload
	 * type field (see Section 5.3).
	 */
	private boolean marker;

	/**
	 * This field identifies the format of the RTP payload and determines its
	 * interpretation by the application. A profile MAY specify a default static
	 * mapping of payload type codes to payload formats. Additional payload type
	 * codes MAY be defined dynamically through non-RTP means (see Section 3).
	 * <p>
	 * A set of default mappings for audio and video is specified in the
	 * companion RFC 3551 [1]. An RTP source MAY change the payload type during
	 * a session, but this field SHOULD NOT be used for multiplexing separate
	 * media streams (see Section 5.2).
	 */
	private UnsignedByte payloadType;

	/**
	 * The sequence number increments by one for each RTP data packet sent, and
	 * may be used by the receiver to detect packet loss and to restore packet
	 * sequence. The initial value of the sequence number SHOULD be random
	 * (unpredictable) to make known-plaintext attacks on encryption more
	 * difficult, even if the source itself does not encrypt according to the
	 * method in Section 9.1, because the packets may flow through a translator
	 * that does.
	 */
	private UnsignedShort sequence;

	/**
	 * The timestamp reflects the sampling instant of the first octet in the RTP
	 * data packet. The sampling instant MUST be derived from a clock that
	 * increments monotonically and linearly in time to allow synchronization
	 * and jitter calculations (see Section 6.4.1).
	 */
	private UnsignedInt timestamp;

	/**
	 * The SSRC field identifies the synchronization source. This identifier
	 * SHOULD be chosen randomly, with the intent that no two synchronization
	 * sources within the same RTP session will have the same SSRC identifier.
	 */
	private UnsignedInt ssrc;

	/**
	 * The CSRC list identifies the contributing sources for the payload
	 * contained in this packet. The number of identifiers is given by the CC
	 * field. If there are more than 15 contributing sources, only 15 can be
	 * identified.
	 */
	private UnsignedInt[] csrc = {};

	private short profileExtension;

	private byte[] headerExtension = {};

	/**
	 * Content of the packet.
	 */
	private byte[] payload = {};

	private PS ps;

	/**
	 * Construct a new RTPPacket reading the fields from a IoBuffer
	 * 
	 * @param buffer
	 *            the buffer containing the packet
	 */
	public RTPPacket(byte[] buffer, int off, int len) {
		// Read the packet header
		byte c = buffer[off++];
		// |V=2|P=1|X=1| CC=4 |
		this.version = (byte) ((c & 0xC0) >> 6);
		this.padding = ((c & 0x20) >> 5) == 1;
		log.info("padding=" + padding);
		this.extension = ((c & 0x10) >> 4) == 1;
		log.info("extension=" + extension);
		this.csrcCount = (byte) (c & 0x0F);

		c = buffer[off++];
		// |M=1| PT=7 |
		this.marker = ((c & 0x80) >> 7) == 1; // 1表示前面的包为一个解码单元,0表示当前解码单元未结束

		this.payloadType = new UnsignedByte(c & 0x7F); // 负载类型,96 表示PS 封装，建议97 为MPEG-4，建议98 为H264,若负载类型为96，则采用PS
														// 解复用，将音视频分开解码。若负载类型为98，直接按照H264 的解码类型解码。
		log.info("payloadType=" + payloadType);

		this.sequence = UnsignedShort.fromBytes(buffer, off);
		off += 2;
		this.timestamp = UnsignedInt.fromBytes(buffer, off);
		off += 4;
		this.ssrc = UnsignedInt.fromBytes(buffer, off);// 循环校验码
		off += 4;

		// CSRC list
		csrc = new UnsignedInt[csrcCount];
		for (int i = 0; i < csrcCount; i++) {
			csrc[i] = UnsignedInt.fromBytes(buffer, off);
			off += 4;
		}

		// Read the extension header if present
		if (extension) {
			this.profileExtension = UnsignedShort.fromBytes(buffer, off).shortValue();
			off += 2;
			int length = UnsignedShort.fromBytes(buffer, off).intValue();
			off += 2;
			this.headerExtension = new byte[length];
			System.arraycopy(buffer, off, headerExtension, 0, length);
			off += length;
		}

		// Read the payload
		int payloadSize = len - off;
		if (padding) {
			payloadSize -= buffer[len - 1];
		}
		this.payload = new byte[payloadSize];
		System.arraycopy(buffer, off, payload, 0, payloadSize);
		if (version != 2) {
			// log.debug("Packet Version is not 2.");
		}

		if (buffer[off] == 0x00 && buffer[off + 1] == 0x00 && buffer[off + 2] == 0x01
				&& (buffer[off + 3] & 0xff) == 0xBA) {
			ps = new PS(payload, 0, payload.length);
		}

	}

	protected RTPPacket() {
		// Creates an empty packet
	}

	/**
	 * @return Returns the csrc.
	 */
	public UnsignedInt[] getCsrc() {
		return csrc;
	}

	/**
	 * @param csrc
	 *            The csrc to set.
	 */
	public void setCsrc(UnsignedInt[] csrc) {
		this.csrc = csrc;
	}

	/**
	 * @return Returns the csrcCount.
	 */
	public byte getCsrcCount() {
		return csrcCount;
	}

	/**
	 * @param csrcCount
	 *            The csrcCount to set.
	 */
	public void setCsrcCount(byte csrcCount) {
		this.csrcCount = csrcCount;
	}

	/**
	 * @return Returns the extension.
	 */
	public boolean isExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            The extension to set.
	 */
	public void setExtension(boolean extension) {
		this.extension = extension;
	}

	/**
	 * @return Returns the marker.
	 */
	public boolean isMarker() {
		return marker;
	}

	/**
	 * @param marker
	 *            The marker to set.
	 */
	public void setMarker(boolean marker) {
		this.marker = marker;
	}

	/**
	 * @return Returns the padding.
	 */
	public boolean isPadding() {
		return padding;
	}

	/**
	 * @param padding
	 *            The padding to set.
	 */
	public void setPadding(boolean padding) {
		this.padding = padding;
	}

	/**
	 * @return Returns the payload.
	 */
	public byte[] getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            The payload to set.
	 */
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	/**
	 * @return Returns the payloadType.
	 */
	public UnsignedByte getPayloadType() {
		return payloadType;
	}

	/**
	 * @param payloadType
	 *            The payloadType to set.
	 */
	public void setPayloadType(UnsignedByte payloadType) {
		this.payloadType = payloadType;
	}

	/**
	 * @return Returns the sequence.
	 */
	public UnsignedShort getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            The sequence to set.
	 */
	public void setSequence(UnsignedShort sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return Returns the ssrc.
	 */
	public UnsignedInt getSsrc() {
		return ssrc;
	}

	/**
	 * @param ssrc
	 *            The ssrc to set.
	 */
	public void setSsrc(UnsignedInt ssrc) {
		this.ssrc = ssrc;
	}

	/**
	 * @return Returns the timestamp.
	 */
	public UnsignedInt getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            The timestamp to set.
	 */
	public void setTimestamp(UnsignedInt timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return Returns the version.
	 */
	public byte getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(byte version) {
		this.version = version;
	}

	public PS getPs() {
		return ps;
	}

	public void setPs(PS ps) {
		this.ps = ps;
	}

}

final class UnsignedByte extends UnsignedNumber {
	static final long serialVersionUID = 1L;

	private short value;

	public UnsignedByte(byte c) {
		value = c;
	}

	public UnsignedByte(short c) {
		value = (short) (c & 0xFF);
	}

	public UnsignedByte(int c) {
		value = (short) (c & 0xFF);
	}

	public UnsignedByte(long c) {
		value = (short) (c & 0xFFL);
	}

	private UnsignedByte() {
		value = 0;
	}

	public static UnsignedByte fromBytes(byte[] c) {
		return fromBytes(c, 0);
	}

	public static UnsignedByte fromBytes(byte[] c, int idx) {
		UnsignedByte number = new UnsignedByte();
		if ((c.length - idx) < 1)
			throw new IllegalArgumentException("An UnsignedByte number is composed of 1 byte.");

		number.value = (short) (c[idx] & 0xFF);
		return number;
	}

	public static UnsignedByte fromString(String c) {
		return fromString(c, 10);
	}

	public static UnsignedByte fromString(String c, int radix) {
		UnsignedByte number = new UnsignedByte();

		short v = Short.parseShort(c, radix);
		number.value = (short) (v & 0x0F);
		return number;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public short shortValue() {
		return (short) (value & 0xFF);
	}

	@Override
	public int intValue() {
		return value & 0xFF;
	}

	@Override
	public long longValue() {
		return value & 0xFFL;
	}

	@Override
	public byte[] getBytes() {
		byte[] c = { (byte) (value & 0xFF) };
		return c;
	}

	@Override
	public int compareTo(UnsignedNumber other) {
		short otherValue = other.shortValue();
		if (value > otherValue) {
			return +1;
		} else if (value < otherValue) {
			return -1;
		}
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof Number) {
			return value == ((Number) other).shortValue();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return Short.toString(value);
	}

	@Override
	public void shiftRight(int nBits) {
		if (Math.abs(nBits) > 8) {
			throw new IllegalArgumentException("Cannot right shift " + nBits + " an UnsignedByte.");
		}
		value >>>= nBits;
	}

	@Override
	public void shiftLeft(int nBits) {
		if (Math.abs(nBits) > 8) {
			throw new IllegalArgumentException("Cannot left shift " + nBits + " an UnsignedByte.");
		}
		value <<= nBits;
	}
}

final class UnsignedInt extends UnsignedNumber {
	static final long serialVersionUID = 1L;

	private long value;

	public UnsignedInt(byte c) {
		value = c;
	}

	public UnsignedInt(short c) {
		value = c;
	}

	public UnsignedInt(int c) {
		value = c;
	}

	public UnsignedInt(long c) {
		value = c & 0xFFFFFFFFL;
	}

	private UnsignedInt() {
		value = 0;
	}

	public static UnsignedInt fromBytes(byte[] c) {
		return fromBytes(c, 0);
	}

	public static UnsignedInt fromBytes(byte[] c, int idx) {
		UnsignedInt number = new UnsignedInt();
		if ((c.length - idx) < 4) {
			throw new IllegalArgumentException("An UnsignedInt number is composed of 4 bytes.");
		}
		number.value = (((c[idx] << 24) & 0xFF000000L) | ((c[idx + 1] << 16) & 0xFF0000L) | (c[idx + 2] << 8 & 0xFF00L)
				| (c[idx + 3] & 0xFFL));
		return number;
	}

	public static UnsignedInt fromString(String c) {
		return fromString(c, 10);
	}

	public static UnsignedInt fromString(String c, int radix) {
		UnsignedInt number = new UnsignedInt();
		long v = Long.parseLong(c, radix);
		number.value = v & 0xFFFFFFFFL;
		return number;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public int intValue() {
		return (int) (value & 0xFFFFFFFFL);
	}

	@Override
	public long longValue() {
		return value & 0xFFFFFFFFL;
	}

	@Override
	public byte[] getBytes() {
		byte[] c = new byte[4];
		c[0] = (byte) ((value >> 24) & 0xFF);
		c[1] = (byte) ((value >> 16) & 0xFF);
		c[2] = (byte) ((value >> 8) & 0xFF);
		c[3] = (byte) ((value >> 0) & 0xFF);
		return c;
	}

	@Override
	public int compareTo(UnsignedNumber other) {
		long otherValue = other.longValue();
		if (value > otherValue)
			return +1;
		else if (value < otherValue)
			return -1;
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Number))
			return false;
		return value == ((Number) other).longValue();
	}

	@Override
	public String toString() {
		return Long.toString(value & 0xFFFFFFFFL);
	}

	@Override
	public int hashCode() {
		return (int) (value ^ (value >>> 32));
	}

	@Override
	public void shiftRight(int nBits) {
		if (Math.abs(nBits) > 32)
			throw new IllegalArgumentException("Cannot right shift " + nBits + " an UnsignedInt.");

		value >>>= nBits;
	}

	@Override
	public void shiftLeft(int nBits) {
		if (Math.abs(nBits) > 32)
			throw new IllegalArgumentException("Cannot left shift " + nBits + " an UnsignedInt.");

		value <<= nBits;
	}
}

abstract class UnsignedNumber extends Number {

	private static final long serialVersionUID = -6404256963187584919L;

	/**
	 * Get a byte array representation of the number. The order will be MSB first (Big Endian).
	 * 
	 * @return the serialized number
	 */
	public abstract byte[] getBytes();

	/**
	 * Perform a bit right shift of the value.
	 * 
	 * @param nBits
	 *            the number of positions to shift
	 */
	public abstract void shiftRight(int nBits);

	/**
	 * Perform a bit left shift of the value.
	 * 
	 * @param nBits
	 *            the number of positions to shift
	 */
	public abstract void shiftLeft(int nBits);

	public abstract String toString();

	public abstract int compareTo(UnsignedNumber other);

	public abstract boolean equals(Object other);

	public abstract int hashCode();

	public String toHexString() {
		return toHexString(false);
	}

	public String toHexString(boolean pad) {
		StringBuilder sb = new StringBuilder();
		boolean started = false;
		for (byte b : getBytes()) {
			if (!started && b == 0) {
				if (pad) {
					sb.append("00");
				}
			} else {
				sb.append(hexLetters[(byte) ((b >> 4) & 0x0F)]).append(hexLetters[b & 0x0F]);
				started = true;
			}
		}
		if (sb.length() == 0) {
			return "0";
		}
		return sb.toString();
	}

	protected static final char[] hexLetters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };
}

final class UnsignedShort extends UnsignedNumber {
	static final long serialVersionUID = 1L;

	private int value;

	public UnsignedShort(byte c) {
		value = c;
	}

	public UnsignedShort(short c) {
		value = c;
	}

	public UnsignedShort(int c) {
		value = c & 0xFFFF;
	}

	public UnsignedShort(long c) {
		value = (int) (c & 0xFFFFL);
	}

	private UnsignedShort() {
		value = 0;
	}

	public static UnsignedShort fromBytes(byte[] c) {
		return fromBytes(c, 0);
	}

	public static UnsignedShort fromBytes(byte[] c, int idx) {
		UnsignedShort number = new UnsignedShort();
		if ((c.length - idx) < 2) {
			throw new IllegalArgumentException("An UnsignedShort number is composed of 2 bytes.");
		}
		number.value = (((c[idx] << 8) & 0xFF00) | (c[idx + 1] & 0xFF));
		return number;
	}

	public static UnsignedShort fromString(String c) {
		return fromString(c, 10);
	}

	public static UnsignedShort fromString(String c, int radix) {
		UnsignedShort number = new UnsignedShort();
		long v = Integer.parseInt(c, radix);
		number.value = (int) (v & 0xFFFF);
		return number;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public short shortValue() {
		return (short) (value & 0xFFFF);
	}

	@Override
	public int intValue() {
		return value & 0xFFFF;
	}

	@Override
	public long longValue() {
		return value & 0xFFFFL;
	}

	@Override
	public byte[] getBytes() {
		return new byte[] { (byte) ((value >> 8) & 0xFF), (byte) (value & 0xFF) };
	}

	@Override
	public int compareTo(UnsignedNumber other) {
		int otherValue = other.intValue();
		if (value > otherValue) {
			return 1;
		} else if (value < otherValue) {
			return -1;
		}
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Number) {
			return Arrays.equals(getBytes(), ((UnsignedNumber) other).getBytes());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public void shiftRight(int nBits) {
		if (Math.abs(nBits) > 16) {
			throw new IllegalArgumentException("Cannot right shift " + nBits + " an UnsignedShort.");
		}
		value >>>= nBits;
	}

	@Override
	public void shiftLeft(int nBits) {
		if (Math.abs(nBits) > 16) {
			throw new IllegalArgumentException("Cannot left shift " + nBits + " an UnsignedShort.");
		}
		value <<= nBits;
	}

}
