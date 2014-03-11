# Java Common Extensions

The common-extensions provides utility methods for some basic classes.
When project is using `@ExtensionMethod` annotation of Lombok they can be used as a Java extension as shown below.

### example
	@ExtensionMethod(StringExtensions.class)
	public class Example {
		public void hello() {
			System.out.println("1234".toInt());
			System.out.println("2014-03-07".toDate());
			System.out.println("http://test.test/foo".toURL().getContent());
		}
	}

## How to Use

### Requirements

- Java 7 (JDK 1.7) or higher. Set environment variable `JAVA_HOME` to point to the root directory of JDK.
- [Maven 3.x](http://maven.apache.org/)

### Maven Repository

Add dependency and repository to pom.xml.

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>0.12.4</version>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.github.emalock3.common</groupId>
			<artifactId>common-extensions</artifactId>
			<version>0.0.2</version>
		</dependency>
		
		...
		
		<repository>
			<id>emalock3-mvn-repo</id>
			<url>https://github.com/emalock3/mvn-repo/blob/master/</url>
		</repository>

## Also

- [Lombok @ExtensionMethod](http://projectlombok.org/features/experimental/ExtensionMethod.html)

## License

Apache License, Version 2.0
