# xmlbuilder
Dependence:  
dom4j  
  
  
Build XML file with an Object, and get an Object from XML  
Use ```@XmlNode``` for marking the fields which should be included in the XML    
All classes included in XML must have default constructor  
  
Type supporting:  
primitive types except their wrapping classes  
String  
classes with default constructor  
  
Encoding:  
UTF-8 only  
  
Usage:  
Get XML String ```XmlUtils.xmlString(Object) ```  
Get Object from XML String ```XmlUtils.xmlObject(String) ```  
Write XML into a file  ```new XmlWriter().write(Object, File) ```  
Get Object from XML ```new XmlReader().read(File) ```  
