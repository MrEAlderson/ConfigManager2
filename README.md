# ConfigManager2
Fast, light and easy to use config file management library

Example:
```Java
final ConfigFile file = new ConfigFile(new File("test.cm2"));

file.addComment("Don't change this");
file.addConfig("file-version", "1.0");

file.addEmptyLine();

file.addConfig("author", "MrEAlderson");
file.addConfig("language", "english");

file.addEmptyLine();

file.addConfig("candidate.Donald-Trump.votes", "306");
file.addConfig("candidate.Hillary-Clinton.votes", "232");
file.addConfig("candidate.Donald-Trump.Coolness", "0");
file.addConfig("candidate.Hillary-Clinton.Coolness", "-1");
file.addConfig("candidate.amount", "2");

file.save```

Will result as
```cm2
# Don't change this
file-version: 1.0

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
