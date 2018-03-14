package com.songa.ravi.sample.java.controllers.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.ResourceResponse;
import com.songa.ravi.sample.java.controllers.IControllers;
import com.songa.ravi.sample.java.models.Student;

@RestController
@RequestMapping("/v1/api/")
public class ControllersImpl implements IControllers {

	private final String databaseId = "sampleDB";
	private final String collectionId = "sample";
	private final String host = "https://sample-comosdb.documents.azure.com:443/";
	private final String masterKey = "PyIfXxkfpKvNImz9jRc7TOOlFOZS0LpW6uWy7Hs8PkDny4O5Kq8t2dGnTGaAiHQDZuEOwtNsTMSjwVibKeUoIg==";

	private DocumentClient client;

	@GetMapping("/ping")
	public String pingpong() {
		return "pong";
	}

	@GetMapping("/insert/bulk/{id}")
	public String bulkInsert(@PathVariable("id") Integer id) {

		List<Student> students = new ArrayList<Student>();

		for (Integer i = id; i < 5 + id; i++) {
			Student temp = new Student();
			temp.setStudentId(i.toString());
			temp.setId(i.toString());
			students.add(temp);
		}

		Gson gson = new Gson();
		String json = gson.toJson(students);
		Object[] storedProcedureArgs = new Object[] { json };

		return "Records successfully inserted : " + bulkInsert(storedProcedureArgs);
	}
	
	@GetMapping("/insert/single/{id}")
	public String singleInsert(@PathVariable("id") Integer id) {

		client = new DocumentClient(host, masterKey, null, null);
		String collectionLink = String.format("/dbs/%s/colls/%s", databaseId, collectionId);
		

		for (Integer i = id; i < 5 + id; i++) {
			Student temp = new Student();
			temp.setStudentId(i.toString());
			temp.setId(i.toString());
			try {
				ResourceResponse<Document> response = client.createDocument(collectionLink, temp, null, false);
			} catch (DocumentClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "Records successfully inserted";
	}

	private String bulkInsert(Object[] documents) {
		client = new DocumentClient(host, masterKey, null, null);
		String storedProcLink = String.format("/dbs/%s/colls/%s/sprocs/%s", databaseId, collectionId, "bulkCreate");

		String result = null;
		try {
			result = client.executeStoredProcedure(storedProcLink, null, documents).getResponseAsString();
		} catch (DocumentClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
