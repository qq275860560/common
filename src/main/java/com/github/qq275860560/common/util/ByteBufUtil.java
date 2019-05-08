package com.github.qq275860560.common.util;

import io.netty.buffer.ByteBuf;

/**
 * @author jiangyuanlin@163.com
 */
public class ByteBufUtil {
	public static String toString(final ByteBuf byteBuf) {
		final byte[] array = toArray(byteBuf);
		return new String(array);
	}

	public static byte[] toArray(final ByteBuf byteBuf) {
		final byte[] array = new byte[byteBuf.readableBytes()];
		// byteBuf.getBytes(0, array);
		byteBuf.readBytes(array);
		return array;
	}

}
