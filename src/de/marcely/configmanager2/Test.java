package de.marcely.configmanager2;

import java.io.File;
import java.util.Random;

public class Test {
	
	private static final Random RAND = new Random();
	
	public static void main(String[] args){
		final ConfigFile file = new ConfigFile(new File("test.cm2"));
		for(int i=0; i<RAND.nextInt(100)+100; i++){
			file.addConfig(randomConfig(), "test");
			for(int i1=0; i1<RAND.nextInt(10); i1++)
				file.addComment(randomConfig());
		}
		
		long s1 = System.nanoTime();
		file.save();
		
		System.out.println((System.nanoTime()-s1)/1000000F + "ms write time");
		s1 = System.nanoTime();
		file.load();
		System.out.println((System.nanoTime()-s1)/1000000F + "ms read time");
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
