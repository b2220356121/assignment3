import java.time.LocalDate;


public class Book {
    private int Id;
    private String type;
    private String where="";
    private boolean inLibrary;
    private LocalDate deadline;
    private LocalDate borrowTime;
    private int countOfExtend;
    private Member reader;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public LocalDate getBorrowTime() {
        return borrowTime;
    }


    public void setBorrowTime(LocalDate borrowTime) {
        this.borrowTime = borrowTime;
    }


    public Member getReader() {
        return reader;
    }

    public void setReader(Member reader) {
        this.reader = reader;
    }


    public Book(String type, int Id) {
        this.type = type;
        this.Id=Id;
        inLibrary=true;
        countOfExtend=0;
        where="";
    }
    public void infos(){
        System.out.println(Id+ type+ inLibrary+ deadline+ countOfExtend);
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



    public void setInLibrary(boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean getInLibrary() {
        return inLibrary;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getCountOfExtend() {
        return countOfExtend;
    }

}
