package com.goldenboiz.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Executive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    private String position; // President, Secretary, etc
    private String bio;
    private String imageUrl; // /uploads/execs/filename.jpg
    private Integer sortOrder; // to arrange them 1,2,3...
}