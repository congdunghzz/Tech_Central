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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final BrandRepository brandRepository;
    private final BrandService brandService;
    @Autowired
    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository, CategoryRepository categoryRepository, ProductImageService productImageService, BrandRepository brandRepository, BrandService brandService) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.productImageService = productImageService;
        this.brandRepository = brandRepository;
        this.brandService = brandService;
    }

    public Page<ProductDTO> getAllProduct(int page, int size){

        Page<Product> products = productRepository.findAll(PageRequest.of(page-1, size));
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }

    public Page<ProductDTO> getProductByCategory(String name, int page, int size){
        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1, size);
        if (name.isBlank()) {
            products = productRepository.findAll(pageable);
        } else{
            Optional<Category> category = categoryRepository.findByName(name);
            if (category.isEmpty()) throw new NotFoundException("Category with name:"  +name+  "is not found");

            products = productRepository.findAllByCategory(category.get(), pageable);
        }
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }
    public Page<ProductDTO> getProductByBrand(String name, int page, int size){
        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1, size);
        if (name.isBlank()) {
            products = productRepository.findAll(pageable);
        } else{
            Optional<Brand> brand = brandRepository.findByName(name);
            if (brand.isEmpty()) throw new NotFoundException("Brand with name:"  +name+  "is not found");
            products = productRepository.findAllByBrand(brand.get(), pageable);
        }

        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
        }else {
            return null;
        }
    }


    public Page<ProductDTO> searchForName (String name, int page, int size){

        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1,size);
        if (name.isBlank()){
            products = productRepository.findAll(pageable);
        }else {
            products = productRepository.findAllByNameStartingWithIgnoreCase(name, pageable);
        }
        return ProductMapper.TransferToProductDTOPage(products);
    }

    public List<ProductDTO> getLatestProducts (int size){

        Page<Product> products;
        Pageable pageable = PageRequest.of(0,size);
        products = productRepository.findAllByOrderByIdDesc(pageable);
        return ProductMapper.TransferToProductDTOs(products.getContent());
    }

    public Page<ProductDTO> getProductByCategoryAndBrand(String category, String brand, int page, int size){
        Page<Product> products;
        Pageable pageable = PageRequest.of(page-1, size);
        boolean hasCategory = !category.isBlank();
        boolean hasBrand = !brand.isBlank();
        System.out.println("Product Service: get product by cate and brand: " + hasCategory + " " + hasBrand);

        if(!hasCategory && !hasBrand){
            products = productRepository.findAll(pageable);
        }else{
            // has category
            if(hasCategory){
                Optional<Category> dbCategory = categoryRepository.findByName(category);
                if (category.isEmpty()) throw new NotFoundException("Category with name:"  +category+  "is not found");
                // brand is not present
                if(!hasBrand) {
                    products = productRepository.findAllByCategory(dbCategory.get(),pageable);
                }
                //has brand as well
                else{
                    Optional<Brand> dbBrand = brandRepository.findByName(brand);
                    if (brand.isEmpty()) throw new NotFoundException("Brand with name:"  +brand+  "is not found");
                    products = productRepository.findAllByCategoryAndBrand(dbCategory.get(), dbBrand.get(),pageable);
                }
            }
            // has brand
            else{
                Optional<Brand> dbBrand = brandRepository.findByName(brand);
                if (brand.isEmpty()) throw new NotFoundException("Brand with name:"  +brand+  "is not found");
                products = productRepository.findAllByBrand(dbBrand.get(), pageable);
            }
        }
        if (!products.isEmpty()) {
            return ProductMapper.TransferToProductDTOPage(products);
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
            Brand newBrand = Brand.builder().name(request.brand()).build();
            product.setBrand(newBrand);
        }
        Product createdProduct = productRepository.save(product);

        // if images are present, add them
        if (!request.images().isEmpty()){
            return addImages(createdProduct.getId(), request.images());
        }
        return ProductMapper.TransferToProductDTO(createdProduct);
    }

    public boolean deleteProduct (Long id){
        Optional<Product> product =productRepository.findById(id);
        if (product.isEmpty())
            throw new NotFoundException("Product with id: " +id+ " is not found");

        if(product.get().getProductImages() != null) {
            product.get().getProductImages().forEach(productImage -> {
                try {
                    productImageService.deleteImg(productImage.getUrl());
                } catch (IOException e) {
                    throw new NotFoundException("Image cant not be removed");
                }
            });
        }
        try{
            productRepository.deleteById(id);
            return true;
        }catch (Exception e ){
            throw new DataIntegrityViolationException("This data have some relation with other object");
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
            if (!category.get().getName().equals(updatedProduct.getCategory().getName()))
                updatedProduct.setCategory(category.get());
        }else {
            throw new NotFoundException("Category with name: " +productDTO.category()+ " is not found");
        }
        Optional<Brand> brand = brandRepository.findByName(productDTO.brand());
        // if category is present, set it for product. if it is not, break the function
        if (brand.isPresent()){
            if (!brand.get().getName().equals(updatedProduct.getCategory().getName()))
                updatedProduct.setCategory(category.get());
        }else {
            throw new NotFoundException("Brand with name: " +productDTO.brand()+ " is not found");
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
            if (product.get().getProductImages() == null){
                product.get().setProductImages(new ArrayList<>());
            }
            product.get()
                    .getProductImages()
                    .addAll(productImages);
            return ProductMapper.TransferToProductDTO(productRepository.save(product.get()));
        } catch (IOException e) {
            throw new NotFoundException("Image file was not saved, something went wrong !");
        }
    }


    ///         NOT WORKING

    public ProductDTO deleteImages (Long productId, List<ProductImage> imageList){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()){
            throw new NotFoundException("Product with id: " +productId+ " is not found");
        }
        List <ProductImage> productImages = product.get().getProductImages();
        if (imageList != null) {
            for (ProductImage img : imageList) {
                try {
                    productImageService.deleteImg(img.getUrl());
                    productImages.remove(img);
                } catch (IOException e) {
                    throw new NotFoundException("Image file was not removed, something went wrong !");
                }
            }
            product.get().setProductImages(productImages);
        }
        return ProductMapper.TransferToProductDTO(productRepository.save(product.get()));
    }

    ///         NOT WORKING

}
