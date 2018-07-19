package com.jtd.engine.utils;

import org.apache.mina.proxy.utils.ByteUtilities;

/**
 * @作者 Amos Xu
 * @版本 V1.0
 * @配置 
 * @创建日期 2016年9月8日
 * @项目名称 dsp-engine
 * @描述 <p></p>
 */
public class ByteUtil extends ByteUtilities{

    /**
     * 从4字节读出Long型值
     * @param b
     * @return
     */
    public static final long makeLongFromBigEndianByte4(byte[] b) {
        return makeLongFromBigEndianByte4(b, 0);
    }

    /**
     * 从4字节读出Long型值
     * @param b
     * @param offset
     * @return
     */
	public static final long makeLongFromBigEndianByte4(byte[] b, int offset) {

		return (long) (b[offset] & 255) << 24 
				| (b[offset + 1] & 255) << 16
				| (b[offset + 2] & 255) << 8 
				| (b[offset + 3] & 255);
	}

    /**
     * 从8字节读出Long型值
     * @param b
     * @return
     */
    public static final long makeLongFromBigEndianByte8(byte[] b) {
        return makeLongFromBigEndianByte8(b, 0);
    }

    /**
     * 从8字节读出Long型值
     * @param b
     * @param offset
     * @return
     */
	public static final long makeLongFromBigEndianByte8(byte[] b, int offset) {

		return (long) (b[offset] & 255) << 56
				| (long) (b[offset + 1] & 255) << 48
				| (long) (b[offset + 2] & 255) << 40
				| (long) (b[offset + 3] & 255) << 32
				| (long) (b[offset + 4] & 255) << 24
				| (b[offset + 5] & 255) << 16 
				| (b[offset + 6] & 255) << 8
				| (b[offset + 7] & 255);
	}

	/**
	 * 用大端在前的字节序写short
	 * @param v
	 * @param b
	 * @param offset
	 * @return
	 */
	public static final byte[] writeShortBigEndian(short v, byte[] b, int offset) {
		b[offset] = (byte) (v >> 8);
		b[offset + 1] = (byte) v;
		return b;
	}

	/**
	 * 用大端在前的字节序写int
	 * @param v
	 * @param b
	 * @param offset
	 * @return
	 */
	public static final byte[] writeIntBigEndian(int v, byte[] b, int offset) {
		b[offset] = (byte) (v >> 24);
		b[offset + 1] = (byte) (v >> 16);
		b[offset + 2] = (byte) (v >> 8);
		b[offset + 3] = (byte) v;
		return b;
	}

	/**
	 * 用大端在前的字节序写long
	 * @param v
	 * @param b
	 * @param offset
	 * @return
	 */
	public static final byte[] writeLongBigEndian8(long v, byte[] b, int offset) {
		b[offset] = (byte) ((v >>> 56) & 255);
		b[offset + 1] = (byte) ((v >>> 48) & 255);
		b[offset + 2] = (byte) ((v >>> 40) & 255);
		b[offset + 3] = (byte) ((v >>> 32) & 255);
		b[offset + 4] = (byte) ((v >>> 24) & 255);
		b[offset + 5] = (byte) ((v >>> 16) & 255);
		b[offset + 6] = (byte) ((v >>> 8) & 255);
		b[offset + 7] = (byte) (v & 255);
		return b;
	}

	/**
	 * 用大端在前的字节序写long
	 * @param v
	 * @param b
	 * @param offset
	 * @return
	 */
	public static final byte[] writeLongBigEndian4(long v, byte[] b, int offset) {
		b[offset] = (byte) ((v >>> 24) & 255);
		b[offset + 1] = (byte) ((v >>> 16) & 255);
		b[offset + 2] = (byte) ((v >>> 8) & 255);
		b[offset + 3] = (byte) (v & 255);
		return b;
	}

	/**
	 * 两个字节数组是否相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static final boolean equalBytes(byte[] a, byte[] b) {
		if (a == null || b == null) return false;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
}
