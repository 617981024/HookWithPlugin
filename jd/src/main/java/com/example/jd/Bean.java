package com.example.jd;

import com.example.mylibrary.IBean;
import com.example.mylibrary.ICallback;

public class Bean implements IBean {
	private ICallback callback;

	private String name = "jd";

	public String getName() {
		return name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	@Override
	public void register(ICallback iCallback) {
		this.callback = iCallback;
		clickButton();
	}

	private void clickButton() {
		callback.sensResult("Hello" + this.name);
	}
}