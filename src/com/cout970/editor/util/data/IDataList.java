package com.cout970.editor.util.data;

/**
 * Created by cout970 on 21/12/2015.
 */
public interface IDataList extends IDataElement {

    int getSize();

    IDataCompound getDataCompound(int index);

    void addDataCompound(IDataCompound data);

    void setDataCompound(IDataCompound data, int index);

    void removeDataCompound(int index);

    @Override
    IDataList copy();
}
