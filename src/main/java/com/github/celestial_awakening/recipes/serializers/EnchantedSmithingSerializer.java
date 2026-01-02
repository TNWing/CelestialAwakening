package com.github.celestial_awakening.recipes.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.HashMap;

public interface EnchantedSmithingSerializer {

    default HashMap<Enchantment,Integer> checkEnchants(JsonObject json){
        HashMap<Enchantment,Integer> enchantMap=new HashMap<>();
        if (json.has("enchantments")){
            JsonArray arr=json.getAsJsonArray("enchantments");
            for (JsonElement ele:arr){
                JsonObject obj=ele.getAsJsonObject();
                Enchantment enchantment= BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(obj.get("enchantment").getAsString()));
                Integer lvl=obj.get("level").getAsInt();
                if (enchantment==null){
                    throw new JsonSyntaxException("Invalid enchantment");
                }
                enchantMap.put(enchantment,lvl);
            }
        }
        return enchantMap;
    }

    default HashMap<Enchantment,Integer> bufReader(FriendlyByteBuf buf){
        int size=buf.readVarInt();
        HashMap<Enchantment,Integer> map=new HashMap();
        for (int i=0;i<size;i++){
            ResourceLocation eLoc= buf.readResourceLocation();
            map.put(BuiltInRegistries.ENCHANTMENT.get(eLoc), buf.readVarInt());
        }
        return map;
    }

    default void bufWriter(){

    }
}
