package upgrade.util;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class ByteTool {
	/**
	 * 字节 转十六进制字符串
	 * @param buf
	 * @return
	 */
	public static String bytesToHexString(byte[] buf) {
		return bytesToHexString(buf, 0, buf.length);
	}

	/**
	 * 字节 转十六进制字符串
	 * @param buf
	 * @param start
	 * @param length
	 * @return
	 */
	public static String bytesToHexString(byte[] buf, int start, int length) {
		StringBuilder out = new StringBuilder();
		for (int i = start; i < start + length; i++) {
			out.append(String.format("%02X",new Object[] { Byte.valueOf(buf[i]) }));
		}
		return out.toString();
	}
	
	/**
	 * 字节转整�?
	 * @param buf
	 * @param start
	 * @param length
	 * @return
	 */
	public static int bytesToInt(byte[] buf, int start, int length) {
		int ret = 0;
		for (int i = start; i < start + length; i++) {
			ret <<= 8;
			ret |= (0xFF & buf[i]);
		}
		return ret;
	}

	/**
	 * 字节转长整型
	 * @param buf
	 * @param start
	 * @param length
	 * @return
	 */
	public static long bytesToLong(byte[] buf, int start, int length) {
		long ret = 0L;
		for (int i = start; i < start + length; i++) {
			ret <<= 8;
			ret |= (0xFF & buf[i]);
		}
		return ret;
	}
	
	/**
	 * 字节转十六进制字符串
	 * @param buf
	 * @param start
	 * @param len
	 * @return
	 */
	public static String toHexString(byte[] buf, int start, int len) {
		StringBuilder buffer = new StringBuilder();
		char[] ch = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		for (int i = start; (i < start + len) && (start + len <= buf.length); i++) {
			int b = buf[i];
			int high = b >>> 4 & 0xF;
			int low = b & 0xF;
			buffer.append(ch[high]);
			buffer.append(ch[low]);
		}
		return buffer.toString();
	}

	/**
	 * byte[] �? hex
	 * @param buf
	 * @return
	 */
	public static String toHexString(byte[] buf) {
		return toHexString(buf, 0, buf.length);
	}
	
	/**
	 * int �? byte[]
	 * @param num
	 * @param length
	 * @return
	 */
	public static byte[] intToBytes(int num, int length) {
		byte[] buf = new byte[length];
		return intToBytes(num, buf, 0, length);
	}

	/**
	 * int �? byte[]
	 * @param num
	 * @param buf
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] intToBytes(int num, byte[] buf, int start, int length) {
		return longToBytes(num, buf, start, length);
	}

	/**
	 * 长整型转字节
	 * @param num
	 * @param length
	 * @return
	 */
	public static byte[] longToBytes(long num, int length) {
		byte[] buf = new byte[length];
		return longToBytes(num, buf, 0, length);
	}

	/**
	 * long �? byte[]
	 * @param num
	 * @param buf
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] longToBytes(long num, byte[] buf, int start, int length) {
		for (int i = start + length - 1; i >= start; i--) {
			buf[i] = (byte) (int) (num & 0xFF);
			num >>>= 8;
		}
		return buf;
	}
	
	/**
	 * short转byte[]
	 * @param v
	 * @return
	 */
	public static byte[] shortToByte(short v) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		byte[] ret = new byte[2];
		ShortBuffer sb = bb.asShortBuffer();
		sb.put(v);
		bb.get(ret);
		return ret;
	}

	/**
	 * 将byte转成String
	 * 
	 * @param bytes
	 * 
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
		if (bytes == null) {
			return "null";
		}
		int iMax = bytes.length - 1;
		if (iMax == -1) {
			return "[]";
		}

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (byte bt : bytes) {
			b.append(String.format("%02X ", (((int) bt) & 0xFF)));
		}
		b.append(']');
		return b.toString();
	}

	/**
	 * 截取数组
	 * @param src
	 * @param start
	 * @param len
	 * @return
	 */
	public static byte[] split(byte[] src, int start, int len) {
		byte[] dest = new byte[len];
		for (int i = 0; i < len; i++) {
			dest[i] = src[i + start];
		}
		return dest;
	}

	/**
	 * int转byte[]
	 * @param v
	 * @return
	 */
	public static byte[] intToByte(int v) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		byte[] ret = new byte[4];
		IntBuffer ib = bb.asIntBuffer();
		ib.put(v);
		bb.get(ret);
		return ret;
	}

	/**
	 * float转byte[]
	 * @param v
	 * @return
	 */
	public static byte[] floatToByte(float v) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		byte[] ret = new byte[4];
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(v);
		bb.get(ret);
		return ret;
	}

	/**
	 * long转byte[]
	 * @param v
	 * @return
	 */
	public static byte[] longToByte(long v) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		byte[] ret = new byte[8];
		LongBuffer lb = bb.asLongBuffer();
		lb.put(v);
		bb.get(ret);
		return ret;
	}

	/**
	 * byte[]转short
	 * @param v
	 * @return
	 */
	public static short byteToShort(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		ShortBuffer sb = bb.asShortBuffer();
		return sb.get();
	}

	/**
	 * byte[]转int
	 * @param v
	 * @return
	 */
	public static int byteToInt(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		IntBuffer ib = bb.asIntBuffer();
		return ib.get();
	}

	/**
	 * byte[]转float
	 * @param v
	 * @return
	 */
	public static float byteToFloat(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		FloatBuffer fb = bb.asFloatBuffer();
		return fb.get();
	}

	/**
	 * byte[]转long
	 * @param v
	 * @return
	 */
	public static long byteToLong(byte[] v) {
		ByteBuffer bb = ByteBuffer.wrap(v);
		LongBuffer lb = bb.asLongBuffer();
		return lb.get();
	}
	
    public static byte[] hexStringToBytes(String hexStr) {
        int bytelen = (hexStr.length() + 1) / 2;
        byte[] buf = new byte[bytelen];
        for (int i = 0; i < bytelen; i++) {
            int high = hexCharToVal(hexStr.charAt(i * 2));
            int low = (i == bytelen - 1 && hexStr.length() + 1 == bytelen * 2) ? 0 : hexCharToVal(hexStr.charAt(i * 2 + 1));
            buf[i] = (byte) ((high << 4) | (low & 0x0f));
        }
        return buf;//new HexBytes(hexString).getBytes();
    }
    
    private static int hexCharToVal(char ch) {
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        } else if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        } else if (ch >= '0' && ch <= '9') {
            return ch - '0';
        } else {
            return 0;
        }
    }
    
    public static byte[] strToBCD(String asc) {    
 	   int len = asc.length();    
 	   int mod = len % 2;   
 	  
 	    if (mod != 0) {    
 	     asc = "0" + asc;    
 	     len = asc.length();    
 	    }   
 	  
 	    byte abt[] = new byte[len];    
 	    if (len >= 2) {    
 	     len = len / 2;    
 	    }   
 	  
 	    byte bbt[] = new byte[len];    
 	    abt = asc.getBytes();    
 	    int j, k;   
 	  
 	    for (int p = 0; p < asc.length()/2; p++) {    
 	     if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {    
 	      j = abt[2 * p] - '0';    
 	     } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {    
 	      j = abt[2 * p] - 'a' + 0x0a;    
 	     } else {    
 	      j = abt[2 * p] - 'A' + 0x0a;    
 	     }   
 	  
 	     if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {    
 	      k = abt[2 * p + 1] - '0';    
 	     } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {    
 	      k = abt[2 * p + 1] - 'a' + 0x0a;    
 	     }else {    
 	      k = abt[2 * p + 1] - 'A' + 0x0a;    
 	     }   
 	  
 	     int a = (j << 4) + k;    
 	     byte b = (byte) a;    
 	     bbt[p] = b;    
 	    }    
 	    return bbt;    
 	}

	public static long bytesToLong(byte[] len) {
		// TODO Auto-generated method stub
		return bytesToLong(len,0,len.length);
	}

	public static int bytesToInt(byte[] len) {
		// TODO Auto-generated method stub
		return bytesToInt(len, 0, len.length);
	}    
 
}
