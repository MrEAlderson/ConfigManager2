package de.marcely.configmanager2.objects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;

public class Tree extends Config {
	
	@Getter private final List<Config> chields = new ArrayList<Config>();
	
	public Tree(String name, String shortName, Tree parent){
		super(name, shortName, parent);
	}
	
	public void addChield(Config config){
		this.chields.add(config);
	}
	
	public List<Tree> getTreeChields(){
		final List<Tree> list = new ArrayList<Tree>();
		
		for(Config c:chields){
			if(c instanceof Tree)
				list.add((Tree) c);
		}
		
		return list;
	}
	
	public @Nullable Tree getTreeChield(String name){
		for(Tree t:getTreeChields()){
			if(t.getShortName().equals(name))
				return t;
		}
		
		return null;
	}
	
	public List<Config> getConfigChields(){
		final List<Config> list = new ArrayList<Config>();
		
		for(Config c:chields){
			if(!(c instanceof Tree))
				list.add(c);
		}
		
		return list;
	}
	
	public @Nullable Config getConfigChield(String name){
		for(Config c:getConfigChields()){
			if(c.getShortName().equals(name))
				return c;
		}
		
		return null;
	}
	
	public void clear(){
		this.chields.clear();
	}
}
