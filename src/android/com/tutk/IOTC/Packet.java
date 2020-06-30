/*! \file AVAPIs.h
This file describes all the APIs of the AV module in IOTC platform.
AV module is a kind of data communication modules in IOTC platform to provide
fluent streaming Audio / Video data from AV servers to AV clients in
unidirectional way.

\copyright Copyright (c) 2010 by Throughtek Co., Ltd. All Rights Reserved.

Revision Table

Version     | Name             |Date           |Description
------------|------------------|---------------|-------------------
3.1.10.5    |Terry Liu         |2018-12-17     |- Mark avServStart, avRecvFrameData as deprecated
3.1.10.6    |Ethan Tsai        |2019-02-11     |- Mark avServStart2, avServStart3 as deprecated
3.1.10.6    |Ethan Tsai        |2019-02-11     |- Mark avClientStart, avClientStart2 as deprecated
 */

// package com.kit.cordova.nativeLocation.com.tutk.IOTC;
package com.tutk.IOTC;

//import cn.com.fmsh.script.constants.ScriptToolsConst;
//import cn.com.fmsh.tsm.business.constants.Constants;

public class Packet {

	public static final short byteArrayToShort(byte[] bArr, int i, boolean z) {
		if (z) {
			return (short) ((bArr[i + 1] & 255) | ((bArr[i] & 255) << 8));
		}
		return (short) (((bArr[i + 1] & 255) << 8) | (bArr[i] & 255));
	}

	public static final int byteArrayToInt(byte[] bArr, int i, boolean z) {
		if (z) {
			return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
		}
		return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
	}

//	public static final long byteArrayToLong(byte[] bArr, int i, boolean z) {
//		if (z) {
//			return (long) ((bArr[i + 7] & 255) | ((bArr[i] & 255) << ScriptToolsConst.TagName.TagSerial) | ((bArr[i + 1] & 255) << 48) | ((bArr[i + 2] & 255) << Constants.TagName.CARD_APP_BLANCE) | ((bArr[i + 3] & 255) << 32) | ((bArr[i + 4] & 255) << 24) | ((bArr[i + 5] & 255) << 16) | ((bArr[i + 6] & 255) << 8));
//		}
//		return (long) (((bArr[i + 7] & 255) << ScriptToolsConst.TagName.TagSerial) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24) | ((bArr[i + 4] & 255) << 32) | ((bArr[i + 5] & 255) << Constants.TagName.CARD_APP_BLANCE) | ((bArr[i + 6] & 255) << 48));
//	}

	public static final byte[] shortToByteArray(short s, boolean z) {
		if (z) {
			return new byte[]{(byte) (s >>> 8), (byte) s};
		}
		return new byte[]{(byte) s, (byte) (s >>> 8)};
	}

	public static final byte[] intToByteArray(int i, boolean z) {
		if (z) {
			return new byte[]{(byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i};
		}
		return new byte[]{(byte) i, (byte) (i >>> 8), (byte) (i >>> 16), (byte) (i >>> 24)};
	}

	public static final byte[] intToByteArray_Little(int value) {
		return new byte[] { (byte) value, (byte) (value >>> 8), (byte) (value >>> 16), (byte) (value >>> 24) };
	}
}
