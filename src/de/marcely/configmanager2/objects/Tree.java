package de.marcely.configmanager2.objects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.marcely.configmanager2.ConfigFile;
import lombok.Getter;

public class Tree extends Config {
	
	private final ConfigFile file;
	@Getter private final List<Config> childs = new ArrayList<Config>();
	@Getter private final List<String> rawChilds = new ArrayList<String>();
	
	public Tree(String name, Tree parent){
		super(name, parent);
		
		this.file = null;
	}
	
	public Tree(ConfigFile file){
		super("", null);
		
		this.file = file;
	}
	
	@Override
	public byte getType(){
		return TYPE_TREE;
	}
	
	public void addChild(Config config){
		this.childs.add(config);
		
		if(config.getType() == Config.TYPE_CONFIG && isInsideRoot())
			getConfigFile().getPicker().getAllConfigs().add(config);
	}
	
	public List<Tree> getTreeChilds(){
		final List<Tree> list = new ArrayList<Tree>();
		
		for(Config c:childs){
			if(c.getType() == Config.TYPE_TREE)
				list.add((Tree) c);
		}
		
		return list;
	}
	
	public @Nullable Tree getTreeChild(String name){
		for(Tree t:getTreeChilds()){
			if(t.getName().equals(name))
				return t;
		}
		
		return null;
	}
	
	public List<Config> getConfigChilds(){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:childs){
			if(c.getType() == Config.TYPE_CONFIG)
				list.add(c);
		}
		
		return list;
	}
	
	public @Nullable Config getConfigChild(String name){
		for(Config c:getConfigChilds()){
			if(c.getType() == Config.TYPE_CONFIG && c.getName() != null && c.getName().equals(name))
				return c;
		}
		
		return null;
	}
	
	public List<Config> getConfigChilds(String name){
		final List<Config> list = new ArrayList<>();
		
		for(Config c:getConfigChilds()){
			if(c.getType() == Config.TYPE_CONFIG && c.getName() != null && c.getName().equals(name))
				list.add(c);
		}
		
		return list;
	}
	
	@Deprecated
	public List<ListItem> getListItems(){
		final List<ListItem> items = new ArrayList<ListItem>();
		
		for(Config c:getConfigChilds()){
			if(c.getType() == Config.TYPE_LISTITEM)
				items.add((ListItem) c);
		}
		
		return items;
	}
	
	public void clear(){
		this.childs.clear();
	}
	
	public boolean isRoot(){
		return this.file != null;
	}
	
	public boolean isInsideRoot(){
		return isRoot() ? true : getParent().isInsideRoot();
	}
	
	public @Nullable ConfigFile getConfigFile(){
		if(isInsideRoot())
			return isRoot() ? this.file : getParent().getConfigFile();
		else
			return null;
	}
}
