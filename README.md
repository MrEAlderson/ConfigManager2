# ConfigManager2
Fast, light and easy to use config file management library:
(Probably much easier to use than YAML, JSON, XML etc.) 

============================================

No other libraries required.
Download available at: https://github.com/MrEAlderson/ConfigManager2/releases

============================================

Example to write a config manually:
```Java
final ConfigFile file = new ConfigFile(new File("test.cm2"));
final ConfigPicker picker = file.getPicker();

picker.setDescription("file-version", "1.0");

picker.addEmptyLine();

picker.addConfig("author", "MrEAlderson");
picker.addConfig("language", "english");

picker.addEmptyLine();

picker.addConfig("candidate.Donald-Trump.votes", "306");
picker.addConfig("candidate.Hillary-Clinton.votes", "232");
picker.addConfig("candidate.Donald-Trump.Coolness", "0");
picker.addConfig("candidate.Hillary-Clinton.Coolness", "-1");
picker.addConfig("candidate.amount", "2");

file.save();
```

Will result as
```cm2
!file-version: 1.0

author: MrEAlderson
language: english

candidate {
	Donald-Trump {
		votes: 306
		Coolness: 0
	}
	Hillary-Clinton {
		votes: 232
		Coolness: -1
	}
	amount: 2
}
```

With:
3.140133ms write time
1.005645ms read time


============================================

Example to read a config file:
```Java
final ConfigFile file = new ConfigFile(new File("test.cm2"));
final ConfigPicker picker = file.getPicker();

file.load();

final Description version = picker.getDescription("file-version");
final Config author = picker.getConfig("author");
final Config language = picker.getConfig("language");
final Config blabla = picker.addConfig("candidate.Donald-Trump.votes");

if(version != null){
	System.out.println("Version: " + version.getValue());
}

if(author != null){
	// ...
}

if(language != null){
	// ...
}

// ...

file.clear();

```
