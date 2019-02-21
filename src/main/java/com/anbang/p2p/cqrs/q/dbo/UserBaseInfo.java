package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户基本信息
 */
public class UserBaseInfo {
	private String id;// 用户id
	private String nickname;// 用户昵称
	private String headimgurl;// 头像
	private String phone;// 手机号码
	private String IDcard;// 身份证
	private String realName;// 真实姓名
	private String faceImgUrl;// 人脸图片
	private String IDcardImgUrl_front;// 身份证正面
	private String IDcardImgUrl_reverse;// 身份证反面

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String iDcard) {
		IDcard = iDcard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getFaceImgUrl() {
		return faceImgUrl;
	}

	public void setFaceImgUrl(String faceImgUrl) {
		this.faceImgUrl = faceImgUrl;
	}

	public String getIDcardImgUrl_front() {
		return IDcardImgUrl_front;
	}

	public void setIDcardImgUrl_front(String iDcardImgUrl_front) {
		IDcardImgUrl_front = iDcardImgUrl_front;
	}

	public String getIDcardImgUrl_reverse() {
		return IDcardImgUrl_reverse;
	}

	public void setIDcardImgUrl_reverse(String iDcardImgUrl_reverse) {
		IDcardImgUrl_reverse = iDcardImgUrl_reverse;
	}

}
