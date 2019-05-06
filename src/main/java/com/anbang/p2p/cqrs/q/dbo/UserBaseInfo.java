package com.anbang.p2p.cqrs.q.dbo;

import org.eclipse.jetty.util.StringUtil;

/**
 * 用户实名信息
 */
public class UserBaseInfo {
	private String id;// 用户id
	private String IDcard;// 身份证
	private String realName;// 真实姓名
	private String faceImgUrl;// 人脸图片
	private String IDcardImgUrl_front;// 身份证正面
	private String IDcardImgUrl_reverse;// 身份证反面
	private long createTime;

	/**
	 * 是否完成实名认证
	 */
	public boolean finishUserVerify() {
		if (StringUtil.isBlank(IDcard)) {
			return false;
		} else if (StringUtil.isBlank(realName)) {
			return false;
		} else if (StringUtil.isBlank(faceImgUrl)) {
			return false;
		} else if (StringUtil.isBlank(IDcardImgUrl_front)) {
			return false;
		} else if (StringUtil.isBlank(IDcardImgUrl_reverse)) {
			return false;
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
