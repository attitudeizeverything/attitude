package com.websystique.springmvc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.websystique.springmvc.model.AppResponse;
import com.websystique.springmvc.model.CampaignContents;
import com.websystique.springmvc.model.CampaignResponse;
import com.websystique.springmvc.model.ContentPlayingNow;
import com.websystique.springmvc.model.ContentRequest;
import com.websystique.springmvc.model.Device;
import com.websystique.springmvc.model.DeviceLocation;
import com.websystique.springmvc.model.UserDocument;
import com.websystique.springmvc.service.ContentPlayingNowService;
import com.websystique.springmvc.service.DeviceLocationService;
import com.websystique.springmvc.service.DeviceService;
import com.websystique.springmvc.service.UserDocumentService;

@RestController
public class ClientController {

	@Autowired
	ContentPlayingNowService contentPlayingNowService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	DeviceLocationService deviceLocationService;
	
	@Autowired
	UserDocumentService userDocumentService;
	
	@RequestMapping(value = "/ads", method = RequestMethod.POST,headers="Accept=application/json")
	public AppResponse getFile(@RequestParam (name="id") String id){
		List<ContentPlayingNow> cont = contentPlayingNowService.findByDeviceId(Integer.parseInt(id));
		for (ContentPlayingNow contentPlayingNow : cont) {
		System.out.println(contentPlayingNow.getUserDocument().getPlayGroup());
		}
		
		AppResponse response = new AppResponse();
		
		ArrayList<CampaignResponse> campresponse = new ArrayList<CampaignResponse>(); 
				
		for(int i=0;i<cont.size();i++){
			
			CampaignResponse res= new CampaignResponse();
			res.setDelay(cont.get(i).getDelay());
			res.setStartDateTime(cont.get(i).getStartTime());
			res.setEndDateTime(cont.get(i).getEndTime());
			res.setUnit(cont.get(i).getDelayUnit());
			CampaignContents campaignContents = new CampaignContents();
			List<CampaignContents> campaignContents2 = new ArrayList<CampaignContents>();
			campaignContents.setImageDetails(cont.get(i).getUserDocument().getFileLocation());
			campaignContents.setUnqiqueIdentifier(cont.get(i).getUserDocument().getUniqueIdentifier());
			campaignContents2.add(campaignContents);
			res.setCampaignContents(campaignContents2);
			//if(cont.get(i).getUserDocument().getPlayGroup())
			campresponse.add(res);
		}
		
		 response.setResponse(campresponse);
		 return response;
	}
	
	@RequestMapping(value = "/devices", method = RequestMethod.POST,headers="Accept=application/json")
	public List<Device> getAllDevices(@RequestParam (name="deviceLocationId") String deviceLocationId){
		return deviceService.getDevicesByLocation(Integer.parseInt(deviceLocationId));
	}
	
	@RequestMapping(value = "/deviceLocation", method = RequestMethod.POST,headers="Accept=application/json")
	public List<DeviceLocation> getAllDevicesInCity(@RequestParam (name="cityName") String cityName){
		return deviceLocationService.getLocationByCity(cityName);
	}
	
	@RequestMapping(value = "/saveContents", method = RequestMethod.POST,headers="Accept=application/json")
	public String saveContent(@RequestBody ContentRequest contentPlayingNow){
		 saveContents(contentPlayingNow);
		 return "managedocuments";
	}
	
	private void saveContents(ContentRequest contentPlayingNow){
		ContentPlayingNow now = new ContentPlayingNow();
		Device device = deviceService.findDeviceById(contentPlayingNow.getDeviceId());
		System.out.println(device.getId());
		System.out.println(device.getDeviceName());
		UserDocument document = userDocumentService.findById(contentPlayingNow.getContnetId());
		now.setDelay(4);
		now.setDelayUnit("MIN");
		now.setDevice(device);
		now.setEndTime(new Date().toString());
		now.setStartTime(new Date().toString());
		now.setUserDocument(document);
		contentPlayingNowService.save(now);
	}
}