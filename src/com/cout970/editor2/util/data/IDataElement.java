package com.cout970.editor2.util.data;

import com.cout970.bfs.BFSBase;

public interface IDataElement {

    byte getID();

    IDataElement copy();

    BFSBase getBFS();
}