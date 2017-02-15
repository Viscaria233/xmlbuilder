# xmlbuilder
Dependence:  
dom4j  
  
  
Build XML file with an Object, and get an Object from XML  
Use ```java @XmlNode``` for marking the fields which should be included in the XML    
All classes included in XML must have default constructor  
  
Type supporting:  
primitive types except their wrapping classes  
String  
classes with default constructor  
  
Encoding:  
UTF-8 only  
  
Example:  
Get XML String ```java XmlUtils.xmlString(Object) ```  
Get Object from XML String ```java XmlUtils.xmlObject(String) ```  
Write XML into a file  ```java new XmlWriter().write(Object, File) ```  
Get Object from XML ```java new XmlReader().read(File) ```  
