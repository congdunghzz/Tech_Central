package com.example.techcentral;

import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import com.example.techcentral.models.ProductDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class TechcentralApplicationTests {
	@Autowired
	ProductRepository productRepository;
	@Test
	void contextLoads() {
	}

	@Test
	void testDAO(){

		/*Category category = Category.builder()
				.name("Lap top")
				.build();
		ProductDetail detail = ProductDetail.builder()
				.cpu("Mac")
				.ram(8)
				.rom(256)
				.color("Pink")
				.material("Aluminum")
				.screen(15.6)
				.build();
		Product product = Product.builder()
				.name("iphone")
				.price(2500)
				.category(category)
				.productDetail(detail)
				.build();
		productRepository.save(product);*/
		List<Product> result = productRepository.findAll();
		System.out.println(result);
		/*result.stream().forEach(System.out ::println);*/


	}
}
