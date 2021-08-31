package dev.lightdream.chunkcells.files.config;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class Saves {

    public HashMap<String, Integer> axis = new HashMap<String, Integer>(){{
       put("+X", 0);
       put("+Z", 0);
       put("-X", 0);
       put("-Z", 0);
    }};
}
