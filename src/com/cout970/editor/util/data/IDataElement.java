package com.cout970.editor.util.data;

import com.cout970.bfs.BFSBase;

public interface IDataElement {

    byte getID();

    IDataElement copy();

    BFSBase getBFS();
}