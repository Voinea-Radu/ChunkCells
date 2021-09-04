package dev.lightdream.chunkcells.files.config;

import dev.lightdream.api.files.dto.*;
import dev.lightdream.chunkcells.files.dto.Position;
import dev.lightdream.chunkcells.files.dto.PositionRange;
import dev.lightdream.chunkcells.files.dto.UpgradePath;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class Config extends dev.lightdream.api.files.config.Config {

    public String cellWorld = "world";
    public PluginLocation cellCreateWaitingLocation = new PluginLocation("world", 0, 5, 0);
    public int cellPasteY = 100;
    public int offsetFromCenter = 100;
    public float tpOffsetX = 31.5f;
    public float tpOffsetY = 23;
    public float tpOffsetZ = 1;

    public GUIConfig upgradeGUI = new GUIConfig("upgrade_cell", "CHEST", "Upgrade GUI", 3, 9,
            new Item(XMaterial.GLASS_PANE, 1),
            new HashMap<String, GUIItem>() {{
                put("upgrade_cell", new GUIItem(new Item(XMaterial.STONE, 10, 1, "Upgrade Cell", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_farm", new GUIItem(new Item(XMaterial.STONE, 12, 1, "Upgrade Farm", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_furnace", new GUIItem(new Item(XMaterial.STONE, 13, 1, "Upgrade Furnace", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_mine", new GUIItem(new Item(XMaterial.STONE, 14, 1, "Upgrade Mine", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_crops", new GUIItem(new Item(XMaterial.STONE, 15, 1, "Upgrade Crops", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_blocks", new GUIItem(new Item(XMaterial.STONE, 16, 1, "Upgrade Blocks", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
                put("upgrade_wall", new GUIItem(new Item(XMaterial.STONE, 17, 1, "Upgrade Wall", Arrays.asList(
                        "Level: %current_level%/%max_level%",
                        "Price: %price%"
                ), new HashMap<>()), "{}"));
            }}
    );
    public HashMap<Integer, UpgradePath> upgrades = new HashMap<Integer, UpgradePath>() {{
        put(1, new UpgradePath(3, 3, 3, 2, 2, 4,
                1, 1, 1, 1, 1, 1, 1));
        put(2, new UpgradePath(2, 2, 2, 2, 1, 4,
                1, 1, 1, 1, 1, 1, 1));
        put(3, new UpgradePath(1, 2, 2, 1, 2, 4,
                1, 1, 1, 1, 1, 1, 1));
        put(4, new UpgradePath(1, 2, 1, 1, 3, 4,
                1, 1, 1, 1, 1, 1, 1));
        put(5, new UpgradePath(2, 2, 1, 3, 3, 4,
                1, 1, 1, 1, 1, 1, 1));
    }};

    public HashMap<Integer, HashMap<Integer, PositionRange>> mines = new HashMap<Integer, HashMap<Integer, PositionRange>>() {{
        put(1, new HashMap<Integer, PositionRange>() {{
            put(1, new PositionRange(
                    new Position(13, 22, 25),
                    new Position(22, 20, 34)
            ));
        }});
    }};

    public HashMap<XMaterial, Integer> cellMinesBlocks = new HashMap<XMaterial, Integer>() {{
        put(XMaterial.STONE, 10);
        put(XMaterial.IRON_ORE, 10);
        put(XMaterial.DIAMOND_ORE, 1);
    }};

    public int mineRefillInterval = 5 * 60;
    public int mineRefillChunks = 500;
    public int cellSizeX = 47;
    public int cellSizeY = 53;
    public int cellSizeZ = 49;

    public List<String> raidToolLore = Arrays.asList("You can raid with this");
    public int raidDurabilityUse = 50;
    public int raidDuration = 5 * 60;

    public HashMap<Integer, List<XMaterial>> wallMaterials = new HashMap<Integer, List<XMaterial>>() {{
        put(1, Arrays.asList(
                XMaterial.DIORITE,
                XMaterial.POLISHED_DIORITE
        ));
        put(2, Arrays.asList(
                XMaterial.COBBLESTONE,
                XMaterial.MOSSY_COBBLESTONE
        ));
        put(3, Arrays.asList(
                XMaterial.POLISHED_ANDESITE
        ));
        put(4, Arrays.asList(
                XMaterial.STONE_BRICKS,
                XMaterial.MOSSY_STONE_BRICKS
        ));
    }};

    public int cellRent = 10;

    public GUIConfig choseAxisGUI = new GUIConfig("chose_axis", "CHEST", "Chose Axis", 3, 9,
            new Item(XMaterial.GLASS_PANE, 1),
            new HashMap<String, GUIItem>() {{
                put("+x", new GUIItem(new Item(XMaterial.STONE, 0, 1, "+X", new ArrayList<>(), new HashMap<>()), "{functions:[\"open_gui\"], open_gui:\"rent_+x\"}"));
                put("+z", new GUIItem(new Item(XMaterial.STONE, 1, 1, "+Z", new ArrayList<>(), new HashMap<>()), "{functions:[\"open_gui\"], open_gui:\"rent_+z\"}"));
                put("-x", new GUIItem(new Item(XMaterial.STONE, 2, 1, "-X", new ArrayList<>(), new HashMap<>()), "{functions:[\"open_gui\"], open_gui:\"rent_-x\"}"));
                put("-z", new GUIItem(new Item(XMaterial.STONE, 3, 1, "-Z", new ArrayList<>(), new HashMap<>()), "{functions:[\"open_gui\"], open_gui:\"rent_-z\"}"));
                put("unrent", new GUIItem(new Item(XMaterial.RED_WOOL,4,1,"Unrent", new ArrayList<>()), "{functions:[unrent_cell], unrent_cell:\"\"}"));

            }});

    public GUIConfig rentGUI = new GUIConfig("rent", "CHEST","Rent a cell", 3, new Item(XMaterial.GLASS_PANE,1),
            new HashMap<String,GUIItem>(){{
                put("slot_1", new GUIItem(new Item(XMaterial.STONE, 0, 1,"Cell %number%", Arrays.asList(
                        "Rent: 10/day",
                        "Free: %free%"
                )),"{functions:[\"rent_cell\"], rent_cell:\"%axis%_%number%\"}"));
                put("slot_2", new GUIItem(new Item(XMaterial.STONE, 1, 1,"Cell %number%", Arrays.asList(
                        "Rent: 10/day",
                        "Free: %free%"
                )),"{functions:[\"rent_cell\"], rent_cell:\"%axis%_%number%\"}"));
                put("slot_3", new GUIItem(new Item(XMaterial.STONE, 2, 1,"Cell %number%", Arrays.asList(
                        "Rent: 10/day",
                        "Free: %free%"
                )),"{functions:[\"rent_cell\"], rent_cell:\"%axis%_%number%\"}"));
                put("slot_4", new GUIItem(new Item(XMaterial.STONE, 3, 1,"Cell %number%", Arrays.asList(
                        "Rent: 10/day",
                        "Free: %free%"
                )), "{functions:[\"rent_cell\"], rent_cell:\"%axis%_%number%\"}"));
                put("main_menu", new GUIItem(new Item(XMaterial.BARRIER, 4,1,"Main Menu", new ArrayList<>(), new HashMap<>()), "{functions:[open_gui], open_gui:\"chose_axis\"}"));
                put("back", new GUIItem(new Item(XMaterial.ARROW,5,1,"back", new ArrayList<>()), "{functions:[back_page], back_page:\"\"}"));
                put("next", new GUIItem(new Item(XMaterial.ARROW,6,1,"next", new ArrayList<>()), "{functions:[next_page], next_page:\"\"}"));
                put("generate", new GUIItem(new Item(XMaterial.DIRT,7,1,"Generate New", new ArrayList<>()), "{functions:[generate_cell, rent_cell], generate_cell:\"%axis%\", rent_cell:\"%axis%_%last_cell%\"}"));
    }});

    public int cellGeneratePrice = 10;

    public int numberOfCellsPerSchematic = 1;
    public List<String> extraCellGenerateCommands = new ArrayList<>();

}
