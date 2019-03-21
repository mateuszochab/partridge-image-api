package com.ochabmateusz.partridge.partridgeimageapi;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PartridgeImageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartridgeImageApiApplication.class, args);

		OpenCV.loadShared();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

}

//TODO testy
//TODO improve text recognition from image
//TODO train my own classifier for upperbody, smile, eyes recognition
