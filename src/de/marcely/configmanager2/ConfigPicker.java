package de.marcely.configmanager2;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.marcely.configmanager2.objects.Comment;
import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.Description;
import de.marcely.configmanager2.objects.EmptyLine;
import de.marcely.configmanager2.objects.Tree;
import lombok.Getter;

public class ConfigPicker {
	
	@Getter private final ConfigFile file;
	@Getter private final List<Config> allConfigs = new ArrayList<Config>();
	
	public ConfigPicker(ConfigFile file){
		this.file = file;
	}
	
	
	public Config addConfig(String path, Object value){
		return addConfig(path, value.toString());
	}
	
	public Config addConfig(String path, String value){
		final String[] strs = path.split("\\.");
		
		final Tree tree = path.contains(".") ? getTree(path.substring(0, path.lastIndexOf('.')), true) : file.getRootTree();
		final Config config = new Config(path, strs[strs.length-1], tree, value);
		tree.addChield(config);
		allConfigs.add(config);
		
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
			return !file.getConfigNeverNull ? null : new Config(null, null, null, null);
	}
	
	public List<Config> getConfigsWhichStartWith(String name){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:allConfigs){
			if(c.getName().startsWith(name))
				list.add(c);
		}
		
		return list;
	}
	
	public Description setDescription(String name, String value){
		if(!containsBase())
			file.getRootTree().getChields().add(0, new EmptyLine(file.getRootTree()));
		
		Description config = getDescription(name);
		if(config == null)
			config = new Description(file.getRootTree(), name, value);
		else
			config.setValue(value);
			
		file.getRootTree().getChields().add(0, config);
		
		return config;
	}
	
	public @Nullable Description getDescription(String name){
		for(Config c:file.getRootTree().getChields()){
			if(c.getShortName() != null && c.getShortName().equals(name) && c.getType() == Config.TYPE_DESCRIPTION)
				return (Description) c;
		}
		
		return null;
	}
	
	public boolean containsBase(){
		for(Config c:file.getRootTree().getChields()){
			if(c.getType() == Config.TYPE_DESCRIPTION && ((Description) c).isBase())
				return true;
		}
		
		return false;
	}
	
	private @Nullable Tree getTree(String path, boolean newInstance){
		if(path.equals(""))
			return file.getRootTree();
		
		final String[] strs = path.split("\\.");
		
		int cIndex = 0;
		String name = "";
		Tree cTree = file.getRootTree();
		
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
}
