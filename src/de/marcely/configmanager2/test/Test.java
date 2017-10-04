package de.marcely.configmanager2.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.marcely.configmanager2.ConfigFile;
import de.marcely.configmanager2.ConfigPicker;

public class Test {
	
	private static final Random RAND = new Random();
	
	public static void main(String[] args) throws InterruptedException{
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final List<String> cfgs = new ArrayList<String>();
				final List<String> comments = new ArrayList<String>();
				
				for(int i=0; i<100; i++)
					cfgs.add("test.test.test.test");
				
				for(int i=0; i<100; i++)
					comments.add("Simple config xd");
				
				System.out.println("Testing with ConfigManager 2");
				
				final ConfigFile file = new ConfigFile(new File("test.cm2"));
				final ConfigPicker picker = file.getPicker();
				
				for(String cfg:cfgs)
					picker.addConfig(cfg, "test");
				for(String comment:comments)
					picker.addComment(comment);
				
				// prepare ssd
				file.save();
				file.save();
				
				long s1 = System.nanoTime();
				file.save();
				
				System.out.println((System.nanoTime()-s1)/1000000F + "ms write time");
				s1 = System.nanoTime();
				file.load();
				System.out.println((System.nanoTime()-s1)/1000000F + "ms read time");
				
				System.out.println("Testing with ConfigManager 1");
				
				final ConfigManager cm = new ConfigManager(new File("test.cm2"));
				
				for(String cfg:cfgs)
					cm.addConfig(cfg, "test");
				for(String comment:comments)
					cm.addComment(comment);
			}
		}.start();
	}
	
	private static String randomConfig(){
		String str = "";
		
		for(int i=0; i<50; i++){
			if(str.length() >= 1 && !str.endsWith(".") && RAND.nextInt(10) >= 8)
				str += ".";
			else
				str += (char) RAND.nextInt(Character.MAX_VALUE);
		}
		
		return str;
	}
}
