public class Member {
    private int Id;
    private String type;
    private int numberOfExtend;
    private int numberOfBorrow;

    public int getNumberOfExtend() {
        return numberOfExtend;
    }

    public void setNumberOfExtend(int numberOfExtend) {
        this.numberOfExtend = numberOfExtend;
    }

    public int getNumberOfBorrow() {
        return numberOfBorrow;
    }

    public void setNumberOfBorrow(int numberOfBorrow) {
        this.numberOfBorrow = numberOfBorrow;
    }

    public Member(int id, String type) {
        Id = id;
        this.type = type;
        this.numberOfExtend=0;
        this.numberOfBorrow=0;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
