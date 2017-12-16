package com.ods.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ods.common.NameSpace;

@XmlRootElement(name="TestHead", namespace = NameSpace.ODS_URL )  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder={"userName","userAge"})  
public class TestObj {
	@XmlElement(name = "SvcId", namespace = NameSpace.ODS_URL )
	private String userName;
	
	private String userAge;

}
