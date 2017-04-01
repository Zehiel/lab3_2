package edu.iis.mto.staticmock;

import java.util.ArrayList;
import java.util.List;

public class IncomingNews {

    private List<IncomingInfo> elems = new ArrayList<>(10);

    public List<IncomingInfo> elems() {
        return elems;
    }

    public void add(IncomingInfo info) {
        elems.add(info);
    }
}
