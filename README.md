# csvw-validator-java-demo
This is a demo project to show how to use the [csvw-validator](https://github.com/malyvoj3/csvw-validator) tool as a library. Dependency management is done by using Maven.

## Tips to make the code work
1. Specify the version of Spring to 5.1.12 to include all the required methods.
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
4. To download all the dependencies, go to the project folder in terminal and run the following Maven commond
```shell
mvn install dependency:copy-dependencies
```
Then all the JAR files will be downloaded to `./target/dependency/` folder.

## Input and expected output
This demo has a default input that is specified in the code (the main method). The input is the [tree-ops example](https://w3c.github.io/csvw/tests/test011/tree-ops.csv-metadata.json) on the [CSVW TEST CASES website](https://w3c.github.io/csvw/tests/)]. 

By importing this project to Eclipse as a Maven project and running it, the console output should looks like this:

```console
Metadata URL: https://w3c.github.io/csvw/tests/test011/tree-ops.csv-metadata.json
Result: PASSED
Strict mode: false
Total errors: 0
Warning errors: 0
Error errors: 0
Fatal errors: 0
Processed tables: 1
Processed rows: 3
Processed columns: 5
Errors:
```

Also, a file called `rdf_result.ttl` will be generated at the root folder. This is the result of converting the input data file into linked data format (.ttl) based on the metadata information.
