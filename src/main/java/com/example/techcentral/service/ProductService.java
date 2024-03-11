package com.example.techcentral.service;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.dao.CategoryRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.dto.product.ProductDTO;
import com.example.techcentral.dto.mapper.ProductMapper;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            throw new NotFoundException("Category with id: " +productDTO.category_id()+ " is not found");
        }
        try{
            productRepository.save(product);
        }catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("Some data is duplicated");
        }

        return ProductMapper.TransferToProductDTO(product);
    }

    public boolean deleteProduct (Long id){
        try {
            productRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new NotFoundException("Product with id: " +id+ " is not found");
        }
    }

    public ProductDTO findOneById(Long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) throw new NotFoundException("Product with id: " +id+ " is not found");
        return ProductMapper.TransferToProductDTO(product.get());
    }

    public ProductDTO editProduct(Long id,ProductDTO productDTO){
        ProductDTO result;
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()){
            throw new NotFoundException("Product with id: " +id+ " is not found");
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
            throw new NotFoundException("Category with id: " +productDTO.category_id()+ " is not found");
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
