package de.marcely.configmanager2;

import java.io.File;

import javax.annotation.Nullable;

import de.marcely.configmanager2.objects.Comment;
import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.EmptyLine;
import de.marcely.configmanager2.objects.Tree;
import lombok.Getter;

public class ConfigFile {
	
	public boolean getConfigNeverNull = false;
	
	@Getter private final File file;
	private final FileHandler fileHandler;
	
	@Getter private final Tree rootTree = new Tree("", "", null);
	
	public ConfigFile(File file){
		this(file, false);
	}
	
	public ConfigFile(File file, boolean getConfigNeverNull){
		this.file = file;
		this.fileHandler = new FileHandler(this);
		this.getConfigNeverNull = getConfigNeverNull;
	}
	
	public Config addConfig(String path, Object value){
		return addConfig(path, value.toString());
	}
	
	public Config addConfig(String path, String value){
		final String[] strs = path.split("\\.");
		
		final Tree tree = path.contains(".") ? getTree(path.substring(0, path.lastIndexOf('.')), true) : getRootTree();
		final Config config = new Config(path, strs[strs.length-1], tree, value);
		tree.addChield(config);
		
		return config;
	}
	
	public Comment addComment(String value){
		return addComment("", value);
	}
	
	public Comment addComment(String path, String value){
		final Tree tree = getTree(path, true);
		final Comment config = new Comment(tree, value);
		tree.addChield(config);
		
		return config;
	}
	
	public EmptyLine addEmptyLine(){
		return addEmptyLine("");
	}
	
	public EmptyLine addEmptyLine(String path){
		final Tree tree = getTree(path, true);
		final EmptyLine config = new EmptyLine(tree);
		tree.addChield(config);
		
		return config;
	}
	
	public @Nullable Config getConfig(String path){
		final Tree tree = getTree(path.substring(0, path.lastIndexOf('.')), false);
		
		if(tree != null){
			final String[] strs = path.split("\\.");
			
			return tree.getConfigChield(strs[strs.length-1]);
		}else
			return !getConfigNeverNull ? null : new Config(null, null, null, null);
	}
	
	private @Nullable Tree getTree(String path, boolean newInstance){
		if(path.equals(""))
			return getRootTree();
		
		final String[] strs = path.split("\\.");
		
		int cIndex = 0;
		String name = "";
		Tree cTree = rootTree;
		
		while(cIndex < strs.length){
			// append name
			if(!name.equals(""))
				name += ".";
			name += strs[cIndex];
			
			// get tree
			Tree nTree = cTree.getTreeChield(strs[cIndex]);
			
			// new tree if chield is missing
			if(nTree == null){
				if(newInstance){
					nTree = new Tree(name, strs[cIndex], cTree);
					cTree.addChield(nTree);
				}else
					return null;
			}
			
			// next
			cTree = nTree;
			cIndex++;
		}
		
		return cTree;
	}
	
	public void clear(){
		this.rootTree.clear();
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
