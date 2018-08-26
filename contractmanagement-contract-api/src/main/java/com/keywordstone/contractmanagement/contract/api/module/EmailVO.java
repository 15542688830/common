package com.keywordstone.contractmanagement.contract.api.module;

import lombok.Data;

@Data
public class EmailVO {

	private String toEmail;
	private String toName;
	private String fromEmail;
	private String fromName;
	private String subject;
	private String htmlMsg;
	private String url;

	@Override
	public String toString() {
		return "EmailVO{" +
				"toEmail='" + toEmail + '\'' +
				", toName='" + toName + '\'' +
				", fromEmail='" + fromEmail + '\'' +
				", fromName='" + fromName + '\'' +
				", subject='" + subject + '\'' +
				", htmlMsg='" + htmlMsg + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
