//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jamontes79;

import java.util.Enumeration;
import java.util.Vector;

public class StringTokenizer implements Enumeration {
    private Vector m_elements;

    public StringTokenizer(String strString, String strToken) {
        int iPos;
        for(this.m_elements = new Vector(); (iPos = strString.indexOf(strToken)) >= 0; strString = strString.substring(iPos + strToken.length())) {
            this.m_elements.addElement(strString.substring(0, iPos));
            if(strString.length() <= iPos) {
                break;
            }
        }

        if(strString.length() > 0) {
            this.m_elements.addElement(strString);
        }

    }

    public String nextToken() {
        String str = null;
        if(this.m_elements.size() > 0) {
            str = (String)this.m_elements.elementAt(0);
            this.m_elements.removeElementAt(0);
        }

        return str;
    }

    public boolean hasMoreTokens() {
        return this.m_elements.size() > 0;
    }

    public int countTokens() {
        return this.m_elements.size();
    }

    public boolean hasMoreElements() {
        return this.hasMoreTokens();
    }

    public Object nextElement() {
        return this.nextToken();
    }
}
