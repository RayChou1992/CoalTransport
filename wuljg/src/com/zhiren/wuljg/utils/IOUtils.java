package com.zhiren.wuljg.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

	public static void close(Closeable out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
