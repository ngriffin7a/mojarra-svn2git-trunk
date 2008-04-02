/**
 * 
 */
package com.sun.faces.sandbox.test.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlOutputText;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.component.YuiTreeNode;
import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;

/**
 * @author lee
 *
 */
public class TestBean {
    protected YuiTree tree;
    protected Date date;
    protected FileHolder fileHolder = new FileHolderImpl();
    
    public TestBean() {
        tree = new YuiTree();
        YuiTreeNode node1 = new YuiTreeNode();
        YuiTreeNode node2 = new YuiTreeNode();
        YuiTreeNode node3 = new YuiTreeNode();
        HtmlOutputText text1 = new HtmlOutputText(); text1.setValue("Text 1");
        HtmlOutputText text2 = new HtmlOutputText(); text2.setValue("Text 2");
        HtmlOutputText text3 = new HtmlOutputText(); text3.setValue("Text 3");

        node1.getChildren().add(text1);
        node1.getChildren().add(node3);
        node2.getChildren().add(text3);
        node3.getChildren().add(text2);

        tree.getChildren().add(node1);
        tree.getChildren().add(node2);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getPdf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.pdf");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    public byte[] getImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.png");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }

    public String getDestination() {
        return "jsp/success";
    }

    public FileHolder getFileHolder() {
        return fileHolder;
    }

    public String[] getFileNames() {
        String[] fileNames = fileHolder.getFileNames().toArray(new String[]{});
        fileHolder.clearFiles();
        
        return fileNames;
    }
    
    public List<Person> getPersonList() {
        List<Person> list = new ArrayList();
        
        return list;
    }

    public YuiTree getTree() {
        return tree;
    }

    public void setTree(YuiTree tree) {
        this.tree = tree;
    }
}

class Person {
    protected int id;
    protected String lastName;
    protected String firstName;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    
}