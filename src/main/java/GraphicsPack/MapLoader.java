package GraphicsPack;

import Tiles.Tile;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapLoader {

    public static MapData loadMap(String path) {
        Gson gson = new Gson();

        try (FileReader fileReader = new FileReader("data/map/map.json")) {
            return gson.fromJson(fileReader, MapData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
