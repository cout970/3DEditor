package com.cout970.editor.util.data;

import com.cout970.bfs.BFSTagCompound;

/**
 * Created by cout970 on 10/02/2016.
 */
public class BFSDataCompound implements IDataCompound {

    private BFSTagCompound data;

    public BFSDataCompound(BFSTagCompound data) {
        this.data = data;
    }

    @Override
    public void removeKey(String name) {
        data.removeTag(name);
    }

    @Override
    public boolean containsKey(String name) {
        return data.hasKey(name);
    }

    @Override
    public void setInteger(String name, int value) {
        data.setInteger(name, value);
    }

    @Override
    public void setLong(String name, long value) {
        data.setLong(name, value);
    }

    @Override
    public void setFloat(String name, float value) {
        data.setFloat(name, value);
    }

    @Override
    public void setDouble(String name, double value) {
        data.setDouble(name, value);
    }

    @Override
    public void setString(String name, String value) {
        data.setString(name, value);
    }

    @Override
    public void setIntegerArray(String name, int[] value) {
        data.setIntegerArray(name, value);
    }

    @Override
    public void setByte(String name, byte value) {
        data.setByte(name, value);
    }

    @Override
    public void setByteArray(String name, byte[] value) {
        data.setByteArray(name, value);
    }

    @Override
    public void setBoolean(String name, boolean value) {
        data.setBoolean(name, value);
    }

    @Override
    public void setDataElement(String name, IDataElement value) {
        data.setTag(name, value.getBFS());
    }

    @Override
    public int getInteger(String name) {
        return data.getInteger(name);
    }

    @Override
    public long getLong(String name) {
        return data.getLong(name);
    }

    @Override
    public float getFloat(String name) {
        return data.getFloat(name);
    }

    @Override
    public double getDouble(String name) {
        return data.getDouble(name);
    }

    @Override
    public String getString(String name) {
        return data.getString(name);
    }

    @Override
    public int[] getIntegerArray(String name) {
        return data.getIntegerArray(name);
    }

    @Override
    public byte getByte(String name) {
        return data.getByte(name);
    }

    @Override
    public byte[] getByteArray(String name) {
        return data.getByteArray(name);
    }

    @Override
    public boolean getBoolean(String name) {
        return data.getBoolean(name);
    }

    @Override
    public IDataElement getDataElement(String name) {
        return DataElementFactory.fromBFS(data.getTag(name));
    }

    @Override
    public IDataList getDataList(String name) {
        return DataElementFactory.fromBFSList(data.getTagList(name, 9));
    }

    @Override
    public IDataCompound getDataCompound(String name) {
        return DataElementFactory.fromBFSCompound(data.getBFSTagCompound(name));
    }

    @Override
    public IDataCompound copy() {
        return new BFSDataCompound((BFSTagCompound) data.copy());
    }
}
