package com.anbang.p2p.web.controller;

import java.io.File;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.p2p.conf.FaceCongif;
import com.anbang.p2p.util.HttpUtil;
import com.anbang.p2p.web.vo.CommonVO;

@RestController
@RequestMapping("/verify")
public class UserVerifyController {

	@RequestMapping("/test")
	public CommonVO verifyTest() {
		CommonVO vo = new CommonVO();
		File file = new File("D:/test2.jpg");
		byte[] buff = HttpUtil.getBytesFromFile(file);
		String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
		HashMap<String, String> map = new HashMap<>();
		HashMap<String, byte[]> byteMap = new HashMap<>();
		map.put("api_key", FaceCongif.API_KEY);
		map.put("api_secret", FaceCongif.API_SECRET);
		map.put("return_landmark", "2");
		map.put("return_attributes",
				"gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
		byteMap.put("image_file", buff);
		try {
			byte[] bacd = HttpUtil.post(url, map, byteMap);
			String str = new String(bacd);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

}
