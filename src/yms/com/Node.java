package yms.com;

import java.io.Serializable;

public class Node implements Serializable{
    private Person data;
    public Node next = null;

    public Node(Person data) {
        this.data = data;
    }

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }
}
