# csvw-validator-java-demo
This is a demo project to show how to use the [csvw-validator](https://github.com/malyvoj3/csvw-validator) tool as a library. Dependency management is done by using Maven.

## Tips to make the code work
1. Specify the version of spring to 5.1.12 to include all the required methods.
2. Use an old version (recommond 3.5) of org-apache-commons-lang3 by adding the following code to `pom.xml`:
```
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>3.5</version>
</dependency>
```
3. Make sure to add tmp direcotry using the following code before validation, otherwise the csv file won't be downloaded when using the metafileUrl only:
```java
File file = new File("tmp");
if (file.exists()) {
	try {
		FileUtils.deleteDirectory(file);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
file.mkdir();
```