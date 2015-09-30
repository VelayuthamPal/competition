package com.test;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.management.MBeanServerConnection;

import com.sun.management.OperatingSystemMXBean;
import com.sun.jna.platform.win32.Advapi32Util;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

public class TestUsage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int i = 0;

		/*
		 * OperatingSystemMXBean operatingSystemMXBean =
		 * ManagementFactory.getOperatingSystemMXBean(); for (Method method :
		 * operatingSystemMXBean.getClass().getDeclaredMethods()) {
		 * method.setAccessible(true); if (method.getName().startsWith("get") &&
		 * Modifier.isPublic(method.getModifiers())) { Object value; try { value
		 * = method.invoke(operatingSystemMXBean); } catch (Exception e) { value
		 * = e; } // try System.out.println(method.getName() + " = " + value); }
		 * // if } // for
		 */
		MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();

		OperatingSystemMXBean osMBean;
		try {
			osMBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
					OperatingSystemMXBean.class);
			/*
			 * long nanoBefore = System.nanoTime(); long cpuBefore =
			 * osMBean.getProcessCpuTime();
			 * 
			 * // Call an expensive task, or sleep if you are monitoring a
			 * remote process
			 * 
			 * long cpuAfter = osMBean.getProcessCpuTime(); double
			 * avgLoad=osMBean.getSystemLoadAverage(); long nanoAfter =
			 * System.nanoTime();
			 * 
			 * long percent; if (nanoAfter > nanoBefore) percent =
			 * ((cpuAfter-cpuBefore)*100L)/ (nanoAfter-nanoBefore); else percent
			 * = 0;
			 * 
			 * System.out.println("Cpu usage: "+percent+"%");
			 * 
			 * System.out.println(avgLoad);
			 */

			OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
					.getOperatingSystemMXBean();

			double freeSpace = 0;
			double totalSpace = 0;

			while (true) {

				try {
					Thread.sleep(5000);

					freeSpace = 0;
					totalSpace = 0;

					double cpuUsage = bean.getSystemCpuLoad() * 100;
					System.out.println("New Value: " + cpuUsage);

					double ramUsage = (bean.getTotalPhysicalMemorySize() - bean.getFreePhysicalMemorySize())
							/ 1000000000;
					System.out.println(ramUsage);

					double ramEff = (ramUsage / (bean.getTotalPhysicalMemorySize() / 1000000000)) * 100;
					System.out.println(ramEff);

					File[] drives = File.listRoots();
					if (drives != null && drives.length > 0) {
						for (File aDrive : drives) {
							System.out.println(aDrive);

							freeSpace += aDrive.getFreeSpace() / 1000000000;
							totalSpace += aDrive.getTotalSpace() / 1000000000;
						}
					}

					System.out.println("Free Space " + freeSpace);
					System.out.println("Total Space " + totalSpace);

					java.util.Date utilDate = new Date();

					java.sql.Date date = new java.sql.Date(utilDate.getTime());

					java.sql.Connection con;
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
					System.out.println("Connection established");
					double hdPercent = ((totalSpace - freeSpace) / totalSpace) * 100;

					System.out.println(bean.getSystemCpuLoad() * 100);
					System.out.println(ramUsage);
					System.out.println(hdPercent);

					PreparedStatement ps = con.prepareStatement("INSERT INTO coll_usage VALUES (?,?,?,?,?)");
					ps.setDouble(1, cpuUsage);

					ps.setDouble(2, (100.00-ramUsage));

					ps.setDouble(3, hdPercent);

					ps.setInt(4, 1001);

					ps.setDate(5, date);

					ps.executeUpdate();
					
					ps.close();
					
					String CPUprocessor=Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE,
							"HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\\", "ProcessorNameString");
					
					ps=con.prepareStatement("INSERT INTO user_details VALUES (?,?,?,?)");
					
					ps.setInt(1, 1001);
					
					ps.setString(2, CPUprocessor);
					
					ps.setDouble(3, bean.getTotalPhysicalMemorySize()/1000000000);
					
					ps.setDouble(4, totalSpace);
					
					ps.executeUpdate();
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
