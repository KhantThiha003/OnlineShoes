package com.shoes.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.shoes.assignment.model.Shoe;
import com.shoes.assignment.service.ShoeService;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class AssignmentApplicationTests {
	
	@Autowired
	private ShoeService shoeservice;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void saveShoe() {
		Shoe shoe = new Shoe();
		long id = 3;
		shoe.setShoeId(id);
		shoe.setBrand("aaaa");
		shoe.setName("Running");
		shoe.setPrice(8.8843);
		shoe.setSize("32");
		shoe.setImage("photo.png");
		
		
	}

}
