package com.example.techcentral.service;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.dao.BrandRepository;
import com.example.techcentral.dao.CategoryRepository;
import com.example.techcentral.dao.ProductImageRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.dto.product.ProductDTO;
import com.example.techcentral.dto.mapper.ProductMapper;
import com.example.techcentral.dto.product.ProductRequest;
import com.example.techcentral.models.Brand;
import com.example.techcentral.models.Category;
import com.example.techcentral.models.Product;
import com.example.techcentral.models.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final BrandRepository brandRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository, ProductImageService productImageService, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.productImageService = productImageService;
        this.brandRepository = brandRepository;
    }

    public List<ProductDTO> getAllProduct(){

        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOs(products);
        }else {
            return null;
        }
    }

    public ProductDTO addProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .productDetail(request.productDetail())
                .build();

        Optional<Category> category = categoryRepository.findByName(request.category());
        Optional<Brand> brand = brandRepository.findByName(request.brand());
        // if category is present, add product into it. if it is not, break the function
        if (category.isPresent()){
            product.setCategory(category.get());
        }else {
            throw new NotFoundException("Category with name: " +request.category()+ " is not found");
        }

        // if brand is present, add product into it. if it is not, break the function
        if (brand.isPresent()){
            product.setBrand(brand.get());
        }else {
            throw new NotFoundException("Brand with name: " +request.brand()+ " is not found");
        }

        // if images are present, add them
        if (!request.images().isEmpty()){
            try {
                product.setProductImages(productImageService.addImage(request.images()));
            } catch (IOException e) {
                throw new NotFoundException("Image file was not saved, something went wrong !");
            }
        }

           Product result = productRepository.save(product);


        return ProductMapper.TransferToProductDTO(result);
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
        Optional<Category> category = categoryRepository.findByName(productDTO.category());
        // if category is present, set it for product. if it is not, break the function
        if (category.isPresent()){
            if (!category.get().getId().equals(updatedProduct.getCategory().getId()))
                updatedProduct.setCategory(category.get());
        }else {
            throw new NotFoundException("Category with name: " +productDTO.category()+ " is not found");
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

    public ProductDTO addImages (Long productId, List<MultipartFile> imgFiles){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new NotFoundException("Product with id: " +productId+ " is not found");
        }

        try {
            List<ProductImage> productImages = productImageService.addImage(imgFiles);
            productImages.forEach(item -> item.setProduct(product.get()));
            product.get()
                    .getProductImages()
                    .addAll(productImages);
            return ProductMapper.TransferToProductDTO(productRepository.save(product.get()));
        } catch (IOException e) {
            throw new NotFoundException("Image file was not saved, something went wrong !");
        }
    }
    public ProductDTO deleteImages (Long productId, List<ProductImage> imageList){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new NotFoundException("Product with id: " +productId+ " is not found");
        }

        for (ProductImage img : imageList){
            try {
                productImageService.deleteImg(img.getUrl());
                productImageRepository.deleteByUrl(img.getUrl());
                product.get().getProductImages().remove(img);
            } catch (IOException e) {
                throw new NotFoundException("Image file was not saved, something went wrong !");
            }
        }
        return ProductMapper.TransferToProductDTO(productRepository.save(product.get()));
    }
}
