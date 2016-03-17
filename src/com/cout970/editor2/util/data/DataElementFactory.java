package com.cout970.editor2.util.data;

import com.cout970.bfs.BFSBase;
import com.cout970.bfs.BFSTagCompound;
import com.cout970.bfs.BFSTagList;

/**
 * Created by cout970 on 21/12/2015.
 */
public abstract class DataElementFactory {

    protected static DataElementFactory INSTANCE;

    public static IDataCompound createDataCompound() {
        return new BFSDataCompound(new BFSTagCompound());
    }

    public static IDataList createDataList() {
        return null;//TODO
    }

    public static IDataElement fromBFS(BFSBase tag) {
        return null;//TODO
    }

    public static IDataList fromBFSList(BFSTagList tagList) {
        return null;//TODO
    }

    public static IDataCompound fromBFSCompound(BFSTagCompound bfsTagCompound) {
        return null;//TODO
    }
}
