package org.firstinspires.ftc.robotcore.internal.system;

import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Dom2XmlPullBuilder {
  protected static final boolean NAMESPACES_SUPPORTED = false;
  
  protected Document newDoc() throws XmlPullParserException {
    try {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      documentBuilder.getDOMImplementation();
      return documentBuilder.newDocument();
    } catch (FactoryConfigurationError factoryConfigurationError) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("could not configure factory JAXP DocumentBuilderFactory: ");
      stringBuilder.append(factoryConfigurationError);
      throw new XmlPullParserException(stringBuilder.toString(), null, factoryConfigurationError);
    } catch (ParserConfigurationException parserConfigurationException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("could not configure parser JAXP DocumentBuilderFactory: ");
      stringBuilder.append(parserConfigurationException);
      throw new XmlPullParserException(stringBuilder.toString(), null, parserConfigurationException);
    } 
  }
  
  protected XmlPullParser newParser() throws XmlPullParserException {
    return XmlPullParserFactory.newInstance().newPullParser();
  }
  
  public Element parse(Reader paramReader) throws XmlPullParserException, IOException {
    return parse(paramReader, newDoc());
  }
  
  public Element parse(Reader paramReader, Document paramDocument) throws XmlPullParserException, IOException {
    XmlPullParser xmlPullParser = newParser();
    xmlPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
    xmlPullParser.setInput(paramReader);
    xmlPullParser.next();
    return parse(xmlPullParser, paramDocument);
  }
  
  public Element parse(XmlPullParser paramXmlPullParser, Document paramDocument) throws XmlPullParserException, IOException {
    return parseSubTree(paramXmlPullParser, paramDocument);
  }
  
  public Element parseSubTree(XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    return parseSubTree(paramXmlPullParser, newDoc());
  }
  
  public Element parseSubTree(XmlPullParser paramXmlPullParser, Document paramDocument) throws XmlPullParserException, IOException {
    return (new BuildProcess()).parseSubTree(paramXmlPullParser, paramDocument);
  }
  
  static class BuildProcess {
    private Document docFactory;
    
    private XmlPullParser pp;
    
    private boolean scanNamespaces = true;
    
    private BuildProcess() {}
    
    private void declareNamespaces(XmlPullParser param1XmlPullParser, Element param1Element) throws DOMException, XmlPullParserException {
      if (this.scanNamespaces) {
        this.scanNamespaces = false;
        int i = param1XmlPullParser.getNamespaceCount(param1XmlPullParser.getDepth()) - 1;
        int j;
        for (j = i; j >= param1XmlPullParser.getNamespaceCount(0); j--) {
          String str = param1XmlPullParser.getNamespacePrefix(j);
          int k = i;
          while (true) {
            if (k > j) {
              String str1 = param1XmlPullParser.getNamespacePrefix(k);
              if ((str != null && str.equals(str1)) || (str != null && str == str1))
                break; 
              k--;
              continue;
            } 
            declareOneNamespace(param1XmlPullParser, j, param1Element);
            break;
          } 
        } 
      } else {
        for (int i = param1XmlPullParser.getNamespaceCount(param1XmlPullParser.getDepth() - 1); i < param1XmlPullParser.getNamespaceCount(param1XmlPullParser.getDepth()); i++)
          declareOneNamespace(param1XmlPullParser, i, param1Element); 
      } 
    }
    
    private void declareOneNamespace(XmlPullParser param1XmlPullParser, int param1Int, Element param1Element) throws DOMException, XmlPullParserException {
      String str1;
      String str3 = param1XmlPullParser.getNamespacePrefix(param1Int);
      String str2 = param1XmlPullParser.getNamespaceUri(param1Int);
      if (str3 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("xmlns:");
        stringBuilder.append(str3);
        str1 = stringBuilder.toString();
      } else {
        str1 = "xmlns";
      } 
      param1Element.setAttributeNS("http://www.w3.org/2000/xmlns/", str1, str2);
    }
    
    private Element parseSubTree() throws XmlPullParserException, IOException {
      if (this.pp.getEventType() == 0)
        while (this.pp.getEventType() != 2)
          this.pp.next();  
      this.pp.require(2, null, null);
      String str1 = this.pp.getName();
      String str2 = this.pp.getNamespace();
      Element element = this.docFactory.createElementNS(str2, str1);
      for (int i = 0; i < this.pp.getAttributeCount(); i++) {
        String str4 = this.pp.getAttributeNamespace(i);
        String str3 = this.pp.getAttributeName(i);
        String str5 = this.pp.getAttributeValue(i);
        if (str4 == null || str4.length() == 0) {
          element.setAttribute(str3, str5);
        } else {
          String str7 = this.pp.getAttributePrefix(i);
          String str6 = str3;
          if (str7 != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str7);
            stringBuilder.append(":");
            stringBuilder.append(str3);
            str6 = stringBuilder.toString();
          } 
          element.setAttributeNS(str4, str6, str5);
        } 
      } 
      while (this.pp.next() != 3) {
        if (this.pp.getEventType() == 2) {
          element.appendChild(parseSubTree(this.pp, this.docFactory));
          continue;
        } 
        if (this.pp.getEventType() == 4) {
          String str = this.pp.getText();
          element.appendChild(this.docFactory.createTextNode(str));
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected event ");
        stringBuilder.append(XmlPullParser.TYPES[this.pp.getEventType()]);
        throw new XmlPullParserException(stringBuilder.toString(), this.pp, null);
      } 
      this.pp.require(3, str2, str1);
      return element;
    }
    
    public Element parseSubTree(XmlPullParser param1XmlPullParser, Document param1Document) throws XmlPullParserException, IOException {
      this.pp = param1XmlPullParser;
      this.docFactory = param1Document;
      return parseSubTree();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Dom2XmlPullBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */