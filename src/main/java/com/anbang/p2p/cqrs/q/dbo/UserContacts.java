package com.anbang.p2p.cqrs.q.dbo;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户紧急联系人
 *
 */
public class UserContacts {
	private String id;	//用户id
	private String userId;// 用户id
	private String directContactsPhone;// 直接联系人手机号码
	private String directContactsName;// 直接联系人姓名
	private String direactRelation;		//关系

	private String commonContactsPhone;// 一般联系人手机号码
	private String commonContactsName;// 一般联系人姓名
	private String commonRelation;		//关系
	private String addressBook;		//通讯录

	public static boolean isBlank(UserContacts contacts) {
		if (StringUtils.isAnyBlank(contacts.getDirectContactsPhone(), contacts.getDirectContactsName(),
				contacts.getDireactRelation(), contacts.getCommonContactsPhone(), contacts.getCommonContactsName(),
				contacts.getCommonRelation())) {
			return true;
		}
		return false;
	}

	public boolean finishContactsVerify() {
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDirectContactsPhone() {
		return directContactsPhone;
	}

	public void setDirectContactsPhone(String directContactsPhone) {
		this.directContactsPhone = directContactsPhone;
	}

	public String getDirectContactsName() {
		return directContactsName;
	}

	public void setDirectContactsName(String directContactsName) {
		this.directContactsName = directContactsName;
	}

	public String getDireactRelation() {
		return direactRelation;
	}

	public void setDireactRelation(String direactRelation) {
		this.direactRelation = direactRelation;
	}

	public String getCommonContactsPhone() {
		return commonContactsPhone;
	}

	public void setCommonContactsPhone(String commonContactsPhone) {
		this.commonContactsPhone = commonContactsPhone;
	}

	public String getCommonContactsName() {
		return commonContactsName;
	}

	public void setCommonContactsName(String commonContactsName) {
		this.commonContactsName = commonContactsName;
	}

	public String getCommonRelation() {
		return commonRelation;
	}

	public void setCommonRelation(String commonRelation) {
		this.commonRelation = commonRelation;
	}

	public String getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(String addressBook) {
		this.addressBook = addressBook;
	}
}
