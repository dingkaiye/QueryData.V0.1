package com.ods.message;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ods.common.NameSpace;

import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "LocalHead", namespace = NameSpace.ODS_URL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localHead", propOrder = {
    "IcSrNo"
})
public class LocalHead {
	
	@XmlElement(name="IcSrNo", namespace = NameSpace.ODS_URL )
	public String IcSrNo = null;

}
