
package net.mirrorloong.chineseweapons.procedures;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;
import java.util.Map;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.IntStream;
import java.net.URL;



public class ArmorcastingtableguisyntheticjudgmentProcedure {
    private static final String RECIPE_DIR = "/assets/chineseweapons/models/modrecipes/";
    private static final List<List<String>> recipeLists = new ArrayList<>(13);
    
    static {
        initializeLists();
    }
    
    public static void execute(Entity entity) {
        if (entity == null) return;
        if (recipeLists.get(1).isEmpty()) {
            processRecipeFiles();
        }
        checkAllSlotsSame(entity);
    }

	public static void processRecipeFiles() {
	    try {
	        URL resourceUrl = ArmorcastingtableguisyntheticjudgmentProcedure.class.getResource(RECIPE_DIR);
	        if(resourceUrl != null) {
	            Files.walk(Paths.get(resourceUrl.toURI()))
	                .filter(Files::isRegularFile)
	                .filter(path -> path.toString().endsWith(".json"))
	                .forEach(ArmorcastingtableguisyntheticjudgmentProcedure::processSingleFile);
	        }
	    } catch (Exception e) {
	        System.err.println("Error reading recipe directory: " + e.getMessage());
	    }
	}

    private static void initializeLists() {
        for (int i = 0; i <= 12; i++) {
            recipeLists.add(new ArrayList<>());
        }
    }
    
    private static void processSingleFile(Path filePath) {
        List<List<String>> tempLists = new ArrayList<>(13);
        IntStream.range(0, 13).forEach(i -> tempLists.add(new ArrayList<>()));
    
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject recipe = JsonParser.parseReader(reader).getAsJsonObject();
            
            IntStream.rangeClosed(1, 10)
                .mapToObj(String::valueOf)
                .filter(recipe::has)
                .forEach(key -> tempLists.get(Integer.parseInt(key))
                    .add(recipe.get(key).getAsString()));

            if (!recipe.has("output")) {
                throw new JsonParseException("Missing output field");
            }
            tempLists.get(11).add(recipe.get("output").getAsString());
            
            if (recipe.has("jeiBriefIntroduct")) {
                tempLists.get(12).add(recipe.get("jeiBriefIntroduct").getAsString());
            }

            IntStream.rangeClosed(1, 12).forEach(i ->
                recipeLists.get(i).addAll(tempLists.get(i)));
        } catch (Exception e) {
            System.err.println("Error processing file " + filePath + ": " + e.getMessage());
        }
    }

    public static boolean checkAllSlotsSame(Entity entity) {
        if (!(entity instanceof Player)) return false;
        Player player = (Player) entity;
        
        try {
            Object menu = ((Supplier<?>)player.containerMenu).get();
            Map<?, ?> slots = (Map<?, ?>) menu;
            
            List<String> currentItems = new ArrayList<>();
            for (int slotId = 1; slotId <= 10; slotId++) {
                Slot slot = (Slot) slots.get(slotId);
                currentItems.add(BuiltInRegistries.ITEM.getKey(slot.getItem().getItem()).toString());
            }

            for (int recipeIndex = 0; recipeIndex < recipeLists.get(1).size(); recipeIndex++) {
                boolean match = true;
                for (int slotId = 1; slotId <= 10; slotId++) {
                    if (!currentItems.get(slotId-1).equals(recipeLists.get(slotId).get(recipeIndex))) {
                        match = false;
                        break;
                    }
                }
                
                if (match) {
                    ResourceLocation output = ResourceLocation.tryParse(recipeLists.get(11).get(recipeIndex));
                    if (output != null) {
                        ItemStack outputStack = new ItemStack(BuiltInRegistries.ITEM.get(output));
                        ((Slot)slots.get(11)).set(outputStack);
                        player.containerMenu.broadcastChanges();
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Slot check error: " + e.getMessage());
        }
        return false;
    }
}
