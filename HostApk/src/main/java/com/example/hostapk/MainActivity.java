package com.example.hostapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hostapk.utils.Utils;
import com.example.mylibrary.IBean;
import com.example.mylibrary.ICallback;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
	// apk名称
	private String mApkName = "app-debug.apk";

	// apk文件地址
	private String mDexPath = null;

	// 释放目录
	private File mFileRelease = null;

	// 每个应用都有classLoader，用于帮助添加外部dex内容，这里的外部dex指插件apk的dex
	private DexClassLoader mClassLoader = null;

	/**
	 * application加载完成之前调用
	 *
	 * @param newBase 基础上下文
	 */
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(newBase);
		// 在程序加载完成以前，就复制插件到内存路径（模拟网络请求提前加载的操作）
		try {
			Utils.extractAssets(newBase, mApkName);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// apk复制到内存路径后，开始加载插件apk的内容
		File extractedFile = this.getFileStreamPath(mApkName);
		mDexPath = extractedFile.getPath();

		mFileRelease = getDir("dex", 0);

		Log.d("DEMO", "dexpath:" + mDexPath);
		Log.d("DEMO", "fileRelease.getAbsolutePath():" +
				mFileRelease.getAbsolutePath());

		mClassLoader = new DexClassLoader(mDexPath, mFileRelease.getAbsolutePath(), null,
				getClassLoader());

		final TextView textView = findViewById(R.id.text1);
		Button loadPlugin = findViewById(R.id.loadPlugin);

		loadPlugin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Class loadClassBean;
				try {
					loadClassBean = mClassLoader.loadClass("com.example.jd.Bean");
					// 拿到实例
					Object beanObject = loadClassBean.newInstance();
					// 拿到方法
//					Method getNameMethod = loadClassBean.getMethod("getName");
//					getNameMethod.setAccessible(true);
//					// 通过invoke来调用实例的方法
//					String name = (String) getNameMethod.invoke(beanObject);
//					textView.setText(name);

					// 方法二：通过面向接口编程,Host主动设置数据(两个进程具备同样接口，通过接口调用实现跨进程通讯（类似AIDL），即父类饮用指向子类实现)
//					IBean bean = (IBean) beanObject;
//					bean.setName("jd");
//					textView.setText(bean.getName());

					// 方法二：通过面向接口编程，Plugin主动推动数据给Host
					IBean bean = (IBean) beanObject;
					// new接口，就是接收回调
					ICallback callback = new ICallback() {
						@Override
						public void sensResult(String result) {
							textView.setText(result);
						}
					};
					bean.register(callback);
				}
				catch (Exception e) {

				}
			}
		});
//
//		try {

//		}
//		catch (Exception e) {
//		}
	}
}