package com.example.techcentral;

import com.example.techcentral.dao.OrderRepository;
import com.example.techcentral.dao.ProductImageRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.dao.UserRepository;
import com.example.techcentral.enums.UserRole;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Order;
import com.example.techcentral.models.Product;
import com.example.techcentral.models.ProductDetail;
import com.example.techcentral.service.ProductImageService;
import com.example.techcentral.service.ProductService;
import com.example.techcentral.service.UserService;
import com.example.techcentral.service.imageService.ImageService;
import com.example.techcentral.service.imageService.ImageServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class TechcentralApplicationTests {
	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	UserRepository userRepository;

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

	@Test
	void orderByDate(){
		List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(1L);
		orders.forEach(System.out::println);
	}

	@Test
	void testGetId(){
		Optional<Long> id = userRepository.findIdByEmail("lebaoduy@gmail.com");
		System.out.println(id.get());
	}
	@Autowired
	UserService userService;

	@Autowired
	ProductImageRepository productImageRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;

	@Test
	void testStatuscode(){
		productImageRepository.deleteByUrl("https://storage.googleapis.com/techcenterimage.appspot.com/6bdc1bae-b9c7-40e2-bb30-f533d80c67382-800x451.jpgjpg");
	}
}
