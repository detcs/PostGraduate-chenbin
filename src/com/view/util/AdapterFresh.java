package com.view.util;

//让adapter可以主动刷新:如下拉刷新等。
public interface AdapterFresh {
	public void destroy();

	public void fresh();
}
