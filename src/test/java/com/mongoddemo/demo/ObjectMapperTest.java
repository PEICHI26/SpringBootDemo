package com.mongoddemo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class ObjectMapperTest {
	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testSerializeBookToJSON() throws Exception {
		Book book = new Book();
		book.setId("B0001");
		book.setName("Computer Science");
		book.setPrice(350);
		book.setIsbn("978-986-123-456-7");
		book.setCreatedTime(new Date());
		String bookString = objectMapper.writeValueAsString(book);
		//json format
		JSONObject bookJSON = new JSONObject(bookString);
		Assert.assertEquals(book.getId(), bookJSON.getString("id"));
	}

	@Test
	public void testDeserializeJSONToPublisher() throws Exception {
		JSONObject publisherJSON = new JSONObject();
		publisherJSON
			.put("companyName", "Taipei Company")
			.put("address", "Taipei")
			.put("tel", "02-1234-5678");
		Publisher publisher = objectMapper.readValue(publisherJSON.toString(), Publisher.class);
		Assert.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
	}

	private static class Book {
		private String id;
		private String name;
		private int price;
		private String isbn;
		private Date createdTime;
		private Publisher publisher;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public String getIsbn() {
			return isbn;
		}

		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}

		public Date getCreatedTime() {
			return createdTime;
		}

		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}

		public Publisher getPublisher() {
			return publisher;
		}

		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}
	}

	private static class Publisher {
		private String companyName;
		private String address;
		private String tel;

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}
	}
}
