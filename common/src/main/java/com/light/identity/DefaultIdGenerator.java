package com.light.identity;

/**
 * 缺省流水号ID生成器。 <br>
 * long类型数值的范围： 999+233720+3685+4774807 (64bit) <br>
 */
public class DefaultIdGenerator extends BaseIdGenerator {

	/**
	 * twitter的snowflake算法
	 */
	private SnowFlake snowFlake;
	
	public DefaultIdGenerator(long siteId, String prefix) {
		super(siteId, prefix);
		snowFlake = new SnowFlake(this.datacenterId, this.siteId);
	}
	
	public DefaultIdGenerator(long datacenterId, long siteId, String prefix) {
		super(datacenterId, siteId, prefix);
		snowFlake = new SnowFlake(this.datacenterId, this.siteId);
	}

	@Override
	public void reset() {
		snowFlake = new SnowFlake(this.datacenterId, this.siteId);
	}

	@Override
	public long generate() {
		return this.snowFlake.nextId();
	}
}
