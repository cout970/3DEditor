package com.cout970.editor.export;

import com.cout970.editor.model.TechneCube;
import com.cout970.editor.tools.Project;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by cout970 on 05/04/2016.
 */
public class ObjExportHandler implements IExportHandler {

    private List<Vect3d> vertex = new LinkedList<>();
    private Set<Vect3d> vertexMap = new LinkedHashSet<>();

    private List<Vect2d> texCoords = new LinkedList<>();
    private Set<Vect2d> texCoordsMap = new LinkedHashSet<>();

    private List<Vect3d> normals = new LinkedList<>();
    private Set<Vect3d> normalsMap = new LinkedHashSet<>();

    @Override
    public boolean export(Project project, File output) {

        vertex.clear();
        vertexMap.clear();
        texCoords.clear();
        texCoordsMap.clear();
        normals.clear();
        normalsMap.clear();

        try {
            FileWriter writer = new FileWriter(output);

            //inicio de la conversion del modelo
            List<ObjQuad> objQuads = new LinkedList<>();
            project.getModelTree().getAllModels().stream().filter(model -> model instanceof TechneCube).forEach(model -> {
                for (TechneCube.Quad q : ((TechneCube) model).getQuads()) {
                    ObjQuad quad = new ObjQuad();

                    for (TechneCube.QuadVertex v : TechneCube.QuadVertex.values()) {

                        Vect3d vert = q.getVertex(v);
                        if (vertexMap.contains(vert)) {
                            quad.getVertexIndices()[v.ordinal()] = vertex.indexOf(vert) + 1;
                        } else {
                            quad.getVertexIndices()[v.ordinal()] = vertex.size() + 1;
                            vertex.add(vert);
                            vertexMap.add(vert);
                        }

                        Vect2d tex = q.getUV(v);
                        if (texCoordsMap.contains(tex)) {
                            quad.getTextureIndices()[v.ordinal()] = texCoords.indexOf(tex) + 1;
                        } else {
                            quad.getTextureIndices()[v.ordinal()] = texCoords.size() + 1;
                            texCoords.add(tex);
                            texCoordsMap.add(tex);
                        }

                        Vect3d norm = q.getNormal().toVect3i().toVect3d();
                        if (normalsMap.contains(norm)) {
                            quad.getNormalIndices()[v.ordinal()] = normals.indexOf(norm) + 1;
                        } else {
                            quad.getNormalIndices()[v.ordinal()] = normals.size() + 1;
                            normals.add(norm);
                            normalsMap.add(norm);
                        }
                    }
                    objQuads.add(quad);
                }
            });
            //fin de la conversion del modelo

            DecimalFormatSymbols sym = new DecimalFormatSymbols();
            sym.setDecimalSeparator('.');
            DecimalFormat format = new DecimalFormat("####0.000000", sym);

            writer.write("\no model\n");
            for (Vect3d a : vertex) {
                writer.write(String.format("v %s %s %s\n", format.format(a.getX()), format.format(a.getY()), format.format(a.getZ())));
            }
            writer.write('\n');
            for (Vect2d a : texCoords) {
                writer.write(String.format("vt %s %s\n", format.format(a.getX()), format.format(a.getY())));
            }
            writer.write('\n');
            for (Vect3d a : normals) {
                writer.write(String.format("vn %s %s %s\n", format.format(a.getX()), format.format(a.getY()), format.format(a.getZ())));
            }
            writer.write('\n');
            for (ObjQuad q : objQuads){
                int[] a = q.getVertexIndices();
                int[] b = q.getTextureIndices();
                int[] c = q.getNormalIndices();
                writer.write(String.format("f %d/%d/%d %d/%d/%d %d/%d/%d %d/%d/%d\n",
                        a[0], b[0], c[0], a[1], b[1], c[1],
                        a[2], b[2], c[2], a[3], b[3], c[3]));
            }
            writer.flush();

            vertex.clear();
            vertexMap.clear();
            texCoords.clear();
            texCoordsMap.clear();
            normals.clear();
            normalsMap.clear();

            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    private class ObjQuad {
        int[] vertexIndices;
        int[] textureIndices;
        int[] normalIndices;

        public ObjQuad() {
            this.vertexIndices = new int[4];
            this.textureIndices = new int[4];
            this.normalIndices = new int[4];
        }

        public int[] getVertexIndices() {
            return vertexIndices;
        }

        public int[] getTextureIndices() {
            return textureIndices;
        }

        public int[] getNormalIndices() {
            return normalIndices;
        }
    }
}
