package com.anbang.p2p.plan.bean;

import java.util.Map;

/**
 * 身份证认证查询信息
 *
 */
public class IDCardQueryInfo {

	private String id;
	/**
	 * 用于标示本次请求的唯一的字符串
	 */
	private String request_id;
	/**
	 * 传入的业务流水号
	 */
	private String biz_no;
	/**
	 * 表示本次验证的结果状态码；开发者可结合：result_code、result_message知晓具体的结果、原因
	 */
	private String result_code;
	/**
	 * 开发者可通过此字段信息知晓具体的原因，具体见：result_code&result_code对照表
	 */
	private String result_message;
	/**
	 * 身份证人像面照片，Base64编码
	 */
	private String image_frontside;
	/**
	 * 身份证国徽面照片，Base64编码
	 */
	private String image_backside;
	/**
	 * 姓名。存在逻辑问题：姓名识别结果不合理：只有一个字/非汉字字符（允许存在“.”）（少数民族也应该是汉字，因为他们存在汉字的内容部分）
	 */
	private String name;
	/**
	 * 性别（男/女）。存在逻辑问题：性别识别结果与二代身份证号码第17位不匹配（奇数为男、偶数为女），或者男/女之外的其他值
	 */
	private String gender;
	/**
	 * 民族（汉字）。不做逻辑判断，logic字段返回“0”
	 */
	private String nationality;
	/**
	 * 出生年份。存在逻辑问题：该结果与身份证号码7～10位不一致、若识别结果大于当前年份，或者小于1800
	 */
	private String birth_year;
	/**
	 * 出生月份。存在逻辑问题：该结果与身份证号码11～12位不一致，或者结果在1～12范围之外
	 */
	private String birth_month;
	/**
	 * 出生日。存在逻辑问题：该结果与身份证号码13～14位不一致，或识别结果在1～28/30/31（不同月份对应天数）之外
	 */
	private String birth_day;
	/**
	 * 身份证号。注意：本字段返回的result里面如果某些字符被遮挡（internal：confidence过低）会返回“*”，此时quality=1（因为出星号，我们认为一定是存在遮挡等影戏那个识别的问题存在，或者十分模糊）。
	 * 存在逻辑问题：身份证号码规则不正确
	 */
	private String idcard_number;
	/**
	 * 住址。不做逻辑判断，logic字段返回“0”
	 */
	private String address;
	/**
	 * 头像（在Result中返回Base64编码的String信息）。存在逻辑问题：如果没有检测到人脸，则“logic=1”，检测到人脸返回0
	 */
	private String portrait;
	/**
	 * 签发机关。不做逻辑判断，logic字段返回0
	 */
	private String issued_by;
	/**
	 * 有效日期的起始时间，表示方法为8位数字，如：20150302。存在逻辑问题：有效期开始日期不存在、截止日期-开始日期<5年
	 */
	private String valid_date_start;
	/**
	 * 有效日期的结束时间，表示方法为8位数字或者汉字：长期。存在逻辑问题：证件有效期到期时间在当前时间之前、有效期截止日期不存在或不为“长期”、截止日期-开始日期<5年
	 */
	private String valid_date_end;
	/**
	 * 表示以上会返回识别内容的区域才会返回的对应内容，若没有识别到，则返回空或者“null”；注意：如果没有识别出身份证，即result=2001时，或者内容字段没有识别出来，该内容字段的“result”返回空。
	 */
	private String result;
	/**
	 * 表示该区域是否存在质量问题（存在影响识别的光斑、阴影、遮挡、污渍等），注意：存在质量问题如果是光斑，部分遮挡，也是可以识别出内容的，本字段对存在留存需求的场景提供参考。取［0，1］区间实数，3位有效数字，一般来说，这个质量分低于0.753，我们认为该区域存在质量问题。****注意：如果没有识别出身份证，即result=2001时，该内容字段的“quality”返回空。
	 */
	private String quality;

	/**
	 * 返回字典结构，里面会包含区域的四点坐标
	 */
	private String rect;

	/**
	 * 表示该字段是否和其他某个字段存在逻辑问题；通常如果存在逻辑问题，可能是图片内容有问题，或者识别错误，但是有逻辑层的判断发现了。
	 * 0:表示正常；1:表示存在逻辑问题；
	 */
	private String logic;

	/**
	 * 表示对身份证照片的五种分类的结果。它为一个结构体，包含五个key-value
	 * pair，每个key表示一种分类类型，每个value为此类型的概率值（取［0，1］区间实数，取3位有效数字，五个value总和为1）。这五个key是：ID_Photo：正式身份证照片、Temporary_ID_Photo：临时身份证照、Photocopy：正式身份证的复印件、Screen：手机或电脑屏幕翻拍的照片、Edited：用工具合成或者编辑过的身份证图片
	 * ID_Photo_Threshold：表示判断为正式身份证照片的阈值，通常来说，如果ID_Photo的值不低于该阈值，可以认定为真实什么证的实拍
	 */
	private String frontside_legality;

	/**
	 * 表示身份证图片的完整性 0:表示身份证部分是完整的； 1:表示身份证不完整，但是内容区域全部都在图片内； 2:表示身份证不完整，且部分内容在区域外；
	 * 注意：如果没有识别出身份证，即result=2001时，此字段返回空。
	 */
	private String frontside_completeness;

	/**
	 * 返回字典结构，里面会包含整张卡片四点坐标
	 */
	private String frontside_card_rect;

	/**
	 * 表示对身份证照片的五种分类的结果。它为一个结构体，包含五个key-value
	 * pair，每个key表示一种分类类型，每个value为此类型的概率值（取［0，1］区间实数，取3位有效数字，五个value总和为1）。这五个key是：ID_Photo：正式身份证照片、Temporary_ID_Photo：临时身份证照、Photocopy：正式身份证的复印件、Screen：手机或电脑屏幕翻拍的照片、Edited：用工具合成或者编辑过的身份证图片
	 * ID_Photo_Threshold：表示判断为正式身份证照片的阈值，通常来说，如果ID_Photo的值不低于该阈值，可以认定为真实什么证的实拍
	 */
	private String backside_legality;

	/**
	 * 表示身份证图片的完整性 0:表示身份证部分是完整的； 1:表示身份证不完整，但是内容区域全部都在图片内； 2:表示身份证不完整，且部分内容在区域外；
	 * 注意：如果没有识别出身份证，即result=2001时，此字段返回空。
	 */
	private String backside_completeness;

	/**
	 * 返回字典结构，里面会包含整张卡片四点坐标
	 */
	private String backside_card_rect;

	/**
	 * 发生错误后，会返回对应的错误码
	 */
	private String ERROR;

	public IDCardQueryInfo(Map map) {
		if (map.containsKey("request_id")) {
			this.request_id = (String) map.get("request_id");
		}
		if (map.containsKey("biz_no")) {
			this.biz_no = (String) map.get("biz_no");
		}
		if (map.containsKey("result_code")) {
			this.result_code = (String) map.get("result_code");
		}
		if (map.containsKey("result_message")) {
			this.result_message = (String) map.get("result_message");
		}
		if (map.containsKey("image_frontside")) {
			this.image_frontside = (String) map.get("image_frontside");
		}
		if (map.containsKey("image_backside")) {
			this.image_backside = (String) map.get("image_backside");
		}
		if (map.containsKey("name")) {
			this.name = (String) map.get("name");
		}
		if (map.containsKey("gender")) {
			this.gender = (String) map.get("gender");
		}
		if (map.containsKey("nationality")) {
			this.nationality = (String) map.get("nationality");
		}
		if (map.containsKey("birth_year")) {
			this.birth_year = (String) map.get("birth_year");
		}
		if (map.containsKey("birth_month")) {
			this.birth_month = (String) map.get("birth_month");
		}
		if (map.containsKey("birth_day")) {
			this.birth_day = (String) map.get("birth_day");
		}
		if (map.containsKey("idcard_number")) {
			this.idcard_number = (String) map.get("idcard_number");
		}
		if (map.containsKey("address")) {
			this.address = (String) map.get("address");
		}
		if (map.containsKey("portrait")) {
			this.portrait = (String) map.get("portrait");
		}
		if (map.containsKey("issued_by")) {
			this.issued_by = (String) map.get("issued_by");
		}
		if (map.containsKey("valid_date_start")) {
			this.valid_date_start = (String) map.get("valid_date_start");
		}
		if (map.containsKey("valid_date_end")) {
			this.valid_date_end = (String) map.get("valid_date_end");
		}
		if (map.containsKey("+result")) {
			this.result = (String) map.get("+result");
		}
		if (map.containsKey("+quality")) {
			this.quality = (String) map.get("+quality");
		}
		if (map.containsKey("+rect")) {
			this.rect = (String) map.get("+rect");
		}
		if (map.containsKey("+logic")) {
			this.logic = (String) map.get("+logic");
		}
		if (map.containsKey("frontside_legality")) {
			this.frontside_legality = (String) map.get("frontside_legality");
		}
		if (map.containsKey("frontside_completeness")) {
			this.frontside_completeness = (String) map.get("frontside_completeness");
		}
		if (map.containsKey("frontside_card_rect")) {
			this.frontside_card_rect = (String) map.get("frontside_card_rect");
		}
		if (map.containsKey("backside_legality")) {
			this.backside_legality = (String) map.get("backside_legality");
		}
		if (map.containsKey("backside_completeness")) {
			this.backside_completeness = (String) map.get("backside_completeness");
		}
		if (map.containsKey("backside_card_rect")) {
			this.backside_card_rect = (String) map.get("backside_card_rect");
		}
		if (map.containsKey("ERROR")) {
			this.ERROR = (String) map.get("ERROR");
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getBiz_no() {
		return biz_no;
	}

	public void setBiz_no(String biz_no) {
		this.biz_no = biz_no;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_message() {
		return result_message;
	}

	public void setResult_message(String result_message) {
		this.result_message = result_message;
	}

	public String getImage_frontside() {
		return image_frontside;
	}

	public void setImage_frontside(String image_frontside) {
		this.image_frontside = image_frontside;
	}

	public String getImage_backside() {
		return image_backside;
	}

	public void setImage_backside(String image_backside) {
		this.image_backside = image_backside;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBirth_year() {
		return birth_year;
	}

	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}

	public String getBirth_month() {
		return birth_month;
	}

	public void setBirth_month(String birth_month) {
		this.birth_month = birth_month;
	}

	public String getBirth_day() {
		return birth_day;
	}

	public void setBirth_day(String birth_day) {
		this.birth_day = birth_day;
	}

	public String getIdcard_number() {
		return idcard_number;
	}

	public void setIdcard_number(String idcard_number) {
		this.idcard_number = idcard_number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getIssued_by() {
		return issued_by;
	}

	public void setIssued_by(String issued_by) {
		this.issued_by = issued_by;
	}

	public String getValid_date_start() {
		return valid_date_start;
	}

	public void setValid_date_start(String valid_date_start) {
		this.valid_date_start = valid_date_start;
	}

	public String getValid_date_end() {
		return valid_date_end;
	}

	public void setValid_date_end(String valid_date_end) {
		this.valid_date_end = valid_date_end;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getRect() {
		return rect;
	}

	public void setRect(String rect) {
		this.rect = rect;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getFrontside_legality() {
		return frontside_legality;
	}

	public void setFrontside_legality(String frontside_legality) {
		this.frontside_legality = frontside_legality;
	}

	public String getFrontside_completeness() {
		return frontside_completeness;
	}

	public void setFrontside_completeness(String frontside_completeness) {
		this.frontside_completeness = frontside_completeness;
	}

	public String getFrontside_card_rect() {
		return frontside_card_rect;
	}

	public void setFrontside_card_rect(String frontside_card_rect) {
		this.frontside_card_rect = frontside_card_rect;
	}

	public String getBackside_legality() {
		return backside_legality;
	}

	public void setBackside_legality(String backside_legality) {
		this.backside_legality = backside_legality;
	}

	public String getBackside_completeness() {
		return backside_completeness;
	}

	public void setBackside_completeness(String backside_completeness) {
		this.backside_completeness = backside_completeness;
	}

	public String getBackside_card_rect() {
		return backside_card_rect;
	}

	public void setBackside_card_rect(String backside_card_rect) {
		this.backside_card_rect = backside_card_rect;
	}

	public String getERROR() {
		return ERROR;
	}

	public void setERROR(String eRROR) {
		ERROR = eRROR;
	}

}
