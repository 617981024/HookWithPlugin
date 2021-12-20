package com.example.hostapk.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.hostapk.GetApplicationContext;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 将apk复制到内部存储中
 */
public class Utils {
	private static File mBaseDir;

	/**
	 * 复制文件到
	 *
	 * @param context
	 * @param sourceName
	 */
	public static void extractAssets(Context context, String sourceName) {
		AssetManager am = context.getAssets();
		// 读取流
		InputStream is = null;
		// 写入流
		FileOutputStream fos = null;
		try {
			is = am.open(sourceName);
			// 拿到assets对应的文件
			File extractFile = context.getFileStreamPath(sourceName);
			fos = new FileOutputStream(extractFile);
			byte[] buffer = new byte[1024];
			int count = 0;
			// 如果读取流一直有数据在读,就把流数据放在buffer
			while ((count = is.read(buffer)) > 0) {
				// 读取流的数据放到写入流中
				fos.write(buffer, 0, count);
			}
			fos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			// 关闭数据流
			closeSilently(is);
			closeSilently(fos);
		}
	}

	/**
	 * 获取opt优化后的路径
	 *
	 * @param packageName 包名
	 * @return 路径
	 */
	public static File getPluginOptDexDir(String packageName) {
		return enforceDirExists(new File(getPluginBaseDir(packageName), "odex"));
	}

	/**
	 * 插件得lib库路径, 这个demo里面没有用
	 */
	public static File getPluginLibDir(String packageName) {
		return enforceDirExists(new File(getPluginBaseDir(packageName), "lib"));
	}

	/**
	 * 获取被加载的插件的基础路径
	 *
	 * @param packageName 插件包名
	 * @return 基础路径
	 */
	private static File getPluginBaseDir(String packageName) {
		if (mBaseDir == null) {
			mBaseDir = GetApplicationContext.getContext().getFileStreamPath("plugin");
			enforceDirExists(mBaseDir);
		}
		return enforceDirExists(new File(mBaseDir, packageName));
	}

	private static synchronized File enforceDirExists(File sBaseDir) {
		if (!sBaseDir.exists()) {
			boolean ret = sBaseDir.mkdir();
			if (!ret) {
				throw new RuntimeException("create dir " + sBaseDir + "failed");
			}
		}
		return sBaseDir;
	}

	private static void closeSilently(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		}
		catch (IOException e) {
		}
	}
}
