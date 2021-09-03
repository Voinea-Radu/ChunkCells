package dev.lightdream.chunkcells.files.config;

import dev.lightdream.chunkcells.files.dto.Cell;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class Saves {

    public HashMap<String, Integer> axis = new HashMap<String, Integer>(){{
       put("+X", 0);
       put("+Z", 0);
       put("-X", 0);
       put("-Z", 0);
    }};

    //public List<Cell> unrentedCells = new ArrayList<>();
}
