package com.cout970.editor.export;

import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.tools.Project;
import com.cout970.editor.util.Direction;
import com.cout970.editor.util.Vect3d;
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cout970 on 06/04/2016.
 */
public class JsonExportHandler implements IExportHandler {


    @Override
    public boolean export(Project project, File output) {
        try {
            FileWriter writer = new FileWriter(output);

            JsonObject root = new JsonObject();
            root.addProperty("ambientocclusion", false);

            JsonObject textures = new JsonObject();
            textures.addProperty("tex", TextureStorage.MODEL_TEXTURE.getTextureName());
            textures.addProperty("particle", "#tex");
            root.add("textures", textures);

            JsonArray elements = new JsonArray();

            project.getModelTree().getAllModels().stream().filter(model -> model instanceof TechneCube).forEach(model -> {

                TechneCube cube = (TechneCube) model;
                JsonObject part = new JsonObject();

                //from
                JsonArray vec1 = new JsonArray();
                Vect3d from = cube.getPos().multiply(16);
                vec1.add(new JsonPrimitive(from.getX()));
                vec1.add(new JsonPrimitive(from.getY()));
                vec1.add(new JsonPrimitive(from.getZ()));
                part.add("from", vec1);

                //to
                JsonArray vec2 = new JsonArray();
                Vect3d to = cube.getPos().multiply(16).add(cube.getSize());
                vec2.add(new JsonPrimitive(to.getX()));
                vec2.add(new JsonPrimitive(to.getY()));
                vec2.add(new JsonPrimitive(to.getZ()));
                part.add("to", vec2);

                //shade
                part.addProperty("shade", false);

                //faces
                JsonObject faces = new JsonObject();
                for (Direction dir : Direction.values()) {
                    JsonObject direction = new JsonObject();

                    //uv
                    JsonArray vec3 = new JsonArray();
                    TechneCube.TextureUV uv = cube.getUV(dir);
                    vec3.add(new JsonPrimitive(uv.getStart().getX() * 16));
                    vec3.add(new JsonPrimitive(uv.getStart().getY() * 16));
                    vec3.add(new JsonPrimitive(uv.getEnd().getX() * 16));
                    vec3.add(new JsonPrimitive(uv.getEnd().getY() * 16));
                    direction.add("uv", vec3);

                    //texture
                    direction.addProperty("texture", "#tex");

                    //cullface
                    direction.addProperty("cullface", dir.name().toLowerCase());

                    faces.add(dir.name().toLowerCase(), direction);
                }
                //rotation
//                JsonObject rotation = new JsonObject();
//                //origin
//                JsonArray vec4 = new JsonArray();
//                Vect3d origin = cube.getRotationPoint().multiply(16);
//                vec4.add(new JsonPrimitive(origin.getX()));
//                vec4.add(new JsonPrimitive(origin.getY()));
//                vec4.add(new JsonPrimitive(origin.getZ()));
//                rotation.add("origin", vec4);
//
//                //
//                JsonArray vec4 = new JsonArray();
//                Vect3d origin = cube.getRotationPoint().multiply(16);
//                vec4.add(new JsonPrimitive(origin.getX()));
//                vec4.add(new JsonPrimitive(origin.getY()));
//                vec4.add(new JsonPrimitive(origin.getZ()));
//                rotation.add("origin", vec4);

                part.add("faces", faces);
                elements.add(part);
            });
            root.add("elements", elements);

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            writer.write(gson.toJson(root));
            writer.close();

            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
