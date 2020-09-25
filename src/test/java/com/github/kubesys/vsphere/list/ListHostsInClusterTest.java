package com.github.kubesys.vsphere.list;

import com.github.kubesys.vsphere.VsphereClientTest;

/**
 * Unit test for simple App.
 */
public class ListHostsInClusterTest extends VsphereClientTest {
	
	public static void main(String[] args) throws Exception {
		System.out.println(getClient().virtualMachinePools().listHostsInCluster("domain-c16"));
	}
}

