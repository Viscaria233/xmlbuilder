# xmlbuilder
Dependence:  
            dom4j  
  
  
Build XML file with an Object, and get an Object from XML  
Support primitive type  
Using 2 Annocations @XMLBean, @XMLNode  
  
Encoding: UTF-8 only  
  
Example:  
```java
@XMLBean()
class A {
    double aDouble = 20.17;                     //has not @XMLNode. Will not be included in XML
    @XMLNode()                                  //the tag name is the field name "aFloat"
    float aFloat;                               //0
    @XMLNode()
    private int aInt = 2017;
    @XMLNode(name = "text")                     //the tag name is "text"
    protected String aString = "xmlbuilder";
    @XMLNode()
    public B aB;
}

@XMLBean(name = "BeanB")
class B {
    @XMLNode()
    public A bA;
    @XMLNode()
    public A nullA;                             //null
    @XMLNode()
    public C bC;                                //class C has not @XMLNode
}

class C {                                       //ignored
    public int cInt = 100;
}

/*
    only the instance of class with @XMLBean can be use for builder.build(). @XMLBean means the root element of XML
    all fields with @XMLNode will be included in XML although its class has not @XMLBean. @XMLNode means child element of XML
*/
public static void main(String[] args) {
    XMLBuilder b = new XMLBuilder();
    A a = new A();
    B b = new B();
    C c = new C();
    
    a.aB = b;
    b.bA = a;                                   //reference a-->b-->a-->b-->a......
    b.bC = c;
    System.out.println(b.build(a));
}
```
Result:  
Same class and same id means the same instance  
  
```html
<?xml version="1.0" encoding="UTF-8"?>
<A class="com.haochen.xmlbuilder.A" id="0">     <!--refers to the instance of A which has the id=0-->
                                                    <!--if id=0 not exists, then refers to a new instance of A given an id=0-->
	<aFloat
			class="float"
			field="aFloat">
		0.0
	</aFloat>
	<aInt
			class="int"
			field="aInt">
		2017
	</aInt>                                         <!--@XMLNode() use the field name for tag name-->
	<text                                           
			class="java.lang.String"
			field="aString">
		xmlbuilder
	</text>                                         <!--@XMLNode(name = "text") tag name is "text"-->
	<aB
			class="com.haochen.xmlbuilder.B"
			field="aB"
			id="0">
		<bA
				class="com.haochen.xmlbuilder.A"
				field="bA"
				id="0">                             <!--refers to the Object of A which has the id=0-->
		</bA>
		<bC
				class="com.haochen.xmlbuilder.C"
				field="bC"
				id="0">
		</bC>
	</aB>                                           <!--this is a field of A, so @XMLBean(name = "Beanb") has ignored-->
                                                    <!--tag name was specified by @XMLNode()-->
</A>
```
builder.build(c) will get nothing because class C has not @XMLBean  
  
  
Write XML into a file:  
```java
public static void main(String args) {
    A a;
    /*
        Create the same Object a like [Row 44]
    */
    
    File file = new File("test.xml");
    XMLWriter writer = new XMLWriter();
    try {
        writer.write(a, file);
    } catch (IllegalFileFormatException e) {        //if the file is not a .xml file
        e.printStackTrace();
    }
}
```
Result:  
write XML into test.xml with UTF-8  
  
  
Get Object:  
```java
public static void main(String args) {
    File file = new File("test.xml");
    XMLReader reader = new XMLReader();
    try {
        Object o = reader.read(file);               //o is a new Object of class A, which has the same content with [Row 44]
    } catch (IllegalFileFormatException e) {        //if the file is not a .xml file
        e.printStackTrace();
    }
}
```
