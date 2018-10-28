package de.marcely.configmanager2;

import java.io.File;

import de.marcely.configmanager2.objects.Tree;
import lombok.Getter;

public class ConfigFile {
	
	public static final String VERSION = "2.1";
	
	public boolean getConfigNeverNull = false;
	
	@Getter private final File file;
	private final FileHandler fileHandler;
	@Getter private final ConfigPicker picker;
	
	@Getter private final Tree rootTree = new Tree(this);
	
	public ConfigFile(File file){
		this(file, false);
	}
	
	public ConfigFile(File file, boolean getConfigNeverNull){
		this.file = file;
		this.fileHandler = new FileHandler(this);
		this.picker = new ConfigPicker(this);
		
		this.getConfigNeverNull = getConfigNeverNull;
	}
	
	public void clear(){
		this.rootTree.clear();
		this.picker.getAllConfigs().clear();
	}
	
	/**
	 * 
	 * @return Returns the result of the class IOResult
	 */
	public int load(){
		return fileHandler.load();
	}
	
	/**
	 * 
	 * @return Returns the result of the class IOResult
	 */
	public int save(){
		return fileHandler.save();
	}
	
	public boolean exists(){
		return this.file.exists();
	}
}
