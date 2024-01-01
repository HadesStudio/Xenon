package net.ascend.core.bukkit.tag.menu.data.category;

import net.ascend.core.bukkit.tag.menu.data.button.CustomButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    public boolean reset_tag_enabled;
    public String reset_tag_material;
    public int reset_tag_data;
    public String reset_tag_name;
    public List<String> reset_tag_lore;
    public int reset_tag_slot;
}
