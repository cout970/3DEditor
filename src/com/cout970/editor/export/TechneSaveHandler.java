package com.cout970.editor.export;

import com.cout970.editor.Editor;
import com.cout970.editor.ModelTree;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.tools.Project;
import com.cout970.editor.util.Log;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;
import com.cout970.editor.util.Vect3d;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;

/**
 * Created by cout970 on 01/03/2016.
 */
public class TechneSaveHandler implements ISaveHandler {

    public static final List<String> cubeTypes = Arrays.asList(
            "d9e621f7-957f-4b77-b1ae-20dcd0da7751",
            "de81aa14-bd60-4228-8d8d-5238bcd3caaa"
    );

    @Override
    public void save(File file, Project project) {
        try {
            save0(file, project);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Project load(File file) {
        try {
            return load0(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void save0(File file, Project project) throws FileNotFoundException {
        OutputStream stream = new FileOutputStream(file);
        ZipOutputStream zipOutput = new ZipOutputStream(stream);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();

            //root
            Element rootNode = doc.createElement("Techne");
            doc.appendChild(rootNode);

            //version
            Attr version = doc.createAttribute("Version");
            version.setValue("2.2");//current techne version
            rootNode.setAttributeNode(version);

            //autor
            Element autorNode = doc.createElement("Author");
            autorNode.setTextContent(project.getAutor() == null || project.getAutor().equals("") ? "ZeuX" : project.getAutor());
            rootNode.appendChild(autorNode);

            //date
            Element dateNode = doc.createElement("DateCreated");
            dateNode.setTextContent(project.getDataCreation() == null ? "" : project.getDataCreation());
            rootNode.appendChild(dateNode);

            //description
            Element descriptionNode = doc.createElement("Description");
            rootNode.appendChild(descriptionNode);

            //models
            Element modelsNode = doc.createElement("Models");
            {
                Element modelNode = doc.createElement("Model");
                //model
                modelNode.setAttribute("texture", "none");

                Element baseClassNode = doc.createElement("BaseClass");
                baseClassNode.setTextContent("ModelBase");
                modelNode.appendChild(baseClassNode);

                Element geometryNode = doc.createElement("Geometry");
                project.getModelTree().getAllModels().stream().filter(m -> m instanceof TechneCube).forEach(m -> {
                    TechneCube cube = (TechneCube) m;
                    Element shapeNode = doc.createElement("Shape");
                    shapeNode.setAttribute("name", cube.getName());
                    shapeNode.setAttribute("type", cubeTypes.get(0));
                    {// Animation
                        Element animationNode = doc.createElement("Animation");
                        Element animationAnglesNode = doc.createElement("AnimationAngles");
                        animationAnglesNode.setTextContent("0,0,0");
                        animationNode.appendChild(animationAnglesNode);
                        Element animationDurationNode = doc.createElement("AnimationDuration");
                        animationDurationNode.setTextContent("0,0,0");
                        animationNode.appendChild(animationDurationNode);
                        Element animationTypeNode = doc.createElement("AnimationType");
                        animationTypeNode.setTextContent("0,0,0");
                        animationNode.appendChild(animationTypeNode);
                        shapeNode.appendChild(animationNode);
                    }
                    //other
                    Element isDecorativeNode = doc.createElement("IsDecorative");
                    isDecorativeNode.setTextContent("False");
                    shapeNode.appendChild(isDecorativeNode);
                    Element isFixedNode = doc.createElement("IsFixed");
                    isFixedNode.setTextContent("False");
                    shapeNode.appendChild(isFixedNode);
                    Element isMirroredNode = doc.createElement("IsMirrored");
                    isMirroredNode.setTextContent(cube.isFlipped() ? "True" : "False");
                    shapeNode.appendChild(isMirroredNode);

                    //cube properties
                    Vect3d size = cube.getSize();
                    Vect3d position = cube.getRotationPoint().subtract(0.5, 1.5, 0.5).multiply(16);
                    Vect3d position0 = new Vect3d(position.getX(), position.getY() - size.getY(), position.getZ());
                    //-cubeSize.getY() - Float.parseFloat(position[1])
                    //cubePosition.copy().add(cubeOffset).multiply(1 / 16d).add(0.5, 1.5, 0.5),
                    Vect3d offset = cube.getPos().subtract(0.5, 1.5, 0.5).multiply(16).sub(position0);
                    Vect3d rotation = cube.getRotation().toDegrees();
                    Vect2d textureOffset = cube.getTextureOffset();

                    Element offsetNode = doc.createElement("Offset");
                    offsetNode.setTextContent(format(offset.getX()) + "," + format(-offset.getY()) + "," + format(offset.getZ()));
                    shapeNode.appendChild(offsetNode);

                    Element positionNode = doc.createElement("Position");
                    positionNode.setTextContent(format(position.getX()) + "," + format(-position.getY()) + "," + format(position.getZ()));
                    shapeNode.appendChild(positionNode);

                    Element rotationNode = doc.createElement("Rotation");
                    rotationNode.setTextContent(format(-rotation.getX()) + "," + format(rotation.getY()) + "," + format(-rotation.getZ()));
                    shapeNode.appendChild(rotationNode);

                    Element sizeNode = doc.createElement("Size");
                    sizeNode.setTextContent(format(size.getX()) + "," + format(size.getY()) + "," + format(size.getZ()));
                    shapeNode.appendChild(sizeNode);

                    Element textureNode = doc.createElement("TextureOffset");
                    textureNode.setTextContent(format(textureOffset.getX()) + "," + format(textureOffset.getY()));
                    shapeNode.appendChild(textureNode);

                    geometryNode.appendChild(shapeNode);
                });
                modelNode.appendChild(geometryNode);

                Element scaleNode = doc.createElement("GlScale");
                scaleNode.appendChild(doc.createTextNode("1,1,1"));
                modelNode.appendChild(scaleNode);

                Element nameNode = doc.createElement("Name");
                nameNode.appendChild(doc.createTextNode(project.getName() == null ? "" : project.getName()));
                modelNode.appendChild(nameNode);

                Element textureSizeNode = doc.createElement("TextureSize");
                textureSizeNode.appendChild(doc.createTextNode("16,16"));//TODO add several models with the different texture sizes
                modelNode.appendChild(textureSizeNode);

                modelsNode.appendChild(modelNode);
            }
            rootNode.appendChild(modelsNode);

            //name
            Element nameNode = doc.createElement("Name");
            nameNode.setTextContent(project.getName() == null ? "" : project.getName());
            rootNode.appendChild(nameNode);

            //previewImage
            Element prevImageNode = doc.createElement("PreviewImage");
            prevImageNode.setTextContent(project.getPreviewImagePath() == null || project.getDataCreation().equals("") ? "" : project.getPreviewImagePath());
            rootNode.appendChild(prevImageNode);

            //projectName
            Element projectNameNode = doc.createElement("ProjectName");
            projectNameNode.setTextContent(project.getProjectName() == null ? "" : project.getProjectName());
            rootNode.appendChild(projectNameNode);

            //projectType
            Element projectTypeNode = doc.createElement("ProjectType");
            projectTypeNode.setTextContent(project.getProjectType() == null ? "Minecraft" : project.getProjectType());
            rootNode.appendChild(projectTypeNode);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File temp = new File("temp.xml");
            FileOutputStream out = new FileOutputStream(temp);
            StreamResult result = new StreamResult(out);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
            out.flush();
            out.close();

            FileInputStream fis = new FileInputStream(temp);
            ZipEntry entry = new ZipEntry("model.xml");
            zipOutput.putNextEntry(entry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOutput.write(bytes, 0, length);
            }
            zipOutput.closeEntry();
            temp.deleteOnExit();
            zipOutput.close();
            stream.close();
            Thread.currentThread().sleep(1000);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String format(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    private Project load0(File file) throws FileNotFoundException {
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
                //TODO fix getSize() == -1
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

            //root
            NodeList nodeListTechne = document.getElementsByTagName("Techne");
            if (nodeListTechne.getLength() < 1) {
                throw new ModelFormatException("Model " + file + " contains no Techne tag");
            }

            //autor
            String autor = "";
            NodeList nodeListAutor = document.getElementsByTagName("Autor");
            if (nodeListAutor.getLength() > 0) {
                autor = nodeListAutor.item(0).getNodeValue();
            }

            //creation date
            String creationDate = "";
            NodeList nodeListCreationDate = document.getElementsByTagName("DateCreated");
            if (nodeListCreationDate.getLength() > 0) {
                creationDate = nodeListCreationDate.item(0).getNodeValue();
            }

            //name
            String projectName = "";
            NodeList nodeListName = document.getElementsByTagName("Name");
            if (nodeListName.getLength() > 0) {
                projectName = nodeListName.item(0).getNodeValue();
            }

            //previewImagePath
            String previewImagePath = "";
            NodeList nodeListPreviewImagePath = document.getElementsByTagName("PreviewImage");
            if (nodeListPreviewImagePath.getLength() > 0) {
                previewImagePath = nodeListPreviewImagePath.item(0).getNodeValue();
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
            ModelTree m = new ModelTree(parts);
            m.init();
            return new Project(Editor.EDITOR_VERSION, autor, creationDate, projectName, projectName, previewImagePath, "Minecraft", m);
        } catch (ZipException e) {
            throw new ModelFormatException("Model " + file + " is not a valid zip file");
        } catch (IOException e) {
            throw new ModelFormatException("Model " + file + " could not be read", e);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new ModelFormatException("Model " + file + " contains invalid XML", e);
        }
        return null;
    }
}
