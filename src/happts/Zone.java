package happts;

class Zone{
    int size;
    //分区始址
    int head;
    boolean isFree;

    Zone(int head, int size) {
        this.head = head;
        this.size = size;
        this.isFree = true;
    }
}