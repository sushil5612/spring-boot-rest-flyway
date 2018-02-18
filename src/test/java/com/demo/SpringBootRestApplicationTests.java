package com.demo;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRestApplicationTests {

	@Test
	public void checkDuplicateVersion() {

		System.out.println("Starting checkDuplicateVersion");

		List<ClassLoader> classLoadersList = new LinkedList<>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forManifest(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("local"))));

		Reflections reflections1 = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forManifest(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("common"))));

		Stream<String> stream1=  reflections.getStore().get("ResourcesScanner").keys().stream();
		Stream<String> stream2=  reflections1.getStore().get("ResourcesScanner").keys().stream();

		Stream<String> resultingStream=Stream.of(stream1,stream2).flatMap(i -> i);

		//resultingStream.forEach(System.out::println);

		List<String> existingFileVersions=
				resultingStream
						.filter(f -> f.startsWith("V"))
						.filter(f -> f.endsWith(".sql"))
						//.forEach(System.out::println);
						.map(n -> n.split("__")[0].substring(1))
						//.forEach(System.out::println);
						.collect(Collectors.toList());

		Set<String> duplicateVersion=existingFileVersions.stream().filter(i -> Collections.frequency(existingFileVersions, i) >1)
				.collect(Collectors.toSet());

		duplicateVersion.forEach( i -> System.out.println("Duplicate Version found "+i));

		Assert.assertEquals(0,duplicateVersion.size());
	}

	@Test
	public void checkFlywayFileNamingStandard(){

		System.out.println("Starting checkFlywayFileNamingStandard");

		List<ClassLoader> classLoadersList = new LinkedList<>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forManifest(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("local"))));

		Reflections reflections1 = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forManifest(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))))
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("common"))));

		Stream<String> stream1=  reflections.getStore().get("ResourcesScanner").keys().stream();
		Stream<String> stream2=  reflections1.getStore().get("ResourcesScanner").keys().stream();

		Stream<String> resultingStream=Stream.of(stream1,stream2).flatMap(i -> i);
		//resultingStream.forEach(System.out::println);

		resultingStream
				.filter(f -> f.endsWith(".sql"))
				.forEach(n -> {

					if(!n.split("__")[0].toUpperCase().startsWith("V")){
						System.out.println("File starts with " + n + " Does not start with Letter V or v. Please fix it.");
						Assert.fail();
					}

					for(String s : n.split("__")[0].substring(1).split("\\.")){
						try {
							//System.out.println(n);
							Integer.valueOf(s);
						}catch(Exception e){
							//e.printStackTrace();
							System.out.println("File starting with "+ n + " does not match flyway standard");
							System.out.println("Flyway standard is V{version}__{description}.sql");
							Assert.fail();
						}
					}
				});
	}

}
