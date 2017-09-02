package de.marcely.configmanager2.objects;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class Config {
	
	@Getter private final String name, shortName;
	@Getter private final Tree parent;
	
	@Getter @Setter String value;
	
	public Config(String name, String shortName, Tree parent){
		this(name, shortName, parent, null);
	}
	
	public Config(String name, String shortName, Tree parent, String value){
		this.name = name;
		this.shortName = shortName;
		this.parent = parent;
		this.value = value;
	}
	
	public @Nullable Boolean getValueAsBoolean(){
		return null;
	}
	
	// Util
	private static @Nullable Boolean getBoolean(String str){
		return Boolean.valueOf(str);
	}
}