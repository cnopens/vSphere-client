package com.github.kubesys.vsphere.info;

import com.github.kubesys.vsphere.VsphereClient;
import com.github.kubesys.vsphere.VsphereClientTest;

/**
 * Unit test for simple App.
 */
public class GetDatastoreInHostInfoTest extends VsphereClientTest {

	public static void main(String[] args) throws Exception {
		VsphereClient vClient = getClient();
		String cookie = vClient.getCookie();
		System.out.println(vClient.virtualMachinePools().getDatastoreInHostInfo(
				"datastore1", cookie, vClient.getXSRFToken(cookie)));
	}
}
