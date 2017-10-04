package de.marcely.configmanager2.objects;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class Config {
	
	public static final int TYPE_TREE = 0;
	public static final int TYPE_CONFIG = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_EMPTYLINE = 3;
	public static final int TYPE_DESCRIPTION = 4;
	
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
	
	public int getType(){
		return TYPE_CONFIG;
	}
	
	public @Nullable Boolean getValueAsBoolean(){
		return null;
	}
	
	// Util
	private static @Nullable Boolean getBoolean(String str){
		return Boolean.valueOf(str);
	}
}