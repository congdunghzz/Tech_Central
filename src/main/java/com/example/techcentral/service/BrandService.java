package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.ExistException;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.dao.BrandRepository;
import com.example.techcentral.models.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAll(){
        return brandRepository.findAll();
    }

    public Brand getById (Long id){
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.isEmpty())
            throw new NotFoundException("Brand with id: "+id+" is not found");
        return brand.get();
    }
    public Brand getByName (String name){
        Optional<Brand> brand = brandRepository.findByName(name);
        if (brand.isEmpty())
            throw new NotFoundException("Brand with name: "+name+" is not found");
        return brand.get();
    }

    public Brand addBrand (Brand brand){
        if (brandRepository.existsByName(brand.getName()))
            throw new ExistException("Brand with name: " + brand.getName()+" have existed");
        return brandRepository.save(brand);
    }

    public Brand editName (Long id ,Brand brand){
        Optional<Brand> dbBrand = brandRepository.findById(id);
        if (dbBrand.isEmpty())
            throw new NotFoundException("Brand with id: "+id+" is not found");
        dbBrand.get().setName(brand.getName());
        return brandRepository.save(dbBrand.get());
    }

    public void deleteById(Long id){
        Optional<Brand> dbBrand = brandRepository.findById(id);
        if (dbBrand.isEmpty())
            throw new NotFoundException("Brand with id: "+id+" is not found");
        brandRepository.deleteById(id);
    }


}
