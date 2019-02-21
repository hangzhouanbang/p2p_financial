package com.anbang.p2p.cqrs.q.dbo;

/**
 * 用户紧急联系人
 *
 */
public class UserContacts {
	private String id;
	private String userId;// 用户id
	private String contactsPhone;// 联系人手机号码
	private String contactsName;// 联系人姓名

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

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
}
