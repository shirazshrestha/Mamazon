package com.project.onlinestore.buyer.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Line> lines;

    public void addLine(Line line){
        if (lines==null)
            lines = new ArrayList<>();
        lines.add(line);
    }

    public Double getTotal(){
        Double tot = 0.0;
        for(Line line:lines)
            tot+=line.getPrice();
        return tot;
    }
}
