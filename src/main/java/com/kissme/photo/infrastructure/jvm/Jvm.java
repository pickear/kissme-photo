package com.kissme.photo.infrastructure.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * 
 * @author loudyn
 * 
 */
public final class Jvm {
	private final MemoryMXBean memoryMXBean;
	private final RuntimeMXBean runtimeMXBean;

	/**
	 * 
	 * @param memoryMXBean
	 * @param runtimeMXBean
	 */
	private Jvm(MemoryMXBean memoryMXBean, RuntimeMXBean runtimeMXBean) {
		this.memoryMXBean = memoryMXBean;
		this.runtimeMXBean = runtimeMXBean;
	}

	private static Jvm me;
	static {
		Jvm jvm = new Jvm(ManagementFactory.getMemoryMXBean(), ManagementFactory.getRuntimeMXBean());
		me = jvm;
	}

	/**
	 * 
	 * @return
	 */
	public static Jvm me() {
		return me;
	}

	/**
	 * 
	 * @return
	 */
	public long getPid() {

		long pid = -1;
		String xpid = runtimeMXBean.getName();
		try {

			xpid = xpid.split("@")[0];
			return Long.parseLong(xpid);
		} catch (Exception e) {}

		return pid;
	}

	/**
	 * 
	 * @return
	 */
	public String getVmVersion() {
		return runtimeMXBean.getVmVersion();
	}

	public String getVmName() {
		return runtimeMXBean.getVmName();
	}

	public long getVmStartTime() {
		return runtimeMXBean.getStartTime();
	}

	public long getVmUptime() {
		return runtimeMXBean.getUptime();
	}

	public List<String> getVmInputArgs() {
		return runtimeMXBean.getInputArguments();
	}

	public String getJavaVersion() {
		return runtimeMXBean.getSystemProperties().get("java.version");
	}

	public long getHeapInit() {
		return memoryMXBean.getHeapMemoryUsage().getInit();
	}

	public long getHeapMax() {
		return memoryMXBean.getHeapMemoryUsage().getMax();
	}

	public long getHeapUsed() {
		return memoryMXBean.getHeapMemoryUsage().getUsed();
	}

	public long getNonHeapInit() {
		return memoryMXBean.getNonHeapMemoryUsage().getInit();
	}

	public long getNonHeapMax() {
		return memoryMXBean.getNonHeapMemoryUsage().getMax();
	}

	public long getNonHeapUsed() {
		return memoryMXBean.getNonHeapMemoryUsage().getUsed();
	}
}
