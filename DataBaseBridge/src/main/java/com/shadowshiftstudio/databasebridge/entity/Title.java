package com.shadowshiftstudio.databasebridge.entity;

import com.shadowshiftstudio.databasebridge.enums.AgeRating;
import com.shadowshiftstudio.databasebridge.enums.TitleStatus;
import com.shadowshiftstudio.databasebridge.enums.TitleType;
import jakarta.persistence.*;

import lombok.*;

import java.util.Set;

import static jakarta.persistence.EnumType.STRING;

public class Title {
    @Id
    @GeneratedValue
    private long id;

    //@Size(min = 3, message = "{validation.name.size.too_short}")
    //@Size(max = 100, message = "{validation.name.size.too_long}")
    private String name;

    //@Size(min = 3, message = "{validation.name.size.too_short}")
    //@Size(max = 100, message = "{validation.name.size.too_long}")
    @Column(name = "original_name")
    private String originalName;

    private int year;
    private String description;

    @Enumerated(STRING)
    private TitleStatus status;

    @Enumerated(STRING)
    private TitleType type;

    @Column(name = "background_image_url")
    private String backgroundUrl;

    @Column(name = "age_rating")
    @Enumerated(STRING)
    private AgeRating ageRating;


}
