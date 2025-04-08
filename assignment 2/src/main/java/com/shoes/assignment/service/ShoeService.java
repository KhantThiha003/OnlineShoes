package com.shoes.assignment.service;

import com.shoes.assignment.model.Shoe;
import com.shoes.assignment.repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

//    public Shoe saveShoe(Shoe shoe) throws IOException {
//    	System.out.println(shoe.getPrice()+" shoe price in service");
//       
//        return shoeRepository.save(shoe);
//    }
    
    public void saveShoe(Shoe shoe) {
    	shoeRepository.save(shoe);
    }
    
//    public Shoe saveShoe(Shoe shoe) throws IOException {
//        // Extract filename from the full path
//        String fullPath = shoe.getImage();
//        String filename = Paths.get(fullPath).getFileName().toString();
//        shoe.setImage(filename);
//        
//        return shoeRepository.save(shoe);
//    }

    public List<Shoe> getAllShoes() {
        return shoeRepository.findAll();
    }

    public Optional<Shoe> getShoeById(Long shoeId) {
        return shoeRepository.findById(shoeId);
    }

    public void deleteShoe(Long shoeId) {
        shoeRepository.deleteById(shoeId);
    }
    
    public List<Shoe> getShoesWithImages() {
        List<Shoe> shoes = shoeRepository.findAll();
        return shoes.stream()
                .peek(shoe -> shoe.setImage("/images/" + shoe.getImage()))
                .collect(Collectors.toList());
    }
}
