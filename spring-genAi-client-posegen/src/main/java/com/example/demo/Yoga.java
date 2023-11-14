package com.example.demo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Yoga {
  private int poseId;
  private String name;
  private String breath;
  private String desc;
  private List<String> pose;
    
}
