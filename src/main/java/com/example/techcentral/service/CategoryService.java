package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.ExistException;
import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.dao.CategoryRepository;
import com.example.techcentral.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public Category getById (Long id){
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty())
            throw new NotFoundException("Category with id: "+id+" is not found");
        return category.get();
    }
    public Category getByName (String name){
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isEmpty())
            throw new NotFoundException("Category with name: "+name+" is not found");
        return category.get();
    }

    public Category addCategory (Category category){
        if (categoryRepository.existsByName(category.getName()))
            throw new ExistException("Category with name: " + category.getName()+" have existed");
        return categoryRepository.save(category);
    }

    public Category editName (Long id ,Category category){
        Optional<Category> dbCategory = categoryRepository.findById(id);
        if (dbCategory.isEmpty())
            throw new NotFoundException("Category with name: "+id+" is not found");
        dbCategory.get().setName(category.getName());
        return categoryRepository.save(dbCategory.get());
    }

    public void deleteById(Long id){
        Optional<Category> dbCategory = categoryRepository.findById(id);
        if (dbCategory.isEmpty())
            throw new NotFoundException("Category with name: "+id+" is not found");
        categoryRepository.deleteById(id);
    }

}
