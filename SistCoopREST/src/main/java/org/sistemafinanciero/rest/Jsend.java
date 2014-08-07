package org.sistemafinanciero.rest;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jsend")
@XmlAccessorType(XmlAccessType.NONE)
public class Jsend implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private enum STATUS_VALUE {
		success, fail, error
	}

	private STATUS_VALUE status;
	private BigInteger id;
	private Object data;
	private List<String> message;

	public static Jsend getSuccessJSend() {
		Jsend jsend = new Jsend(STATUS_VALUE.success);
		return jsend;
	}

	public static Jsend getSuccessJSend(BigInteger id) {
		Jsend jsend = new Jsend(STATUS_VALUE.success);
		jsend.setId(id);
		return jsend;
	}
	
	public static Jsend getSuccessJSend(String message) {
		Jsend jsend = new Jsend(STATUS_VALUE.success);
		jsend.addMessage(message);
		return jsend;
	}
	
	public static Jsend getErrorJSend() {
		Jsend jsend = new Jsend(STATUS_VALUE.fail);
		return jsend;
	}

	public static Jsend getErrorJSend(String message) {
		Jsend jsend = new Jsend(STATUS_VALUE.fail);
		jsend.addMessage(message);
		return jsend;
	}

	public Jsend addMessage(String message) {
		this.message.add(message);
		return this;
	}

	private Jsend() {
		// TODO Auto-generated constructor stub
	}

	private Jsend(STATUS_VALUE status, Object data) {
		this.status = status;
		this.data = data;
		this.message = new ArrayList<String>();
	}

	private Jsend(STATUS_VALUE status) {
		this.status = status;
		this.data = null;
		this.message = new ArrayList<String>();
	}

	@XmlElement
	public STATUS_VALUE getStatus() {
		return status;
	}

	public void setStatus(STATUS_VALUE status) {
		this.status = status;
	}

	@XmlElement
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@XmlElement
	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	@XmlElement
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

}
