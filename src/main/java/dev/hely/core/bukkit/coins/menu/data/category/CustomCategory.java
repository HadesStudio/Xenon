package dev.hely.core.bukkit.coins.menu.data.category;

import dev.hely.core.bukkit.coins.menu.data.button.CustomButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 13/03/2022
 */
@Getter
@Setter
@AllArgsConstructor
public class CustomCategory {

    private String title;
    private String name;
    private List<CustomButton> customButtons;
    private int rows;

    public boolean fill_enabled;
    public int fill_data;
    public String fill_style;
    public List<String> fill_corners;
    public boolean fill_glow;


    public boolean back_enabled;
    public String back_material;
    public int back_data;
    public String back_name;
    public List<String> back_lore;
    public int back_slot;
}
