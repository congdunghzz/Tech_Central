package com.example.techcentral.service;
import com.example.techcentral.dao.CategoryRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.dto.ProductDTO;
import com.example.techcentral.dto.mapper.ProductMapper;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import com.example.techcentral.models.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> getAllProduct(){

        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOs(products);
        }else {
            return null;
        }
    }

    public ProductDTO addProduct(ProductDTO productDTO) {

        Product product = Product.builder()
                .id(productDTO.id())
                .name(productDTO.name())
                .price(productDTO.price())
                .productDetail(productDTO.productDetail())
                .build();
        // if images are present, add them
        if (!productDTO.productImages().isEmpty()){
            product.setProductImages(productDTO.productImages());
        }

        Optional<Category> category = categoryRepository.findById(productDTO.category_id());

        // if category is present, add product into it. if it is not, break the function
        if (category.isPresent()){
            product.setCategory(category.get());
        }else {
            System.out.println("Product Service: Category is not be found");
            return null;
        }
        productRepository.save(product);

        return ProductMapper.TransferToProductDTO(product);
    }

    public boolean deleteProduct (Long id){
        try {
            productRepository.deleteById(id);
            return true;
        }catch (Exception e){return false;}
    }

    public ProductDTO findOneById(Long id){
        Optional<Product> product = productRepository.findById(id);
        return product.map(ProductMapper::TransferToProductDTO).orElse(null);
    }

    public ProductDTO editProduct(Long id,ProductDTO productDTO){
        ProductDTO result;
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()){
            System.out.println("Product Service: Product is not be found");
            return null;
        }

        //Update name, price.
        Product updatedProduct = product.get();
        updatedProduct.setName(productDTO.name());
        updatedProduct.setPrice(productDTO.price());
        // check category if it is present
        Optional<Category> category = categoryRepository.findById(productDTO.category_id());
        // if category is present, set it for product. if it is not, break the function
        if (category.isPresent()){
            if (!category.get().getId().equals(updatedProduct.getCategory().getId()))
                updatedProduct.setCategory(category.get());
        }else {
            System.out.println("Product Service: Category is not be found");
            return null;
        }

        if (!updatedProduct.getProductDetail().equals(productDTO.productDetail())){

            updatedProduct.getProductDetail().setRam(productDTO.productDetail().getRam());
            updatedProduct.getProductDetail().setCpu(productDTO.productDetail().getCpu());
            updatedProduct.getProductDetail().setColor(productDTO.productDetail().getColor());
            updatedProduct.getProductDetail().setRom(productDTO.productDetail().getRom());
            updatedProduct.getProductDetail().setMaterial(productDTO.productDetail().getMaterial());
            updatedProduct.getProductDetail().setResolution(productDTO.productDetail().getResolution());
            updatedProduct.getProductDetail().setScreen(productDTO.productDetail().getScreen());
        }
        result = ProductMapper.TransferToProductDTO(productRepository.save(updatedProduct));
        return result;
    }
}
