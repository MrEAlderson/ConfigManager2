package de.marcely.configmanager2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import de.marcely.configmanager2.objects.Comment;
import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.EmptyLine;
import de.marcely.configmanager2.objects.Tree;
import lombok.Getter;

public class FileHandler {
	
	@Getter private final ConfigFile file;
	
	public FileHandler(ConfigFile file){
		this.file = file;
	}
	
	public int load(){
		final File f = file.getFile();
		
		// check if everything is ok
		if(!f.exists())
			return IOResult.RESULT_FAILED_LOAD_MISSINGFILE;
		else if(!f.canRead())
			return IOResult.RESULT_FAILED_LOAD_NOPERMS;
		
		// prepare
		file.getRootTree().clear();
		
		// start
		try{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
			
			String line = null;
			
			Tree tree = file.getRootTree();
			
			while((line = reader.readLine()) != null){
				line = replaceFirstSpaces(line);
				
				if(line.startsWith("# "))
					tree.addChield(new Comment(tree, replaceFirstSpaces(line.substring(1))));
				else if(line.contains(":")){
					final String[] strs = line.split(":");
					String value = "";
					
					for(int i=1; i<strs.length; i++){
						value += strs[i];
						
						if(i+1<strs.length)
							value += ":";
					}
					
					tree.addChield(new Config(tree.getName(), strs[0], tree, value));
				}else if(line.endsWith("{")){
					final String name = replaceLastSpaces(line.substring(0, line.length()-1));
					
					tree = new Tree(tree.getName()+"."+name, name, tree);
					tree.getParent().addChield(tree);
				}else if(line.endsWith("}")){
					tree = tree.getParent();
					
					if(tree == null){
						reader.close();
						return IOResult.RESULT_FAILED_LOAD_NOTVALID;
					}
				}else
					tree.addChield(new EmptyLine(tree));
			}
			
			reader.close();
			
			if(!tree.equals(file.getRootTree()))
				return IOResult.RESULT_FAILED_LOAD_NOTVALID;
			
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		return IOResult.RESULT_SUCCESS;
	}
	
	public int save(){
		final File f = file.getFile();
		
		// check if everything is ok
		if(f.exists() && !f.canWrite())
			return IOResult.RESULT_FAILED_SAVE_NOPERMS;
		
		// start
		try{
			// recreate
			if(f.exists())
				f.delete();
			f.createNewFile();
			
			// check if everything is ok again
			if(!f.canWrite())
				return IOResult.RESULT_FAILED_SAVE_NOPERMS;
			
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF8"));
			final List<String> lines = getLines(file.getRootTree(), "");
			
			for(String line:lines){
				writer.write(line);
				writer.newLine();
			}
			
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
			
			return IOResult.RESULT_FAILED_UNKOWN;
		}
		
		
		return IOResult.RESULT_SUCCESS;
	}
	
	private List<String> getLines(Tree tree, String currentPrefix){
		final List<String> lines = new ArrayList<String>();
		
		for(Config c:tree.getChields()){
			if(c instanceof Tree){
				
				lines.add(currentPrefix + c.getShortName() + " {");
				lines.addAll(getLines((Tree) c, currentPrefix + "	"));
				lines.add(currentPrefix + "}");
				
			}else if(c instanceof Comment)
				lines.add(currentPrefix + "# " + c.getValue());
			else if(c instanceof EmptyLine)
				lines.add("");
			else
				lines.add(currentPrefix + c.getShortName() + ": " + c.getValue());
		}
		
		return lines;
	}
	
	private String replaceFirstSpaces(String str){
		while(str.startsWith(" ") || str.startsWith("	"))
			str = str.substring(1, str.length());
		
		return str;
	}
	
	private String replaceLastSpaces(String str){
		while(str.endsWith(" ") || str.endsWith("	"))
			str = str.substring(0, str.length()-1);
		
		return str;
	}
}