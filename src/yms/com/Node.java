package yms.com;

public class Node {
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
