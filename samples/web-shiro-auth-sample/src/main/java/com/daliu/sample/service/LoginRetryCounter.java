package com.daliu.sample.service;

import lombok.Getter;

/**
 * 登录重试计数器
 * @author daliu
 *
 */
public abstract class LoginRetryCounter {

	/**
	 * 最大重试次数
	 */
	@Getter
	protected int maxRetryTimes;
	
	/**
	 * 登录锁定时间（分钟）
	 */
	@Getter
	protected int lockMinutes;
	
	public LoginRetryCounter(int maxRetryTimes, int lockMinutes) {
		this.maxRetryTimes = maxRetryTimes;
		this.lockMinutes = lockMinutes;
	}
	
	/**
	 * 获取当前登录失败次数
	 * @param username
	 * @return 当前失败次数
	 */
	protected abstract int getFailureTimes(String username);
	/**
	 * 重置登录失败次数
	 * @param username
	 */
	protected abstract void resetFailureTimes(String username);
	/**
	 * 登录失败计数加1
	 * @param username
	 */
	protected abstract void incrementFailureTimes(String username);
	
	/**
	 * 剩余重试次数
	 * @param username
	 * @return
	 */
	public int leftRetryTimes(String username) {
		int leftTimes = maxRetryTimes - getFailureTimes(username);
		return leftTimes;
	}
}
