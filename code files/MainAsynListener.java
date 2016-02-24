package com.jsonfiles;

public interface MainAsynListener<T> {

	public void onPostSuccess(T result, int flag, boolean isSucess);

	public void onPostError(int flag, int error);

}
