package com.github.kubesys.vsphere.create;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kubesys.vsphere.VsphereClient;
import com.github.kubesys.vsphere.VsphereClientTest;

public class CreateVMFromTemplateTest extends VsphereClientTest {
	
	public static void main(String[] args) throws Exception {
		String cookie = "CastleSessionvsphere.test=_934e58fa1a301c3707001f87aad3a050; VSPHERE-UI-JSESSIONID=969048449DF195774AC04B173FBB9409; VSPHERE-USERNAME=Administrator%40VSPHERE.TEST; VSPHERE-CLIENT-SESSION-INDEX=_a8c5ec1252c4957bdbf3a6dbc1c493b8";
		String token = "f8793506-c5e3-4fd2-89cf-84579edff3a9";
		VsphereClient vClient = getClient();
		JsonNode createFromTemplate = vClient.virtualMachines().createFromTemplate("henry20", "vm-70", "9e4a98b3-189a-475b-b093-f5cda70cd2a5", "datastore-10", "group-v3", 
				"resgroup-17", "host-9", cookie, token);
		System.out.println(createFromTemplate.toPrettyString());
		String taskId = createFromTemplate.get("task").get("value").asText();

		check(vClient, taskId, cookie);
		
	}

	private static void check(VsphereClient vClient, String taskId, String cookie) throws Exception {
		JsonNode taskInfo = vClient.virtualMachines().getTask(taskId, cookie);
		String state = taskInfo.get("state").asText();
		
		if (state.equals("SUCESS")) {
			// resources
			JsonNode json = client.virtualMachines().search("henry20", "Virtual Machine", cookie);
			String asText = json.get("id").asText();
			int eid = asText.lastIndexOf(":");
			String vmid = asText.substring("urn:vmomi:VirtualMachine:".length(), eid);
			vClient.virtualMachines().updateCPU(vmid, 4);
			vClient.virtualMachines().updateRAM(vmid, 100234);
			
			// network
			
			vClient.virtualMachines().unplugVMNIC(vmid, "2000");
			// STD是标准网络，DVS是分布式网络，根据需要挂载
//			vClient.virtualMachines().plugVM_STD_NIC("vm-43", "network-11");
			vClient.virtualMachines().plugVM_DVS_NIC(vmid, "network-11");
		} else if (state.equals("ERROR")) {
			JsonNode error = taskInfo.get("error");
			JsonNode mesg = error.get("localizedMessage");
			System.out.println(mesg.asText());
		} else {
			// wait and retry
			Thread.sleep(2000);
			check(vClient, taskId, cookie);
		}
	}
}
