package com.cout970.editor.export;

import com.cout970.editor.ModelTree;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Log;
import com.cout970.editor.util.Vect2i;
import com.cout970.editor.util.Vect3d;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * Created by cout970 on 01/03/2016.
 */
public class TechneSaveHandler implements ISaveHandler {

    public static final List<String> cubeTypes = Arrays.asList(
            "d9e621f7-957f-4b77-b1ae-20dcd0da7751",
            "de81aa14-bd60-4228-8d8d-5238bcd3caaa"
    );

    @Override
    public void save(File file, ModelTree models) {
        try {
            save0(file, new LinkedList<>());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ModelTree load(File file) {
        try {
            List<TechneCube> parts = load0(file);
            ModelTree m = new ModelTree(parts);
            m.init();
            return m;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void save0(File file, List<TechneCube> pars) throws FileNotFoundException {
//        if (!file.exists())
//            file.mkdirs();
//        OutputStream stream = new FileOutputStream(file);
//        ZipOutputStream zipOutput = new ZipOutputStream(stream);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //root
            Element rootNode = document.createElement("Techne");
            document.appendChild(rootNode);

            //version
            Attr version = document.createAttribute("Version");
            version.setValue("2.2");//current techne version
            rootNode.setAttributeNode(version);

            //autor
            Element autorNode = document.createElement("Autor");
            autorNode.setTextContent("cout970");
            rootNode.appendChild(autorNode);


//            Element nodeListModel = document.createElement("Model");
//            document.appendChild(nodeListModel);

            Log.debug(document.toString());

//            ZipEntry entry = new ZipEntry("model.xml");
//            zipOutput.putNextEntry(entry);
//            XMLOutputFactory.newFactory().createXMLEventWriter(zipOutput).flush();
//            zipOutput.closeEntry();

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result =
                    new StreamResult(new File("./test0.xml"));
            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);

        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private List<TechneCube> load0(File file) throws FileNotFoundException {
        if (!file.exists() || !file.getName().contains(".tcn")) {
            throw new FileNotFoundException("Error with file: " + file);
        }
        List<TechneCube> parts = new LinkedList<>();
        InputStream stream = new FileInputStream(file);
        try {
            ZipInputStream zipInput = new ZipInputStream(stream);
            Map<String, byte[]> zipContents = new HashMap<>();
            ZipEntry entry;

            while ((entry = zipInput.getNextEntry()) != null) {

                byte[] data = new byte[(int) entry.getSize()];
                int i = 0;
                while (zipInput.available() > 0 && i < data.length) {
                    data[i++] = (byte) zipInput.read();
                }
                zipContents.put(entry.getName(), data);
            }

            byte[] modelXml = zipContents.get("model.xml");
            if (modelXml == null) {
                throw new ModelFormatException("Model " + file + " contains no model.xml file");
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(modelXml));

            NodeList nodeListTechne = document.getElementsByTagName("Techne");
            if (nodeListTechne.getLength() < 1) {
                throw new ModelFormatException("Model " + file + " contains no Techne tag");
            }

            NodeList nodeListModel = document.getElementsByTagName("Model");
            if (nodeListModel.getLength() < 1) {
                throw new ModelFormatException("Model " + file + " contains no Model tag");
            }

            NamedNodeMap modelAttributes = nodeListModel.item(0).getAttributes();
            if (modelAttributes == null) {
                throw new ModelFormatException("Model " + file + " contains a Model tag with no attributes");
            }

            Node modelTexture = modelAttributes.getNamedItem("texture");
            String texture = null;
            if (modelTexture != null) {
                texture = modelTexture.getTextContent();
            }

            NodeList textureDim = document.getElementsByTagName("TextureSize");
            Dimension textureDims = null;
            if (textureDim.getLength() > 0) {
                try {
                    String[] tmp = textureDim.item(0).getTextContent().split(",");
                    if (tmp.length == 2) {
                        textureDims = new Dimension(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
                    }
                } catch (NumberFormatException e) {
                    throw new ModelFormatException("Model " + file + " contains a TextureSize tag with invalid data");
                }
            }

            NodeList shapes = document.getElementsByTagName("Shape");

            for (int i = 0; i < shapes.getLength(); i++) {
                Node shape = shapes.item(i);
                NamedNodeMap shapeAttributes = shape.getAttributes();
                if (shapeAttributes == null) {
                    throw new ModelFormatException("Shape #" + (i + 1) + " in " + file + " has no attributes");
                }

                Node name = shapeAttributes.getNamedItem("name");
                String shapeName = null;
                if (name != null) {
                    shapeName = name.getNodeValue();
                }
                if (shapeName == null) {
                    shapeName = "Shape #" + (i + 1);
                }

                String shapeType = null;
                Node type = shapeAttributes.getNamedItem("type");
                if (type != null) {
                    shapeType = type.getNodeValue();
                }
                if (shapeType != null && !cubeTypes.contains(shapeType)) {
                    Log.error("Model shape [" + shapeName + "] in " + file + " is not a cube, ignoring");
                    continue;
                }

                try {
                    boolean mirrored = false;
                    String[] offset = new String[3];
                    String[] position = new String[3];
                    String[] rotation = new String[3];
                    String[] size = new String[3];
                    String[] textureOffset = new String[2];

                    NodeList shapeChildren = shape.getChildNodes();
                    for (int j = 0; j < shapeChildren.getLength(); j++) {
                        Node shapeChild = shapeChildren.item(j);

                        String shapeChildName = shapeChild.getNodeName();
                        String shapeChildValue = shapeChild.getTextContent();
                        if (shapeChildValue != null) {
                            shapeChildValue = shapeChildValue.trim();

                            switch (shapeChildName) {
                                case "IsMirrored":
                                    mirrored = !shapeChildValue.equals("False");
                                    break;
                                case "Offset":
                                    offset = shapeChildValue.split(",");
                                    break;
                                case "Position":
                                    position = shapeChildValue.split(",");
                                    break;
                                case "Rotation":
                                    rotation = shapeChildValue.split(",");
                                    break;
                                case "Size":
                                    size = shapeChildValue.split(",");
                                    break;
                                case "TextureOffset":
                                    textureOffset = shapeChildValue.split(",");
                                    break;
                            }
                        }
                    }

                    Vect3d cubeSize = new Vect3d(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
                    Vect3d cubeOffset = new Vect3d(Float.parseFloat(offset[0]), -Float.parseFloat(offset[1]), Float.parseFloat(offset[2]));
                    Vect3d cubePosition = new Vect3d(Float.parseFloat(position[0]), -cubeSize.getY() - Float.parseFloat(position[1]), Float.parseFloat(position[2]));
                    Vect3d cubePosition0 = new Vect3d(Float.parseFloat(position[0]), -Float.parseFloat(position[1]), Float.parseFloat(position[2]));
                    Vect2i cubeTextureOffset = new Vect2i(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));
                    Vect3d cubeRotation = new Vect3d(
                            Math.toRadians(-Float.parseFloat(rotation[0])),
                            Math.toRadians(Float.parseFloat(rotation[1])),
                            Math.toRadians(-Float.parseFloat(rotation[2])));


                    TechneCube cube = new TechneCube(shapeName,
                            cubePosition.copy().add(cubeOffset).multiply(1 / 16d).add(0.5, 1.5, 0.5),
                            cubeSize, TextureStorage.MODEL_TEXTURE, cubeTextureOffset.toVect2d(),
                            (int) Math.max(textureDims != null ? textureDims.getWidth() : 32, textureDims != null ? textureDims.getHeight() : 32));

                    cube.setRotation(cubeRotation);
                    cube.setFlipped(mirrored);
                    cube.setRotationPoint(cubePosition0.copy().multiply(1 / 16d).add(0.5, 1.5, 0.5));
                    cube.getQuads();

                    parts.add(cube);
                } catch (NumberFormatException e) {
                    Log.error("Model shape [" + shapeName + "] in " + file + " contains malformed integers within its data, ignoring");
                    e.printStackTrace();
                }
            }
        } catch (ZipException e) {
            throw new ModelFormatException("Model " + file + " is not a valid zip file");
        } catch (IOException e) {
            throw new ModelFormatException("Model " + file + " could not be read", e);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new ModelFormatException("Model " + file + " contains invalid XML", e);
        }

        return parts;
    }
}
