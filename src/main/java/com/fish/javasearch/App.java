package com.fish.javasearch;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App implements ActionListener {  
    JFrame frame = new JFrame("JavaSearch");
    JTabbedPane tabPane = new JTabbedPane();
    Container con = new Container(); 
    JLabel label1 = new JLabel("class name:");
    JLabel label2 = new JLabel("directory:");  
    JTextField text1 = new JTextField();
    JTextField text2 = new JTextField();
    JButton button1 = new JButton("search");
    JButton button2 = new JButton("open");
    
    JTextArea result = new JTextArea();
    JFileChooser jfc = new JFileChooser();
    
    public static List<String> searchRetList = new ArrayList<String>();
      
    public App() {
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
          
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
          
        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));
        frame.setSize(700, 430);
        frame.setContentPane(tabPane);
        
        label1.setBounds(10, 10, 100, 30);
        text1.setBounds(120, 10, 400, 30);
        button1.setBounds(550, 10, 100, 30);
        
        label2.setBounds(10, 45, 100, 30);
        text2.setBounds(120, 45, 400, 30);
        text2.setEditable(false);
        button2.setBounds(550, 45, 100, 30);
        
        result.setBounds(10, 90, 520, 270);
        
        button1.addActionListener(this);
        button2.addActionListener(this);
        con.add(label1);
        con.add(text1);
        con.add(button1);
        con.add(label2);
        con.add(text2);
        con.add(button2);
        con.add(result);
        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabPane.add("", con);
    }
    
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub  
    	if (e.getSource().equals(button1)) {
    		result.setText("");
    		findKey(text1.getText(),new File(text2.getText()));
    		
    		for(String ret:searchRetList) {
    			result.append(ret);
    			result.append("\n");
    		}
    		result.append("search end.");
    	}
        if (e.getSource().equals(button2)) {
            jfc.setFileSelectionMode(1);
            int state = jfc.showOpenDialog(null);
            if (state == 1) {
                return;
            } else {
                File f = jfc.getSelectedFile();
                text2.setText(f.getAbsolutePath());
            }
        }
    }
    
	public void findKey(String keyword, File directory) {
		if(keyword == null || keyword.length() == 0) {
			return;
		}
		if(directory == null || !directory.exists()) {
			return;
		}
		for (File file : directory.listFiles()) {
			String pathName = file.getName();
			if (file.isDirectory())
				findKey(keyword, file);
			else if (pathName.toLowerCase().endsWith(".jar"))
				try {
					String path = file.getPath();
					Enumeration<JarEntry> enu = new JarFile(path).entries();
					while (enu.hasMoreElements()) {
						String fullName = enu.nextElement().getName();
						String[] packages = fullName.split("/");
						String[] fileName = packages[packages.length - 1].split("\\.");
						String packageName = fullName.replace("/", ".");
						packageName = packageName.toLowerCase();
						keyword = keyword.toLowerCase();
						if (packageName.contains(keyword)) {
							if(!searchRetList.contains(path)) {
								searchRetList.add(path);
							}							
							continue;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
    
    public static void main(String[] args) {  
        new App();  
    }  
}  
