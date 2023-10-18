
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static String[] readFile(String path){
        try{
            int i=0;
            // Get the number of lines in the file
            int length= Files.readAllLines(Paths.get(path)).size();
            String[] results=new String[length];
            // Read all the lines from the file and add them to the result array
            for (String line: Files.readAllLines(Paths.get(path))){
                results[i++]=line;
            }
            return results;
            // If there is an error while reading the file, print the stack trace and return null
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) throws IOException {
        FileWriter myWriter = new FileWriter(args[1]);
        //This code reads a file, initializes two ArrayLists, and splits each line by tabs to create an ArrayList of commands.
        String input=args[0];
        String[] lines=readFile (input);
        ArrayList<Book> library=new ArrayList<Book>();
        ArrayList<Member> memberList=new ArrayList<Member>();
        int idCounter=0;
        int memberCounter=0;
        for (String line : lines) {
            //This code adds a new book to the library ArrayList and increments the idCounter.
            String[] splitCommands = line.split("\t");
            ArrayList<String> commandsLine = new ArrayList<>(Arrays.asList(splitCommands));
            if (commandsLine.get(0).equals("addBook")){
                idCounter+=1;
                Book newBook= new Book(String.valueOf(commandsLine.get(1)),idCounter);
                library.add(newBook);
                //This code prints a message indicating the book type (handwritten or printed) and the ID of the newly created book.
                if(newBook.getType().equals("H")){
                    myWriter.write("Created new book: Handwritten [id: "+newBook.getId()+"]"+"\n");
                }else if(newBook.getType().equals("P")){
                    myWriter.write("Created new book: Printed [id: "+newBook.getId()+"]"+"\n");
                }
            }
            else if(commandsLine.get(0).equals("addMember")){
                //This code creates a member object based on the member type
                memberCounter+=1;
                Member newMember= new Member(memberCounter,String.valueOf(commandsLine.get(1)));
                newMember.setNumberOfBorrow(0);
                memberList.add(newMember);

                if(newMember.getType().equals("S")){
                    myWriter.write("Created new member: Student [id: "+newMember.getId()+"]"+"\n");
                } else if (newMember.getType().equals("A")){
                    myWriter.write("Created new member: Academic [id: "+newMember.getId()+"]"+"\n");
                }

            }

            else if( commandsLine.get(0).equals("borrowBook")){
                // Iterate through the list of members to find the one with the matching ID.
                for (Member m:memberList){
                    if (m.getId()==Integer.parseInt(commandsLine.get(2))){
                        for (Book i:library){
                            if (i.getId()==Integer.parseInt(commandsLine.get(1))){
                                if(!i.getInLibrary()){
                                    myWriter.write("You can not read this book!"+"\n");
                                    // Check if the book is currently available for borrowing before lending it to a member.
                                }else {
                                    if(m.getType().equals("S")& m.getNumberOfBorrow()<2){
                                        m.setNumberOfBorrow(m.getNumberOfBorrow()+1);
                                        i.setInLibrary(false);
                                        i.setReader(m);
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        LocalDate localDate = LocalDate.parse(commandsLine.get(3), formatter);
                                        i.setBorrowTime(localDate);
                                        // Set the borrowing deadline based on the type of the borrowing member.
                                        i.setDeadline(localDate.plusDays(7));
                                        myWriter.write("The book ["+i.getId()+ "] was borrowed by member ["+m.getId()+"] at "+i.getBorrowTime()+"\n");

                                    }else if(m.getType().equals("A")& m.getNumberOfBorrow()<4){
                                        i.setReader(m);
                                        m.setNumberOfBorrow(m.getNumberOfBorrow()+1);
                                        i.setInLibrary(false);
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        LocalDate localDate = LocalDate.parse(commandsLine.get(3), formatter);
                                        i.setBorrowTime(localDate);
                                        i.setDeadline(localDate.plusDays(14));
                                        myWriter.write("The book ["+i.getId()+ "] was borrowed by member ["+m.getId()+"] at "+i.getBorrowTime()+"\n");
                                    }else{
                                        myWriter.write("You have exceeded the borrowing limit!"+"\n");
                                    }


                                }

                            }
                        }
                    }
                }
            }
            else if (commandsLine.get(0).equals("returnBook")){
                // Retrieve the book from the library with the ID provided in the command line.
                for (Book i:library){
                    if(Integer.parseInt(commandsLine.get(1))==(i.getId())){
                        for(Member m:memberList){
                            // Retrieve the member from the member list with the ID provided in the command line.
                            if(Integer.parseInt(commandsLine.get(2))==m.getId()){
                                // Parse the date provided in the command line and calculate the number of days between it and the borrowing deadline.
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate localReturnDate = LocalDate.parse(commandsLine.get(3), formatter);
                                int diff=(int)(ChronoUnit.DAYS.between(i.getDeadline(), localReturnDate));
                                if (diff<0){
                                    diff=0;
                                }
                                if (i.getDeadline().isBefore(localReturnDate)){
                                    if (i.getWhere().equals("l")){
                                        i.setWhere("");
                                    }
                                    i.setInLibrary(true);
                                    m.setNumberOfBorrow(m.getNumberOfBorrow()-1);
                                    // If the book is returned on or before the deadline, there is no late fee, so set the fee to 0.
                                    myWriter.write("The book ["+i.getId()+"] was returned by member ["+m.getId()+"] at "+localReturnDate+" Fee: "+
                                            diff+"\n");
                                }
                                else if(i.getDeadline().isAfter(localReturnDate) || i.getDeadline().isEqual(localReturnDate)){
                                    if (i.getWhere().equals("l")){
                                        i.setWhere("");
                                    }
                                    m.setNumberOfBorrow(m.getNumberOfBorrow()-1);
                                    myWriter.write("The book ["+i.getId()+"] was returned by member ["+m.getId()+"] at "+localReturnDate+" Fee: "+
                                            diff+"\n");
                                    i.setInLibrary(true);
                                }


                            }
                        }
                    }

                }
            }
            else if (commandsLine.get(0).equals("extendBook")){
                // Loop through the library to find the book with the given ID
                for (Book i: library){
                    if(Integer.parseInt(commandsLine.get(1))==(i.getId())){
                        // Loop through the member list to find the member with the given ID
                        for (Member m:memberList){
                            if(Integer.parseInt(commandsLine.get(2))==m.getId()){
                                // Parse the date string into a LocalDate object
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate localCurrentDate = LocalDate.parse(commandsLine.get(3), formatter);
                                if (!localCurrentDate.isAfter(i.getDeadline())&i.getReader().getNumberOfExtend()==0){
                                    i.getReader().setNumberOfExtend(i.getReader().getNumberOfExtend()+1);
                                    if (m.getType().equals("S")){
                                        // Check if the book is available to be read in the library
                                        i.setDeadline(i.getDeadline().plusDays(7));
                                        myWriter.write("The deadline of book ["+i.getId()+"] was extended by member ["+m.getId()+"] at "+i.getDeadline()+"\n");
                                    }else if(m.getType().equals("A")){
                                        i.setDeadline(i.getDeadline().plusDays(14));
                                        myWriter.write("The deadline of book ["+i.getId()+"] was extended by member ["+m.getId()+"] at "+i.getDeadline()+"\n");
                                    }
                                    myWriter.write("New deadline of book ["+i.getId()+"] is "+ i.getDeadline()+"\n");

                                }else{
                                    myWriter.write("You cannot extend the deadline!"+"\n");
                                }
                            }
                        }
                    }
                }

            }
            else if (commandsLine.get(0).equals("readInLibrary")){
                //When readInLibrary command is used, it checks if the specified book is available in the library.
                //If the book is not in the library, you can not read it.
                for (Book i: library){
                    if(Integer.parseInt(commandsLine.get(1))==(i.getId())){
                        for (Member m:memberList){
                            if(Integer.parseInt(commandsLine.get(2))==m.getId()){
                                if(!i.getInLibrary()){
                                    myWriter.write("You can not read this book!"+"\n");
                                }else{
                                    //If the book is a handwritten one, students can not read it.
                                    if (i.getType().equals("H") & m.getType().equals("S")){
                                        myWriter.write("Students can not read handwritten books!"+"\n");
                                    }
                                    else{
                                        //The current date is used to set the deadline and borrowing time of the book.
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        LocalDate localCurrentDate = LocalDate.parse(commandsLine.get(3), formatter);
                                        i.setDeadline(localCurrentDate);
                                        i.setBorrowTime(localCurrentDate);
                                        i.setReader(m);
                                        //The book's status is updated to indicate that it has been borrowed by a member from the library.
                                        i.setWhere("l");
                                        i.setInLibrary(false);
                                        myWriter.write("The book ["+i.getId()+"] was read in library by member ["+m.getId()+"] at "+localCurrentDate+"\n");
                                    }
                                }


                        }
                    }
                }
            }



            }
            else if (commandsLine.get(0).equals("getTheHistory")){
                //This code block generates a report on the history of a library system.
                myWriter.write("History of library:"+"\n");
                myWriter.write(""+"\n");
                //It counts the number of books and members of different types, and prints the results.
                int numberOfStudent=0;
                int numberOfAcademic=0;
                int numberOfPrinted=0;
                int numberOfHandwritten=0;
                int numberOfBorrow=0;
                int numberOfReadInlibary=0;

                for (Member m: memberList){
                    if (m.getType().equals("S")){
                        numberOfStudent+=1;
                    }
                    if(m.getType().equals("A")){
                        numberOfAcademic+=1;
                    }
                }
                for (Book b: library){
                    if(b.getType().equals("H")){
                        numberOfHandwritten+=1;
                    }
                    if(b.getType().equals("P")){
                        numberOfPrinted+=1;
                    }
                    if(!b.getInLibrary()& b.getWhere().equals("")){
                        numberOfBorrow+=1;
                    }
                    if(b.getWhere().equals("l")){
                        numberOfReadInlibary+=1;
                    }
                }
                //It also lists borrowed and read-in-library books, along with the relevant member information.
                myWriter.write("Number of students: "+numberOfStudent+"\n");
                for (Member m: memberList){
                    if (m.getType().equals("S")){
                        myWriter.write("Student [id: " + m.getId() +"]"+"\n");
                    }
                }
                myWriter.write(""+"\n");
                myWriter.write("Number of academics: "+numberOfAcademic+"\n");
                for (Member m: memberList){
                    if (m.getType().equals("A")){
                        myWriter.write("Academic [id: " + m.getId() +"]"+"\n");
                    }
                }
                myWriter.write(""+"\n");
                myWriter.write("Number of printed books: "+numberOfPrinted+"\n");
                for (Book b: library){
                    if(b.getType().equals("P")){
                        myWriter.write("Printed [id: "+ b.getId()+"]"+"\n");
                    }
                }
                myWriter.write(""+"\n");
                myWriter.write("Number of handwritten books: "+numberOfHandwritten+"\n");
                for (Book b: library){
                    if(b.getType().equals("H")){
                        myWriter.write("Handwritten [id: "+ b.getId()+"]"+"\n");
                    }
                }
                myWriter.write(""+"\n");
                myWriter.write("Number of borrowed books: "+numberOfBorrow+"\n");
                for (Book b: library){
                    if(!b.getInLibrary() & b.getWhere().equals("")){
                        myWriter.write("The book ["+ b.getId() +"] was borrowed by member ["+b.getReader().getId() +"] at "+b.getBorrowTime()+"\n");
                    }
                }
                myWriter.write(""+"\n");
                myWriter.write("Number of books read in library: "+numberOfReadInlibary+"\n");

                for (Book b:library){
                    if(!b.getInLibrary() & b.getWhere().equals("l")){
                        myWriter.write("The book ["+ b.getId() +"] was read in library by member ["+b.getReader().getId() +"] at "+b.getBorrowTime()+"\n");
                    }
                }

            }
    }myWriter.close();
    }
}
