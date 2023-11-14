package com.example.demo;

import com.google.cloud.spring.data.spanner.core.mapping.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table(name="YOGA_POSES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Yoga {

    @PrimaryKey(keyOrder=1)
    @Column(name="POSE_ID")
    private int poseId;

    @Column(name="NAME")
    private String name;

    @Column(name="BREATH")
    private String breath;

    @Column(name="DESCRIPTION")
    private String desc;
    
}
