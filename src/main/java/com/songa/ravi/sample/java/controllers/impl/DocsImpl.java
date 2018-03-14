package com.songa.ravi.sample.java.controllers.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.songa.ravi.sample.java.biz.DocsManager;
import com.songa.ravi.sample.java.controllers.IDocs;

@RestController
@RequestMapping("/docs")
public class DocsImpl implements IDocs {

	@Autowired
	private DocsManager docManager;

	@GetMapping("/ping")
	public ResponseEntity<String> pingpong() {
		return ResponseEntity.ok().body("pong");
	}

	@GetMapping(value = "/png")
	public ResponseEntity<byte[]> getSamplePng() {
		// return Base64.getEncoder().encode(docManager.getDoc());
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(docManager.getPng(), headers, HttpStatus.OK);

	}

	@PostMapping(value = "/inputstream")
	public ResponseEntity<String> inputStreamSample(InputStream file) {
		// return Base64.getEncoder().encode(docManager.getDoc());

		docManager.postPng(file, "new.png");
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>("File Uploaded", headers, HttpStatus.OK);

	}

	@PostMapping(value = "/multipart/file")
	public ResponseEntity<String> multipartSingleFile(@RequestParam("file") MultipartFile file) throws IOException {
		// return Base64.getEncoder().encode(docManager.getDoc());

		System.out.println(file.getOriginalFilename());
		// FileMultipartData part = new FileMultipartData();
		docManager.postPng(file.getInputStream(), file.getOriginalFilename());
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>("File Uploaded", headers, HttpStatus.OK);

	}

	@PostMapping(value = "/multipart/files")
	public ResponseEntity<String> multipartFiles(@RequestParam("file") List<MultipartFile> files) throws IOException {

		files.stream().forEach(file -> {
			try {
				docManager.postPng(file.getInputStream(), file.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		 final HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.TEXT_PLAIN);
		 return new ResponseEntity<String>("File Uploaded" , headers, HttpStatus.OK);

	}

}
